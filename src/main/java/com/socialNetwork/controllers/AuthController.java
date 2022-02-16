package com.socialNetwork.controllers;

import com.socialNetwork.dto.UserInfo;
import com.socialNetwork.exceptions.UserFriendlyException;
import com.socialNetwork.security.jwt.JwtProvider;
import com.socialNetwork.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(AuthService authService, JwtProvider jwtProvider){
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> registration(@RequestBody UserInfo userInfo) throws UserFriendlyException {
        authService.createUser(userInfo);
        return ResponseEntity.ok(Map.of("msg", "Registered successfully"));
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam("login") String login,
                                                     @RequestParam("password") String password) throws UserFriendlyException {
        authService.login(login, password);
        String token = jwtProvider.generateToken(login);
        return ResponseEntity.ok(Map.of("msg","Login successfully",
                "token", token));
    }

}
