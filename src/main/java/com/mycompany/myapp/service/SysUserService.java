package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.ChangePwDTO;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.web.rest.vm.LoginVM;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface SysUserService {
    void validateCaptcha(LoginVM loginVM);
    SysUserDTO findByEmail(String email);

    SysUserDTO findByUserName(String userName);

    String createHashKey(SysUserDTO sysUserDTO) throws UnsupportedEncodingException;

    void resetPassword(ChangePwDTO changePwdDTO);

    Page<SysUserDTO> doSearch(String keyword, Integer status, Pageable pageable);

    SysUserDTO save(SysUserDTO sysUserDTO) throws Exception;

    SysUserDTO update(SysUserDTO sysUserDTO) throws Exception;

    SysUserDTO findById(Long id);

    List<SysUserDTO> findAll();

    void importUser(MultipartFile fileImport) throws IOException, ParseException;

    void export(HttpServletResponse response) throws IOException;

    void exportExcel();

    void exportUserInfo(Long id);
}
