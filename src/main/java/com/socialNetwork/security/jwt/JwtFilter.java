package com.socialNetwork.security.jwt;

import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.headerFiledName}")
    private String headerFiledName;

    public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService){
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest)servletRequest);
        if(token != null && jwtProvider.validateToken(token)){
            String email = jwtProvider.getLoginFromToken(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(headerFiledName);
        log.info("Token from header: {}", token);
        return token;
    }

}