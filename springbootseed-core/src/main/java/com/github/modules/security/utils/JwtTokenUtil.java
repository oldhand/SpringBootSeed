package com.github.modules.security.utils;

import com.github.modules.utils.MD5Util;
import com.github.modules.utils.RSAUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import com.github.modules.security.security.JwtAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.header}")
    private String tokenHeader;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean verify(HttpServletRequest request,String privatekey){
        if (privatekey.isEmpty()) return false;
        String uri = request.getRequestURI();
        final String query = request.getQueryString();
        final String token = getToken(request);
        final String timestamp = getTimestamp(request);
        if (query != null && !query.isEmpty()) {
            uri += "?" + URLDecoder.decode(query);
        }
//        System.out.println("-----------uri------"+uri+"--------------"+token+"------"+timestamp+"---------------");
//        System.out.println("-----------privatekey---------"+privatekey+"---------------------");
        if (!token.isEmpty() && !timestamp.isEmpty()  && !token.equals("anonymous") && !timestamp.equals("0")) {
            try {
                final String decrypt_token = RSAUtil.decrypt(token, privatekey);
                String md5token = MD5Util.get(uri + timestamp);
                if (md5token.equals(decrypt_token)) return true;
            }
            catch (Exception e) {

            }
        }
        return false;
    }

    public String getToken(HttpServletRequest request){
        final String requestHeader = request.getHeader("token");
        if (requestHeader != null) {
            return requestHeader;
        }
        return "";
    }

    public String getTimestamp(HttpServletRequest request){
        final String requestHeader = request.getHeader("timestamp");
        if (requestHeader != null) {
            return requestHeader;
        }
        return "";
    }

    public String getAccessToken(HttpServletRequest request){
        final String requestHeader = request.getHeader(tokenHeader);
        if (requestHeader != null) {
            return requestHeader;
        }
        return null;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtAuthentication user = (JwtAuthentication) userDetails;
        final Date created = getIssuedAtDateFromToken(token);
//        final Date expiration = getExpirationDateFromToken(token);
//        如果token存在，且token创建日期 > 最后修改密码的日期 则代表token有效
        return (!isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration);
    }
}

