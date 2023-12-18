package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysRoleModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SysRoleModuleRepository extends JpaRepository<SysRoleModule, Long> {
    void deleteAllByRoleId(@Param("roleId") Long roleId);
}
