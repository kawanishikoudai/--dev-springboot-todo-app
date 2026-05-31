package com.todo.api.controller;

import com.todo.api.entity.User;
import com.todo.api.service.AuthService;
import com.todo.api.service.JwtService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/send-otp")
    public ResponseEntity<Void> sendOtp(@RequestBody Map<String, String> body) {
        authService.sendOtp(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(
        @RequestBody Map<String, String> body
    ) {
        User user = authService.verifyOtp(body.get("email"), body.get("code"));
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
