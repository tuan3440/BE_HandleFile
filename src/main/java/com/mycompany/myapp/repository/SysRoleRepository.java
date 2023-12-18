package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysRole;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long> {
    @Query(
        value = "select sr " +
        " from SysRole sr " +
        " where 1=1 " +
        " and (:keyword is null or lower(sr.name) like :keyword or lower(sr.code) like :keyword ) " +
        " and (:status is null or :status = -1 or sr.status = :status)",
        countQuery = "select count(sr) from SysRole sr " +
        " where 1=1 " +
        " and (:keyword is null or lower(sr.name) like :keyword or lower(sr.code) like :keyword) " +
        " and (:status is null or :status = -1 or sr.status = :status)"
    )
    Page<SysRole> doSearch(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    @Query(value = "select sr " + "from SysRole sr " + "where sr.code = :code")
    SysRole getSysRoleByCode(@Param("code") String code);

    @Query(value = "select sr " + "from SysRole sr " + "where sr.code = :code and sr.id != :id")
    List<SysRole> getSysRoleByCodeAndId(@Param("code") String code, @Param("id") Long id);
}
