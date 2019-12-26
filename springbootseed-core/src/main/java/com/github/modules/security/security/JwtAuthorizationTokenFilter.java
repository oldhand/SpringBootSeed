package com.github.modules.security.security;

import com.github.modules.config.GlobalConfig;
import com.github.modules.rsa.config.SecretKeyConfig;
import com.github.modules.security.service.OnlineUserService;
import com.github.modules.utils.RSAUtil;
import com.github.utils.StringUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import com.github.modules.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {


    @Value("${jwt.online}")
    private String onlineKey;

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisTemplate redisTemplate;

    public JwtAuthorizationTokenFilter(@Qualifier("jwtApplicationDetailsService") UserDetailsService userDetailsService,OnlineUserService onlineUserService, JwtTokenUtil jwtTokenUtil, RedisTemplate redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisTemplate = redisTemplate;
        this.onlineUserService = onlineUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String authToken = jwtTokenUtil.getAccessToken(request);
        String Token = jwtTokenUtil.getToken(request);
        String ip = StringUtils.getIp(request);
//        System.out.println("-----authToken---["+authToken+"]----Token---["+Token+"]---IP---["+ip+"]-------");
        if (GlobalConfig.isDev() && authToken != null && authToken.equals("anonymous") && StringUtils.isSafeIp(ip)) {
            TestingAuthenticationToken authentication = new TestingAuthenticationToken("anonymous", null);
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else {
            if (GlobalConfig.isDev() && Token != null && Token.equals("anonymous") && StringUtils.isSafeIp(ip)) {
                TestingAuthenticationToken authentication = new TestingAuthenticationToken("anonymous", null);
                authentication.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                final String uri = request.getRequestURI();
                String privatekey = "";
                if (uri.equals("/auth/credential")) {
                    privatekey = RSAUtil.loadKey(secretKeyConfig.getPrivateKey());
                }
                else if (authToken != null && !authToken.isEmpty()){
                    privatekey = onlineUserService.getPrivateKey(authToken);
                }
                if (!privatekey.isEmpty() && jwtTokenUtil.verify(request, privatekey)) {
                    OnlineUser onlineUser = null;
                    try {
                        onlineUser = (OnlineUser)redisTemplate.opsForValue().get(onlineKey + authToken);
                    } catch (ExpiredJwtException e) {
                        log.error(e.getMessage());
                    }
                    if (onlineUser != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // It is not compelling necessary to load the use details from the database. You could also store the information
                        // in the token and read it from it. It's up to you ;)
                        JwtAuthentication userDetails = (JwtAuthentication)this.userDetailsService.loadUserByUsername(onlineUser.getUserName());
                        // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                        // the database compellingly. Again it's up to you ;)
                        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}
