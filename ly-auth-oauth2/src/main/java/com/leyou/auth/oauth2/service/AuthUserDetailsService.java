package com.leyou.auth.oauth2.service;


import com.leyou.auth.oauth2.client.UserClient;
import com.leyou.auth.oauth2.utils.UserJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户
        com.leyou.domain.User user = userClient.findUserByUsernameForOAuth2(username);

        //ToDo: 查询权限

//        // 判断用户名是否存在
//        if (!"admin".equals(username) && !"sales".equals(username) && !"test".equals(username)) {
//            throw new UsernameNotFoundException("用户名不存在!");
//        }
        // 从数据库中获取的密码 atguigu 的密文
//        String pwd = "d7b79bb6d6f77e6cbb5df2d0d2478361"; //MD5
//        String pwd = "$2a$10$2R/M6iU3mCZt3ByG7kwYTeeW0w7/UqdeXrb27zkBIizBvAven0/na"; //

//        String pwd = "$2a$10$WvBXh3kN9v7wkXr/pkT.TO8x9v5jW/zcQR/gDdS1WhJN0itbVLSmi";//11111

        String pwd = user.getPassword();

        if("admin".equals(username)) {
            return new UserJwt( user.getId(), username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_managerrole,p1,ROLE_P1"));

//            User curUser = new User(username, pwd);
//            SecurityUser securityUser = new SecurityUser(curUser);
//
//            List<String> authorities = new ArrayList<String>();
//            authorities.add("admin");
//            authorities.add("ROLE_managerrole");
//            securityUser.setPermissionValueList(authorities);
//            return securityUser;
        }
        else {
            return new UserJwt( user.getId(), username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList("sales,testUserPermission"));

//            User curUser = new User(username, pwd);
//            SecurityUser securityUser = new SecurityUser(curUser);
//
//            List<String> authorities = new ArrayList<String>();
//            authorities.add("sales");
//            securityUser.setPermissionValueList(authorities);
//            return securityUser;

        }
    }
}