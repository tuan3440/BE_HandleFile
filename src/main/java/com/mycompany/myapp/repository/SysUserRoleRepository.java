package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {
    SysUserRole findSysUserRoleByUserId(@Param("userId") Long userId);
}
