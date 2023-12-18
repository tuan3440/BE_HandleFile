package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.service.SysActionService;
import com.mycompany.myapp.service.dto.SysActionDTO;
import com.mycompany.myapp.service.form.SysActionSearch;
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
@RequestMapping("/api/admin/action")
public class SysActionResource {

    private final SysActionService sysActionService;

    public SysActionResource(SysActionService sysActionService) {
        this.sysActionService = sysActionService;
    }

    @PostMapping("/doSearch")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'SEARCH')")
    public ResponseEntity<List<SysActionDTO>> doSearch(@RequestBody SysActionSearch sysActionSearch, Pageable pageable) {
        Page<SysActionDTO> page = sysActionService.doSearch(sysActionSearch.getKeyword(), sysActionSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'CREATE')")
    public ResponseEntity<SysActionDTO> create(@RequestBody SysActionDTO sysActionDTO) {
        if (sysActionDTO.getId() != null) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "action is exist");
        }
        SysActionDTO rs = sysActionService.save(sysActionDTO);
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("/update")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'UPDATE')")
    public ResponseEntity<SysActionDTO> update(@RequestBody SysActionDTO sysActionDTO) {
        if (sysActionDTO.getId() == null) {
            throw new CustomException(ResponseCode.CODE.EXISTED, ResponseCode.MSG.EXISTED, "action is exist");
        }
        SysActionDTO rs = sysActionService.update(sysActionDTO);
        return ResponseEntity.ok().body(rs);
    }

    @PostMapping("delete/{id}")
    @PreAuthorize("hasPermission('MANAGE_ACTION', 'DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sysActionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
