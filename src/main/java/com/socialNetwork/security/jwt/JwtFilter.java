package com.socialNetwork.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialNetwork.dto.response.ErrorResponse;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends GenericFilterBean {

    private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.headerFiledName}")
    private String headerFiledName;

    public JwtFilter(JwtProvider jwtProvider, CustomUserDetailsService customUserDetailsService){
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest((HttpServletRequest)servletRequest);
        try {
            if(token != null && jwtProvider.validateToken(token)){
                String login = jwtProvider.getLoginFromToken(token);
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(login);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (UserFriendlyException e) {
            ErrorResponse errorResponse = new ErrorResponse(e);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(convertObjectToJson(errorResponse));
        }
    }

    private String getTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(headerFiledName);
        logger.info("Token from header: {}", token);
        return token;
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}