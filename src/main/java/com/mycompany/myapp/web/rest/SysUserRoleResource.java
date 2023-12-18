package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SysUserRoleService;
import com.mycompany.myapp.service.dto.SysUserRoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/userRole")
public class SysUserRoleResource {

    private final SysUserRoleService sysUserRoleService;

    public SysUserRoleResource(SysUserRoleService sysUserRoleService) {
        this.sysUserRoleService = sysUserRoleService;
    }

    @GetMapping("/getRoleByUserId/{userId}")
    public ResponseEntity<SysUserRoleDTO> getRoleByUserId(@PathVariable Long userId) {
        SysUserRoleDTO rs = this.sysUserRoleService.getRoleByUserId(userId);
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/insert")
    public ResponseEntity<Void> update(@RequestBody SysUserRoleDTO sysUserRoleDTO) {
        this.sysUserRoleService.update(sysUserRoleDTO.getUserId(), sysUserRoleDTO.getRoleId());
        return ResponseEntity.noContent().build();
    }
}
