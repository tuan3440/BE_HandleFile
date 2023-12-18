package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysModule;
import com.mycompany.myapp.service.dto.SysModuleDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, Long> {
    @Query(
        value = "select sm " +
        " from SysModule sm " +
        " where 1=1 " +
        " and (:keyword is null or lower(sm.name) like :keyword or lower(sm.code) like :keyword or lower(sm.description) like :keyword or lower(sm.pathUrl) like :keyword) " +
        " and (:status is null or :status = -1 or sm.status = :status) " +
        " and (:parentId is null or sm.parentId = :parentId)",
        countQuery = "select count(sm) from SysModule sm " +
        " where 1=1 " +
        " and (:keyword is null or lower(sm.name) like :keyword or lower(sm.code) like :keyword or lower(sm.description) like :keyword or lower(sm.pathUrl) like :keyword) " +
        " and (:status is null or :status = -1 or sm.status = :status) " +
        " and sm.parentId = :parentId"
    )
    Page<SysModule> doSearch(
        @Param("keyword") String keyword,
        @Param("status") Integer status,
        @Param("parentId") Long parentId,
        Pageable pageable
    );

    boolean existsByCode(@Param("code") String code);
    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);
    boolean existsByParentIdAndStatus(@Param("parentId") Long parentId, @Param("status") Integer status);

    @Query(value = "select sm " + " from SysModule  sm " + " where 1=1 " + " and sm.parentId = :parentId and sm.status = 1")
    List<SysModule> getChildModule(@Param("parentId") Long parentId);

    @Query(
        value = "select distinct sm " +
        " from SysModule  sm " +
        " where 1=1 " +
        " and sm.code in (select srm.moduleCode from SysRoleModule srm where  srm.roleId in (select sur.roleId from SysUserRole sur where sur.userId = :userId))"
    )
    List<SysModule> getMenuByUserId(@Param("userId") Long userId);
}
