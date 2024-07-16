package com.alurachallenges.forohub.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import com.alurachallenges.forohub.dto.user.RegisterUserData;
import com.alurachallenges.forohub.models.User;
import com.alurachallenges.forohub.services.UserService;

@RestController
@RequestMapping("/register")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserData userData) {
        try {
            if (userService.existsByUsername(userData.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "El nombre de usuario ya está en uso"));
            }

            User newUser = new User();
            newUser.setUsername(userData.getUsername());
            newUser.setPassword(passwordEncoder.encode(userData.getPassword()));

            User savedUser = userService.save(newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SuccessResponse(HttpStatus.CREATED.value(), "Usuario registrado exitosamente", savedUser.getUsername()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno del servidor"));
        }
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationExceptions(Exception ex) {
        List<String> errors = new ArrayList<>();
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) ex;
            cve.getConstraintViolations().forEach(violation ->
                    errors.add(formatError(violation.getPropertyPath().toString(), violation.getMessage())));
        } else if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            manve.getBindingResult().getFieldErrors().forEach(error ->
                    errors.add(formatError(error.getField(), error.getDefaultMessage())));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Errores de validación", errors));
    }

    private String formatError(String field, String message) {
        return String.format("%s: %s", field, message);
    }
}

class ErrorResponse {
    private int status;
    private String message;
    private List<String> errors;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

class SuccessResponse {
    private int status;
    private String message;
    private String username;

    public SuccessResponse(int status, String message, String username) {
        this.status = status;
        this.message = message;
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}