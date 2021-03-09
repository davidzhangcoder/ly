package com.leyou.service.impl;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.Constants;
import com.leyou.common.utils.NumberUtils;
import com.leyou.configuration.RabbitMQSMSConfiguration;
import com.leyou.dao.UserDao;
import com.leyou.domain.User;
import com.leyou.service.UserService;
import com.leyou.utils.CheckType;
import com.leyou.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service( value = "UserServiceImpl" )
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RabbitMQSMSConfiguration rabbitMQConfiguration;

    @Autowired
    @Qualifier( value = "leyouSMSRabbitTemplate" )
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Boolean check(String data, Long type) {

        if(StringUtils.isEmpty(data) || type == null ) {
            throw new LyException(ExceptionEnum.DATA_CHECK_TYPE_NOT_EXIST);
        }

        if (type == CheckType.USERNAME.getId()) {
            //直接使用匿名内部类实现接口
            Specification specification = new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicateList = new ArrayList<Predicate>();

                    Predicate phonePredicate = criteriaBuilder.equal(root.get("username").as(String.class), data);
                    predicateList.add(phonePredicate);

                    Predicate[] pre = new Predicate[predicateList.size()];
                    pre = predicateList.toArray(pre);
                    return criteriaQuery.where(pre).getRestriction();
                }
            };

            return userDao.count( specification ) == 0;
        }
        else if (type == CheckType.PHONE.getId()) {
            User user = new User();
            user.setPhone(data);
            Example<User> example = Example.of(user);

            return userDao.count( example ) == 0;
        }
        else {
            throw new LyException(ExceptionEnum.DATA_CHECK_TYPE_NOT_EXIST);
        }
    }

    @Override
    public void code(String phone) {

        String code = NumberUtils.generateCode( 6 );

//        for(int i = 0 ; i < 100 ; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("code", code);
            map.put("phone", phone);

            amqpTemplate.convertAndSend(rabbitMQConfiguration.getSmsExchange(),
                    rabbitMQConfiguration.getSmsRouteringKey(),
                    map);

//        }
    }

    @Override
    public void register(User user, String code) {

        String username = user.getUsername();

        if( !check(username, CheckType.USERNAME.getId()) ) {
            throw new LyException(ExceptionEnum.USER_ALREADY_EXIST);
        }

        String codeFromRedis = (String) redisTemplate.opsForValue().get(Constants.KEY_PREFIX + user.getPhone());
        if(StringUtils.isEmpty(codeFromRedis)) {
            logger.info("[用户服务 － register] : 注册码已过期");
            throw new LyException(ExceptionEnum.CODE_EXPIRED);
        }
        else if( !codeFromRedis.equals(code) ){
            logger.info("[用户服务 － register] : 注册码错误");
            throw new LyException(ExceptionEnum.CODE_INVALID);
        }

        String salt = CodecUtils.generateSalt();
        String encodedPassWord = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(encodedPassWord);
        user.setSalt(salt);

        user.setCreated(Calendar.getInstance().getTime());
        userDao.save(user);
    }

    @Override
    public User query(String username, String password) {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
            logger.info("[用户服务 － query] : 参数错误");
            throw new LyException(ExceptionEnum.INVALID_PARAMETER);
        }

        User user = new User();
        user.setUsername(username);
        Example<User> example = Example.of(user);
        Optional<User> userOptional = userDao.findOne(example);

        if (!userOptional.isPresent()) {
            logger.info("[用户服务 － query] : 用户不存在");
            return null;
        }

        user = userOptional.get();
        String inputPassword = CodecUtils.md5Hex(password, user.getSalt());
        if(!inputPassword.equals(user.getPassword())) {
            logger.info("[用户服务 － query] : 密码错误");
            return null;
        }

        return user;
    }

    @Override
    public User queryForOAuth2(String username, String password) {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)) {
            logger.info("[用户服务 － query] : 参数错误");
            throw new LyException(ExceptionEnum.INVALID_PARAMETER);
        }

        User user = new User();
        user.setUsername(username);
        Example<User> example = Example.of(user);
        Optional<User> userOptional = userDao.findOne(example);

        if (!userOptional.isPresent()) {
            logger.info("[用户服务 － query] : 用户不存在");
            return null;
        }

        user = userOptional.get();

        if(!BCrypt.checkpw(password,user.getPassword())) {
            logger.info("[用户服务 － query] : 密码错误");
            return null;
        }

        return user;
    }

    @Override
    public User findUserByUsernameForOAuth2(String username){
        if(StringUtils.isEmpty(username)) {
            logger.info("[用户服务 － query] : 参数错误");
            throw new LyException(ExceptionEnum.INVALID_PARAMETER);
        }

        User userByUsername = userDao.findUserByUsername(username);
        return userByUsername;
    }

    public void registerForOAuth2(User user){
        String username = user.getUsername();

        if( !check(username, CheckType.USERNAME.getId()) ) {
            throw new LyException(ExceptionEnum.USER_ALREADY_EXIST);
        }

        String salt = CodecUtils.generateSalt();
        String encodedPassWord = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encodedPassWord);
        user.setSalt(salt);

        user.setCreated(Calendar.getInstance().getTime());
        userDao.save(user);

    }
}
