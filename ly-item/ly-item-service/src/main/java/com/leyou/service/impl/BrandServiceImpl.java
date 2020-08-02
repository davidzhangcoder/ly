package com.leyou.service.impl;

import com.leyou.common.vo.PageResult;
import com.leyou.dao.BrandDao;
import com.leyou.dao.CategoryDao;
import com.leyou.domain.Brand;
import com.leyou.domain.Category;
import com.leyou.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service( value = "BrandServiceImpl" )
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public PageResult<Brand> searchBrand(String key, boolean descending, int page, int rowsPerPage, String sortBy) {

        page--;
        page = page < 0 ? 0 : page;//page 为页码，数据库从0页开始
        //可以使用重载的 of(int page, int size, Sort sort) 方法指定排序字段
        //Pageable pageable = PageRequest.of((int)page, (int)rowsPerPage);

        // Order定义了排序规则。参数一是根据倒叙还是正序，参数二是根据那个字段进行排序。
        Sort.Order order = new Sort.Order((descending?Sort.Direction.DESC:Sort.Direction.ASC), sortBy);
        // Sort对象封装了排序的规则。
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(page, rowsPerPage , sort );


        // Specification specification = (a,b,c) ->{ return null; };

        //直接使用匿名内部类实现接口
        Specification specification = new Specification<Brand>() {
            @Override
            public Predicate toPredicate(Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                //条件1：查询 tvName 为 海信 的数据，root.get 中的值与 TV 实体中的属性名称对应
                if (!StringUtils.isBlank(key)) {
                    predicateList.add( cb.like( root.get("name").as(String.class), "%"+key+"%" ) );
                }

                //条件2：TV 生产日期（dateOfProduction）大于等于 start 的数据，root.get 中的 dateOfProduction 必须对应 TV 中的属性
                //predicateList.add(cb.greaterThanOrEqualTo(root.get("dateOfProduction").as(Date.class), start));

                //条件3：TV 生产日期（dateOfProduction）小于等于 end
                //predicateList.add(cb.lessThanOrEqualTo(root.get("dateOfProduction").as(Date.class), end));

                Predicate[] pre = new Predicate[predicateList.size()];
                pre = predicateList.toArray(pre);
                return query.where(pre).getRestriction();
            }
        };

        Page<Brand> brandPage = brandDao.findAll(specification , pageable);//没有数据时，返回空列表

//        if(CollectionUtils.isEmpty(brandPage.getContent())) {
//            throw new LyException(ExceptionEnum.BRAND_SEARCH_LIST_IS_EMPTY);
//        }

        PageResult<Brand> pageResult = new PageResult(brandPage.getTotalElements(), (long)brandPage.getTotalPages(), brandPage.getContent());

        return pageResult;
    }

    @Override
    public Brand persistBrand(Brand brand) {
        if( brand.getId() > 0 ) {
            Brand existingBrand = brandDao.getOne( brand.getId() );
            existingBrand.setName( brand.getName() );
            existingBrand.setLetter( brand.getLetter() );
            existingBrand.setImage( brand.getImage() );

            if( !CollectionUtils.isEmpty(existingBrand.getCategories()) ) {
                existingBrand.getCategories().clear();
            }

            updateBrandCategory(brand, existingBrand);

            existingBrand = brandDao.save( existingBrand );
            return existingBrand;
        }
        else {

            updateBrandCategory(brand, brand);

            brand = brandDao.save(brand);
            //Hibernate.initialize(brand);
            return brand;
        }
    }

    @Override
    public void deleteBrand(long brandId) {
        brandDao.deleteById(brandId);
    }

    @Override
    public Brand getBrandById(long brandId) {
        Brand brand = brandDao.findById(brandId).orElse(null);
        return brand;
    }

    private void updateBrandCategory(Brand brand, Brand targetBrand) {
        if (!CollectionUtils.isEmpty(brand.getCategories())) {
            List<Long> ids = brand.getCategories().stream().map(a -> a.getId()).collect(Collectors.toList());
            targetBrand.getCategories().clear();
            for (Long id : ids) {
                Category retrievedCategory = categoryDao.findById(id).get();
                if (retrievedCategory != null)
                    targetBrand.getCategories().add(retrievedCategory);
            }
        }
    }
}
