package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysUser;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findByEmail(String email);

    Optional<SysUser> findByUserName(String userName);

    Optional<SysUser> findByIdAndResetKey(long userId, String resetKey);

    @Query(
        value = "select su " +
        " from SysUser su " +
        " where 1=1 " +
        " and (:keyword is null or lower(su.userName) like :keyword or lower(su.fullName) like :keyword or lower(su.email) like :keyword or lower(su.cellphone) like :keyword ) " +
        " and (:status is null or :status = -1 or su.status = :status)",
        countQuery = "select count(su) from SysUser su " +
        " where 1=1 " +
        " and (:keyword is null or lower(su.userName) like :keyword or lower(su.fullName) like :keyword or lower(su.email) like :keyword or lower(su.cellphone) like :keyword ) " +
        " and (:status is null or :status = -1 or su.status = :status)"
    )
    Page<SysUser> doSearch(@Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);
}
