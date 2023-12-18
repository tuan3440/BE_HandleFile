package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.service.SysRoleService;
import com.mycompany.myapp.service.dto.SysRoleDTO;
import com.mycompany.myapp.service.form.SysRoleSearch;
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
@RequestMapping("/api/admin/role")
public class SysRoleResource {

    private final SysRoleService sysRoleService;

    public SysRoleResource(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @PostMapping("/doSearch")
    @PreAuthorize("hasPermission('MANAGE_ROLE', 'SEARCH')")
    public ResponseEntity<List<SysRoleDTO>> doSearch(@RequestBody SysRoleSearch sysRoleSearch, Pageable pageable) {
        Page<SysRoleDTO> page = sysRoleService.doSearch(sysRoleSearch.getKeyword(), sysRoleSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasPermission('MANAGE_ROLE', 'CREATE')")
    public ResponseEntity<SysRoleDTO> create(@RequestBody SysRoleDTO sysRoleDTO) {
        if (sysRoleDTO.getId() != null) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "role is exist");
        }
        SysRoleDTO result = sysRoleService.save(sysRoleDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/update")
    @PreAuthorize("hasPermission('MANAGE_ROLE', 'UPDATE')")
    public ResponseEntity<SysRoleDTO> update(@RequestBody SysRoleDTO sysRoleDTO) {
        if (sysRoleDTO.getId() == null) {
            throw new CustomException(ResponseCode.CODE.NOT_EXISTED, ResponseCode.MSG.NOT_EXISTED, "role is not exist");
        }
        SysRoleDTO result = sysRoleService.update(sysRoleDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasPermission('MANAGE_ROLE', 'DELETE')")
    public ResponseEntity<Void> deleteSysRole(@PathVariable Long id) {
        sysRoleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
