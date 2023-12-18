package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SysModuleActionService;
import com.mycompany.myapp.service.dto.SysActionDTO;
import com.mycompany.myapp.service.dto.SysModuleActionDTO;
import com.mycompany.myapp.service.form.SysModuleActionRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/moduleAction")
public class SysModuleActionResource {

    private final SysModuleActionService sysModuleActionService;

    public SysModuleActionResource(SysModuleActionService sysModuleActionService) {
        this.sysModuleActionService = sysModuleActionService;
    }

    @GetMapping("/getByModuleId/{moduleId}")
    public ResponseEntity<List<SysModuleActionDTO>> getByModuleId(@PathVariable Long moduleId) {
        List<SysModuleActionDTO> rs = sysModuleActionService.getByModuleId(moduleId);
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody SysModuleActionRequest request) {
        Long moduleId = request.getModuleId();
        List<Long> actionIds = request.getActionIds();
        // delete all by moduleId
        sysModuleActionService.deleteAllByModuleId(moduleId);
        sysModuleActionService.saveAll(actionIds, moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getActionByModuleCode/{code}")
    public ResponseEntity<List<SysActionDTO>> getActionByModuleId(@PathVariable String code) {
        List<SysActionDTO> rs = sysModuleActionService.getActionByModuleCode(code);
        return ResponseEntity.ok().body(rs);
    }
}
