package com.alurachallenges.forohub.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alurachallenges.forohub.dto.JWTDatos;
import com.alurachallenges.forohub.dto.user.AuthUserData;
import com.alurachallenges.forohub.models.User;
import com.alurachallenges.forohub.services.TokenService;

@RestController
@RequestMapping("login")
public class AuthController {

    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authManager, TokenService tokenService) {
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Object> login(@Valid @RequestBody AuthUserData authUserData) {
        try {
            Authentication authToken = new UsernamePasswordAuthenticationToken(authUserData.username(),
                    authUserData.password());

            Authentication authUser = authManager.authenticate(authToken);

            String jwt = tokenService.generateToken((User) authUser.getPrincipal());

            Object successResponse = new Object() {
                public final int status = HttpStatus.OK.value();
                public final String token = jwt;
            };

            return ResponseEntity.ok().body(successResponse);
        } catch (AuthenticationException ex) {
            Object errorResponse = new Object() {
                public final int status = HttpStatus.UNAUTHORIZED.value();
                public final String message = "Credenciales inválidas";
            };
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}