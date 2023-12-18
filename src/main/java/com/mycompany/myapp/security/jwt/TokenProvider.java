package com.mycompany.myapp.security.jwt;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.management.SecurityMetersService;
import com.mycompany.myapp.security.CustomGrantedAuthority;
import com.mycompany.myapp.service.RedisSevice;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import tech.jhipster.config.JHipsterProperties;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private static final String INVALID_JWT_TOKEN = "Invalid JWT token.";

    private final Key key;

    private final JwtParser jwtParser;

    private final long tokenValidityInMilliseconds;

    private final long tokenValidityInMillisecondsForRememberMe;

    private final SecurityMetersService securityMetersService;
    private final RedisSevice redisService;

    public TokenProvider(JHipsterProperties jHipsterProperties, SecurityMetersService securityMetersService, RedisSevice redisService) {
        byte[] keyBytes;
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        if (!ObjectUtils.isEmpty(secret)) {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            log.warn(
                "Warning: the JWT key used is not Base64-encoded. " +
                "We recommend using the `jhipster.security.authentication.jwt.base64-secret` key for optimum security."
            );
            secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityInMilliseconds = 1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
            1000 * jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSecondsForRememberMe();

        this.securityMetersService = securityMetersService;
        this.redisService = redisService;
    }

    public String createToken(Authentication authentication, boolean rememberMe, Long userId) {
        //        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        String authorities =
            ((List<CustomGrantedAuthority>) authentication.getAuthorities()).get(0)
                .getPermissions()
                .stream()
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + 1000 * Constants.TIME_EXPIRE_TOKEN + 1000 * Constants.TIME_REMEMBER);
        } else {
            validity = new Date(now + 1000 * Constants.TIME_EXPIRE_TOKEN);
        }

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .claim("userId", userId)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        List<String> lstPermission = new ArrayList<>();
        if (claims.containsKey(AUTHORITIES_KEY)) {
            lstPermission = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).collect(Collectors.toList());
        }
        CustomGrantedAuthority grantedAuthority = new CustomGrantedAuthority();
        grantedAuthority.setPermissions(lstPermission);
        grantedAuthority.setRole("ROLE_USER");
        //        Collection<? extends GrantedAuthority> authorities = Arrays
        //            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        //            .filter(auth -> !auth.trim().isEmpty())
        //            .map(SimpleGrantedAuthority::new)
        //            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", Arrays.asList(grantedAuthority));

        return new UsernamePasswordAuthenticationToken(principal, token, Arrays.asList(grantedAuthority));
    }

    public boolean validateToken(String authToken) {
        try {
            jwtParser.parseClaimsJws(authToken);
            if (!redisService.isExist(authToken)) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            this.securityMetersService.trackTokenExpired();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            this.securityMetersService.trackTokenUnsupported();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (MalformedJwtException e) {
            this.securityMetersService.trackTokenMalformed();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (SignatureException e) {
            this.securityMetersService.trackTokenInvalidSignature();

            log.trace(INVALID_JWT_TOKEN, e);
        } catch (IllegalArgumentException e) { // TODO: should we let it bubble (no catch), to avoid defensive programming and follow the fail-fast principle?
            log.error("Token validation error {}", e.getMessage());
        }

        return false;
    }
}
