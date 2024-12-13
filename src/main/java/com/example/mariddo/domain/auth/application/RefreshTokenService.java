package com.example.mariddo.domain.auth.application;

import com.example.mariddo.domain.auth.dao.RefreshTokenRepository;
import com.example.mariddo.domain.auth.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    // refresh token이 유효하다면, DB에서 검색 -> 전달
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    }
}
