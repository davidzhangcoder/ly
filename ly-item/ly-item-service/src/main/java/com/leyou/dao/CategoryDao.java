package com.leyou.dao;

import com.leyou.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category,Long> , JpaSpecificationExecutor<Category> {

    public List<Category> findByParentID(long pid);

    @Query(value="SELECT * FROM `tb_category` WHERE id = (SELECT MAX(id) FROM tb_category)" ,nativeQuery=true)
    public List<Category> queryLast();

}
