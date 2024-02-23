package com.mycompany.myapp.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.mycompany.myapp.config.AmazonClient;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.SysUser;
import com.mycompany.myapp.service.FileAttachmentService;
import com.mycompany.myapp.service.SysUserService;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.service.form.SysUserSearch;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.myapp.utils.WordToPdfConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin/user")
public class SysUserResource {

    private final SysUserService sysUserService;
    private final Environment environment;
    private final FileAttachmentService fileAttachmentService;
    private final AmazonClient amazonClient;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.passwordDefault}")
    private String passwordDefault;

    public SysUserResource(SysUserService sysUserService, Environment environment, FileAttachmentService fileAttachmentService, AmazonClient amazonClient, PasswordEncoder passwordEncoder) {
        this.sysUserService = sysUserService;
        this.environment = environment;
        this.fileAttachmentService = fileAttachmentService;
        this.amazonClient = amazonClient;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/doSearch")
    @PreAuthorize("hasPermission('MANAGE_USER', 'SEARCH')")
    public ResponseEntity<List<SysUserDTO>> doSearch(@RequestBody SysUserSearch sysUserSearch, Pageable pageable) {
        Page<SysUserDTO> page = sysUserService.doSearch(sysUserSearch.getKeyword(), sysUserSearch.getStatus(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("insert")
    public ResponseEntity<SysUserDTO> insert(
        @RequestParam(value = "userDTOString") String userDTOString,
        @RequestParam(value = "imgAsset", required = false) MultipartFile imgAsset
    ) throws Exception {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(
                Instant.class,
                (JsonDeserializer<Instant>) (json, type, ctx) -> Instant.parse(json.getAsJsonPrimitive().getAsString())
            )
            .create();
        SysUserDTO sysUserDTO = gson.fromJson(userDTOString, SysUserDTO.class);
//        Long fileAttachmentId = FileUtil.uploadAvatar(imgAsset, result.getId(), Constants.FILE_TYPE.AVATAR, environment, fileAttachmentService);
        if (imgAsset != null) {
            String imgUrl = amazonClient.uploadFileWithPath(imgAsset, Constants.BUCKET_PATH.AVATAR);
            sysUserDTO.setImageUrl(imgUrl);
        }
        SysUserDTO result = sysUserService.save(sysUserDTO);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("update")
    public ResponseEntity<SysUserDTO> update(
        @RequestParam(value = "userDTOString") String userDTOString,
        @RequestParam(value = "imgAsset", required = false) MultipartFile imgAsset,
        @RequestParam(value = "imgAssetDel", required = false) String imgAssetDel
    ) throws Exception {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class,
                (JsonDeserializer<Instant>) (json, type, ctx) -> Instant.parse(json.getAsJsonPrimitive().getAsString()))
            .create();
//        Gson gson = new GsonBuilder()
//            .registerTypeAdapter(
//                Instant.class,
//                (JsonDeserializer<Instant>) (json, type, ctx) -> Instant.from(LocalDateTime.ofInstant(Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong()), ZoneId.systemDefault()))
//            )
//            .create();
        SysUserDTO sysUserDTOUpdate = gson.fromJson(userDTOString, SysUserDTO.class);
        SysUserDTO sysUserDTO = sysUserService.findById(sysUserDTOUpdate.getId());
//        if (imgAsset != null) {
//            Long fileAttachmentId = FileUtil.uploadAvatar(imgAsset, sysUserDTOUpdate.getId(), Constants.FILE_TYPE.AVATAR, environment, fileAttachmentService);
//        }
        if (imgAsset != null) {
            //delete old avatar in minio
            if (sysUserDTO.getImageUrl() != null) {
                amazonClient.deleteFileS3(sysUserDTO.getImageUrl());
            }
            // upload new avatar
            String imgUrl = amazonClient.uploadFileWithPath(imgAsset, Constants.BUCKET_PATH.AVATAR);
            sysUserDTOUpdate.setImageUrl(imgUrl);
        }
//        if (imgAssetDel != null) {
//            fileAttachmentService.deleteById(Long.valueOf(imgAssetDel));
//        }
        sysUserDTO = sysUserService.update(sysUserDTOUpdate);
        return ResponseEntity.ok(sysUserDTO);
    }
    @GetMapping("viewDetail/{id}")
    public ResponseEntity<SysUserDTO> viewDetail(@PathVariable("id") Long id) {
        SysUserDTO result = sysUserService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exportWithTemplateFromS3")
    public void exportWithTemplateFromS3(HttpServletResponse response) throws IOException {
        sysUserService.export(response);
    }


    @PostMapping("import-user")
    public ResponseEntity<Void> importUser(@RequestParam(value = "fileImport", required = false) MultipartFile fileImport) throws IOException, ParseException {
        sysUserService.importUser(fileImport);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exportWithTemplateFromResource")
    public void exportWithTemplateFromResource(HttpServletResponse response) throws IOException {
        sysUserService.exportExcel();
    }

    @GetMapping("/exportUserInfo/{id}")
    public void exportUserInfo(HttpServletResponse response, @PathVariable("id") Long id) throws IOException {
        sysUserService.exportUserInfo(id);
    }

    @GetMapping("/convert")
    public void convert() throws IOException {
        WordToPdfConverter.convert();
    }
}
