package com.example.mariddo.domain.auth.api;

import com.example.mariddo.domain.auth.application.TokenService;
import com.example.mariddo.domain.auth.dto.Request;
import com.example.mariddo.domain.auth.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/api/auth")
    public ResponseEntity<Response> createNewToken(@RequestBody Request request) throws IllegalAccessException {
        String newAccessToken = tokenService.createNewToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(newAccessToken));
    }
}
