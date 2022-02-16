package com.socialNetwork.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class SocialNetworkExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserFriendlyException.class)
    public ResponseEntity<Map<String, String>> handleUserFriendlyExceptions(UserFriendlyException exception){
        return ResponseEntity.badRequest().body(Map.of("msg", exception.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, String>>handleExpiredJwtException(ExpiredJwtException exception){
        return ResponseEntity.badRequest().body(Map.of("msg", "Token expired"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleDeveloperExceptions(Exception exception){
        log.error("{}: {}", LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.badRequest().body(Map.of("msg", "Error"));
    }
}
