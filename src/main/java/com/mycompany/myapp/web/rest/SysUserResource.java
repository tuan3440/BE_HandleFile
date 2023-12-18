package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SysUserService;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.service.form.SysUserSearch;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/admin/user")
public class SysUserResource {

    private final SysUserService sysUserService;

    public SysUserResource(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/doSearch")
    @PreAuthorize("hasPermission('MANAGE_USER', 'SEARCH')")
    public ResponseEntity<List<SysUserDTO>> doSearch(@RequestBody SysUserSearch sysUserSearch, Pageable pageable) {
        Page<SysUserDTO> page = sysUserService.doSearch(sysUserSearch.getKeyword(), sysUserSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
