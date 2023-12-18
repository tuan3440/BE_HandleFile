package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.domain.ModuleTree;
import com.mycompany.myapp.service.SysModuleService;
import com.mycompany.myapp.service.dto.SysModuleDTO;
import com.mycompany.myapp.service.form.SysModuleSearch;
import com.mycompany.myapp.web.rest.errors.CustomException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/admin/module")
public class SysModuleResource {

    private final SysModuleService sysModuleService;

    public SysModuleResource(SysModuleService sysModuleService) {
        this.sysModuleService = sysModuleService;
    }

    @PostMapping("/doSearch")
    @PreAuthorize("hasPermission('MANAGE_MODULE', 'SEARCH')")
    public ResponseEntity<List<SysModuleDTO>> doSearch(@RequestBody SysModuleSearch sysModuleSearch, Pageable pageable) {
        Page<SysModuleDTO> page = sysModuleService.doSearch(
            sysModuleSearch.getKeyword(),
            sysModuleSearch.getStatus(),
            sysModuleSearch.getParentId(),
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'CREATE')")
    public ResponseEntity<SysModuleDTO> create(@RequestBody SysModuleDTO sysModuleDTO, Pageable pageable) {
        if (sysModuleDTO.getId() != null) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "module is exist");
        }
        SysModuleDTO result = sysModuleService.save(sysModuleDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/update")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'UPDATE')")
    public ResponseEntity<SysModuleDTO> update(@RequestBody SysModuleDTO sysModuleDTO) {
        if (sysModuleDTO.getId() == null) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "module is not exist");
        }
        SysModuleDTO result = sysModuleService.update(sysModuleDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/viewDetail/{id}")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'SEARCH')")
    public ResponseEntity<SysModuleDTO> findById(@PathVariable Long id) {
        SysModuleDTO result = sysModuleService.findById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'DELETE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        sysModuleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("openTree")
    public ResponseEntity<List<ModuleTree>> viewTree() {
        List<ModuleTree> moduleTreeList = sysModuleService.getTree();
        return ResponseEntity.ok().body(moduleTreeList);
    }

    @PostMapping("getMenu")
    public ResponseEntity<List<SysModuleDTO>> getMenuByUserId(@RequestBody Long userId) {
        List<SysModuleDTO> result = sysModuleService.getMenuByUserId(userId);
        return ResponseEntity.ok().body(result);
    }
}
