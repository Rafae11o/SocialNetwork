package com.socialNetwork.controllers;

import com.socialNetwork.dto.RegistrationInfo;
import com.socialNetwork.dto.TokenInfo;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.dto.response.SuccessResponseWithData;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.security.jwt.JwtProvider;
import com.socialNetwork.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthService authService, JwtProvider jwtProvider){
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/registration")
    public ResponseEntity<SuccessResponse> registration(@RequestBody RegistrationInfo userInfo) throws UserFriendlyException {
        authService.createUser(userInfo);
        logger.info("Registration finished successfully");
        return ResponseEntity.ok(new SuccessResponse("Registered successfully"));
    }

    @GetMapping("/login")
    public ResponseEntity<SuccessResponseWithData<TokenInfo>> login(@RequestParam("login") String login,
                                                         @RequestParam("password") String password) throws UserFriendlyException {
        logger.info("[login] RequestParam login: {}", login);
        authService.login(login, password);
        logger.info("Login finished successfully");
        logger.info("Generation token");
        TokenInfo tokenInfo = jwtProvider.generateToken(login);
        logger.info("Generated token: {}", tokenInfo.getToken());
        return ResponseEntity.ok(new SuccessResponseWithData<>(tokenInfo));
    }

}
