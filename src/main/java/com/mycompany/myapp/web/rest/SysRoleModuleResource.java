package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SysRoleModule;
import com.mycompany.myapp.service.SysRoleModuleService;
import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/roleModule")
public class SysRoleModuleResource {

    private final SysRoleModuleService sysRoleModuleService;

    public SysRoleModuleResource(SysRoleModuleService sysRoleModuleService) {
        this.sysRoleModuleService = sysRoleModuleService;
    }

    @PostMapping("/update/{roleId}")
    public ResponseEntity<Void> update(@PathVariable Long roleId, @RequestBody List<SysRoleModuleDTO> req) {
        this.sysRoleModuleService.deleteAllByRoleId(roleId);
        for (SysRoleModuleDTO item : req) {
            this.sysRoleModuleService.save(item);
        }
        return ResponseEntity.noContent().build();
    }
}
