package com.socialNetwork.exceptions;

import com.socialNetwork.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class SocialNetworkExceptionHandler extends ResponseEntityExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(SocialNetworkExceptionHandler.class);

    @ExceptionHandler(UserFriendlyException.class)
    public ResponseEntity<ErrorResponse> handleUserFriendlyExceptions(UserFriendlyException exception){
        logger.warn(exception.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(exception));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse>handleExpiredJwtException(ExpiredJwtException exception){
        return ResponseEntity.badRequest().body(new ErrorResponse( "Token expired"));
    }

    @ExceptionHandler(DeveloperException.class)
    public ResponseEntity<ErrorResponse> handleDeveloperException(DeveloperException exception) {
        logger.error("{}: {}", exception.getPlaceOfCode(), exception.getInfo());
        return ResponseEntity.internalServerError().body(new ErrorResponse(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleDeveloperExceptions(Exception exception){
        logger.error("{}", exception.getMessage());
        return ResponseEntity.internalServerError().body(new ErrorResponse( "Error"));
    }
}
