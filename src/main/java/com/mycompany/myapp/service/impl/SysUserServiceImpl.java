package com.mycompany.myapp.service.impl;

import com.google.gson.Gson;
import com.mycompany.myapp.config.AmazonClient;
import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.domain.SysUser;
import com.mycompany.myapp.repository.SysUserRepository;
import com.mycompany.myapp.service.RedisSevice;
import com.mycompany.myapp.service.SysUserService;
import com.mycompany.myapp.service.captcha.CommonUtil;
import com.mycompany.myapp.service.dto.ChangePwDTO;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.service.mapper.SysUserMapper;
import com.mycompany.myapp.service.model.RequestPasswordModel;
import com.mycompany.myapp.utils.DataUtil;
import com.mycompany.myapp.utils.StringUtil;
import com.mycompany.myapp.utils.ValidateUtil;
import com.mycompany.myapp.web.rest.errors.CustomException;
import com.mycompany.myapp.web.rest.vm.LoginVM;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.security.RandomUtil;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    private RedisSevice redisSevice;
    private final SysUserRepository sysUserRepository;
    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.passwordDefault}")
    private String passwordDefault;
    private final AmazonClient amazonClient;

    SysUserServiceImpl(
        RedisSevice redisSevice,
        SysUserRepository sysUserRepository,
        SysUserMapper sysUserMapper,
        PasswordEncoder passwordEncoder,
        AmazonClient amazonClient) {
        this.redisSevice = redisSevice;
        this.sysUserRepository = sysUserRepository;
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.amazonClient = amazonClient;
    }

    @Override
    public void validateCaptcha(LoginVM loginVM) {
        String captcha = loginVM.getCaptcha();
        String captchaEncode = CommonUtil.encrypt(captcha);
        String captchaReal = this.redisSevice.getValue(captcha);
        if (!captchaEncode.equals(captchaReal)) {
            throw new RuntimeException();
        }
    }

    @Override
    public SysUserDTO findByEmail(String email) {
        return sysUserRepository.findByEmail(email).map(sysUserMapper::toDto).orElse(null);
    }

    @Override
    public SysUserDTO findByUserName(String userName) {
        return sysUserRepository.findByUserName(userName).map(sysUserMapper::toDto).orElse(null);
    }

    @Override
    public String createHashKey(SysUserDTO sysUserDTO) throws UnsupportedEncodingException {
        String refreshToken = RandomUtil.generateResetKey();
        sysUserDTO.setResetKey(refreshToken);
        sysUserRepository.save(sysUserMapper.toEntity(sysUserDTO));

        RequestPasswordModel requestPasswordModel = new RequestPasswordModel();
        requestPasswordModel.setUserId(sysUserDTO.getId());
        requestPasswordModel.setResetKey(refreshToken);
        requestPasswordModel.setExpiredTime(System.currentTimeMillis() + 30 * 60 * 1000);
        String key = "";
        String encryptRequestPasswordModel = CommonUtil.encrypt(new Gson().toJson(requestPasswordModel));
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(encryptRequestPasswordModel)) {
            key = URLEncoder.encode(encryptRequestPasswordModel, StandardCharsets.UTF_8);
        }
        return key;
    }

    @Override
    public void resetPassword(ChangePwDTO changePwdDTO) {
        if (StringUtil.isBlank(changePwdDTO.getKey())) {
            throw new CustomException(ResponseCode.CODE.INVALID_INPUT_DATA, ResponseCode.MSG.INVALID_INPUT_DATA, "error.key.invalid");
        }
        RequestPasswordModel requestPasswordModel;
        requestPasswordModel =
            new Gson()
                .fromJson(CommonUtil.decrypt(URLDecoder.decode(changePwdDTO.getKey(), StandardCharsets.UTF_8)), RequestPasswordModel.class);
        if (requestPasswordModel.getExpiredTime() < System.currentTimeMillis()) {
            throw new CustomException(ResponseCode.CODE.EXPIRED, ResponseCode.MSG.EXPIRED, "error.key.expired");
        }
        if (StringUtil.isBlank(requestPasswordModel.getResetKey())) {
            throw new CustomException(ResponseCode.CODE.INVALID_INPUT_DATA, ResponseCode.MSG.INVALID_INPUT_DATA, "error.key.invalid");
        }
        SysUser sysUser = sysUserRepository
            .findByIdAndResetKey(requestPasswordModel.getUserId(), requestPasswordModel.getResetKey())
            .orElse(null);
        if (sysUser == null) {
            throw new CustomException(ResponseCode.CODE.INVALID_INPUT_DATA, ResponseCode.MSG.INVALID_INPUT_DATA, "error.key.invalid");
        }
        if (!ValidateUtil.checkResetPass(changePwdDTO.getComPass(), changePwdDTO.getNewPass())) {
            throw new CustomException(
                ResponseCode.CODE.INVALID_INPUT_DATA,
                ResponseCode.MSG.INVALID_INPUT_DATA,
                "error.newPassword.invalid"
            );
        }
        completePasswordForget(sysUser, changePwdDTO.getNewPass());
    }

    @Override
    public Page<SysUserDTO> doSearch(String keyword, Integer status, Pageable pageable) {
        if (StringUtils.isNotEmpty(keyword) && StringUtils.isNotBlank(keyword)) {
            keyword = DataUtil.makeLikeQuery(keyword);
        } else {
            keyword = null;
        }
        Page<SysUser> rs = sysUserRepository.doSearch(keyword, status, pageable);
        List<SysUserDTO> rsDTO = sysUserMapper.toDto(rs.getContent());
        return new PageImpl<>(rsDTO, pageable, rs.getTotalElements());
    }

    @Override
    public SysUserDTO save(SysUserDTO sysUserDTO) throws Exception {
        if (sysUserDTO.getUserName() == null || sysUserDTO.getEmail() == null) {
            throw new Exception();
        }
        if (sysUserRepository.existsSysUserByUserName(sysUserDTO.getUserName())) {
            throw new RuntimeException();
        }
        SysUser sysUser = sysUserMapper.toEntity(sysUserDTO);
        sysUser.setPassword(passwordEncoder.encode(passwordDefault));
        SysUser result = sysUserRepository.save(sysUser);
        return sysUserMapper.toDto(result);
    }

    @Override
    public SysUserDTO update(SysUserDTO sysUserDTO) throws Exception {
        if (sysUserDTO.getUserName() == null || sysUserDTO.getEmail() == null) {
            throw new Exception();
        }
//        if (sysUserRepository.existsSysUserByUserName(sysUserDTO.getUserName())) {
//            throw new RuntimeException();
//        }
        SysUser sysUser = sysUserMapper.toEntity(sysUserDTO);
        SysUser result = sysUserRepository.save(sysUser);
        return sysUserMapper.toDto(result);
    }

    @Override
    public SysUserDTO findById(Long id) {
        Optional<SysUser> result = sysUserRepository.findById(id);
        return sysUserMapper.toDto(result.get());
    }

    @Override
    public List<SysUserDTO> findAll() {
        List<SysUser> sysUsers = sysUserRepository.findAll();
        return sysUserMapper.toDto(sysUsers);
    }

    @Override
    public void importUser(MultipartFile fileImport) throws IOException, ParseException {
        XSSFWorkbook workBook = new XSSFWorkbook(fileImport.getInputStream());
        XSSFSheet sheet = workBook.getSheetAt(0);
        List<SysUser> employeeList = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0); rowIndex++) {
            if (rowIndex == 0 || rowIndex == 1) continue;
            XSSFRow row = sheet.getRow(rowIndex);
            String userName = row.getCell(1).toString();
            String fullName = row.getCell(2).toString();
            Integer gender = (Integer) (row.getCell(3).toString().equals("Nam") ? 1 : 0);
            String email = row.getCell(5).toString();
            String phone = row.getCell(6).toString();
            DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            // you can change format of date
            Date date = formatter.parse(row.getCell(4).toString());
            Timestamp timeStampDate = new Timestamp(date.getTime());
            SysUser sysUser = new SysUser();
            sysUser.setUserName(userName);
            sysUser.setIsActive(1);
            sysUser.setStatus(1);
            sysUser.setFullName(fullName);
            sysUser.setGender(gender);
            sysUser.setEmail(email);
            sysUser.setCellphone(phone);
            sysUser.setPassword(passwordEncoder.encode(passwordDefault));
            sysUser.setDateOfBirth(timeStampDate);
            sysUserRepository.save(sysUser);
        }
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        byte[] bytes = this.amazonClient.getFileFromS3("template_export/user/template_user.xlsx");
        InputStream inputStream = new ByteArrayInputStream(bytes);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        List<SysUser> userDTOList = sysUserRepository.findAll();
        CellStyle style = workbook.createCellStyle();
        int rowNum2 = 2;
        for (SysUser userDTO : userDTOList) {
            Row row2 = sheet.createRow(rowNum2++);
            int columnCount = 0;
            createCell(row2, columnCount++, rowNum2 - 2, style);
            createCell(row2, columnCount++, userDTO.getUserName(), style);
            createCell(row2, columnCount++, userDTO.getFullName(), style);
            if (userDTO.getGender() == 0) {
                createCell(row2, columnCount++, "Nam", style);
            } else {
                createCell(row2, columnCount++, "Nữ", style);
            }
            createCell(row2, columnCount++, userDTO.getDateOfBirth(), style);
            createCell(row2, columnCount++, userDTO.getEmail(), style);
            createCell(row2, columnCount++, userDTO.getCellphone(), style);
        }
//         Set the response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=sample.xlsx");

//         Write the workbook data to the response output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        }
        else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Instant) {
            cell.setCellValue(value.toString());
        } else if (value instanceof Timestamp) {
            Timestamp timestamp = new Timestamp(((Timestamp) value).getTime());

            // Create a SimpleDateFormat to format the Timestamp as a String
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Format the Timestamp as a String
            String formattedTimestamp = dateFormat.format(timestamp);
            cell.setCellValue(formattedTimestamp);
        }
        else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }

    private void completePasswordForget(SysUser sysUser, String newPassword) {
        sysUser.setPassword(passwordEncoder.encode(newPassword));
        sysUser.setResetKey(null);
        sysUserRepository.save(sysUser);
    }
}
