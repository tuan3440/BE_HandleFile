package com.mycompany.myapp.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.constant.ResponseCode;
import com.mycompany.myapp.security.CustomGrantedAuthority;
import com.mycompany.myapp.security.jwt.JWTFilter;
import com.mycompany.myapp.security.jwt.TokenProvider;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.captcha.CommonUtil;
import com.mycompany.myapp.service.dto.ChangePwDTO;
import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import com.mycompany.myapp.service.dto.SysUserDTO;
import com.mycompany.myapp.service.model.BaseRes;
import com.mycompany.myapp.utils.DataUtil;
import com.mycompany.myapp.web.rest.errors.CustomException;
import com.mycompany.myapp.web.rest.vm.LoginVM;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.config.JHipsterProperties;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisSevice redisService;
    private final SysUserService sysUserService;
    public static final String AUTHORIZATION_HEADER = "authorization";
    private final JwtParser jwtParser;
    private final Key key;
    private final OTPService otpService;
    private final NotifyService notifyService;
    private final SysRoleModuleService sysRoleModuleService;

    public UserJWTController(
        TokenProvider tokenProvider,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        RedisSevice redisSevice,
        SysUserService sysUserService,
        JHipsterProperties jHipsterProperties,
        OTPService otpService,
        NotifyService notifyService,
        SysModuleActionService sysModuleActionService,
        SysRoleModuleService sysRoleModuleService
    ) {
        this.otpService = otpService;
        this.notifyService = notifyService;
        this.sysRoleModuleService = sysRoleModuleService;
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisService = redisSevice;
        this.sysUserService = sysUserService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        SysUserDTO sysUserDTO = sysUserService.findByUserName(loginVM.getUsername());
        List<SysRoleModuleDTO> list = sysRoleModuleService.getRoleModuleByUserId(sysUserDTO.getId());
        List<String> permissionList = list
            .stream()
            .map(m -> {
                if (!DataUtil.isNullOrEmpty(m.getActionCode())) {
                    return m.getModuleCode() + "_" + m.getActionCode();
                } else {
                    return m.getModuleCode() + "_*";
                }
            })
            .collect(Collectors.toList());
        CustomGrantedAuthority grantedAuthority = new CustomGrantedAuthority();
        grantedAuthority.setRole("USER");
        grantedAuthority.setPermissions(permissionList);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword(),
            Arrays.asList(grantedAuthority)
        );

        if (loginVM.getCaptcha() != null) {
            this.sysUserService.validateCaptcha(loginVM);
        }

        //        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken, loginVM.isRememberMe(), sysUserDTO.getId());
        if (loginVM.isRememberMe()) {
            redisService.setKeyValueWithTimeOut(
                jwt,
                loginVM.getUsername(),
                (Constants.TIME_EXPIRE_TOKEN + Constants.TIME_REMEMBER) * 1000,
                TimeUnit.MILLISECONDS
            );
        } else {
            redisService.setKeyValueWithTimeOut(jwt, loginVM.getUsername(), Constants.TIME_EXPIRE_TOKEN * 1000, TimeUnit.MILLISECONDS);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String token = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        redisService.delKey(token);
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @GetMapping("/get-captcha-code")
    public ResponseEntity<?> getCaptchaCode() {
        String captchaStr = CommonUtil.generateCaptchaTextMethod2(6);
        String encryptCaptcha = CommonUtil.encrypt(captchaStr);
        this.redisService.setKeyValueWithTimeOut(captchaStr, encryptCaptcha, 5L, TimeUnit.MINUTES);
        return ResponseEntity.status(HttpStatus.OK).body(encryptCaptcha);
    }

    @PostMapping("/get-captcha-img")
    public ResponseEntity<Map<String, String>> generateImageCaptcha(@RequestBody Map<String, String> body) {
        String captchaStr = CommonUtil.decrypt(body.get("key"));
        String imgCaptcha = null;
        Map<String, String> result = new HashMap<>();
        try {
            int width = 130;
            int height = 37;
            Color bg = new Color(202, 215, 219);
            Color fg = new Color(10, 82, 138);
            Font font = new Font("Helvetica", Font.BOLD, 24);
            BufferedImage cpimg = new BufferedImage(width, height, BufferedImage.OPAQUE);
            Graphics g = cpimg.createGraphics();
            g.setFont(font);
            g.setColor(bg);
            g.fillRect(0, 0, width, height);
            g.setColor(fg);
            g.drawString(captchaStr, 10, 25);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(cpimg, "jpeg", os);
            imgCaptcha = "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("imgCaptcha", imgCaptcha);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @PostMapping("/getOtp")
    public ResponseEntity<?> generateOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String captcha = body.get("captcha");
        String encryptCaptcha = redisService.getValue(captcha);
        String captchaSecret = CommonUtil.decrypt(encryptCaptcha);
        if (!captchaSecret.equals(captcha)) {
            throw new RuntimeException();
        }
        SysUserDTO sysUserDTO = sysUserService.findByEmail(email);
        if (sysUserDTO == null) {
            throw new RuntimeException();
        }
        String otpCode = this.otpService.createCodeByEmail(email);
        this.notifyService.sendCodeForgetPwd(sysUserDTO, otpCode);
        redisService.delKey(captcha);
        Map<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("expiredTime", 300000L);
        return new ResponseEntity<>(BaseRes.buildSuccessfulRes(result), HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> authenticateOTP(@RequestBody Map<String, String> body) throws UnsupportedEncodingException {
        String email = body.get("email");
        String otpCode = body.get("code");
        SysUserDTO sysUserDTO = sysUserService.findByEmail(email);
        if (sysUserDTO == null) {
            throw new RuntimeException();
        }
        Map<String, Object> otpCheck = otpService.verifyOtp(email, otpCode);
        if (otpCheck.get("isSuccess").equals(false)) {
            throw new CustomException(ResponseCode.CODE.AUTHENTICATE_FAILED, ResponseCode.MSG.AUTHENTICATE_FAILED, "error.otp.invalid");
        }
        String mess;
        mess = sysUserService.createHashKey(sysUserDTO);
        Map<String, Object> message = new HashMap<>();
        message.put("key", mess);
        return new ResponseEntity<>(BaseRes.buildSuccessfulRes(message), HttpStatus.OK);
    }

    @PostMapping("/change-password-forget")
    public ResponseEntity<?> changePassForget(@RequestBody Map<String, String> body) {
        ObjectMapper mapper = new ObjectMapper();
        ChangePwDTO changePwdDTO;
        changePwdDTO = mapper.convertValue(body, ChangePwDTO.class);
        sysUserService.resetPassword(changePwdDTO);
        return new ResponseEntity<>(BaseRes.buildSuccessfulRes(), HttpStatus.OK);
    }
}
