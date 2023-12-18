package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysActionRepository extends JpaRepository<SysAction, Long> {
    @Query(
        value = "select sa " +
        " from SysAction sa " +
        " where 1=1 " +
        " and (:keyword is null or lower(sa.name) like :keyword or lower(sa.code) like :keyword ) " +
        " and (:status is null or :status = -1 or sa.status = :status)",
        countQuery = "select count(sa) from SysRole sa " +
        " where 1=1 " +
        " and (:keyword is null or lower(sa.name) like :keyword or lower(sa.code) like :keyword) " +
        " and (:status is null or :status = -1 or sa.status = :status)"
    )
    Page<SysAction> doSearch(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    boolean existsByCode(@Param("code") String code);

    boolean existsByCodeAndIdNot(@Param("code") String code, @Param("id") Long id);
}
