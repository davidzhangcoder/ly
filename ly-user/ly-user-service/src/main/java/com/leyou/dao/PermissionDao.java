package com.leyou.dao;

import com.leyou.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionDao extends JpaRepository<Permission,Long>, JpaSpecificationExecutor<Permission> {

    public List<Permission> findAll();

    @Query(value="SELECT d.*  FROM tb_user a, user_role_relation b , role_permission_relation c, permission d  " +
            "WHERE a.id = :userId and a.id = b.user_id and b.role_id = c.role_id and c.permission_id = d.id" ,nativeQuery=true)
    public List<Permission> findByAdminUserId(long userId);

}