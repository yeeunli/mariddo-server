package com.example.mariddo.global.security;


import com.example.mariddo.domain.user.domain.User;
import com.example.mariddo.global.exception.CustomException;
import com.example.mariddo.global.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static com.example.mariddo.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey()));

    /** 1. 토큰을 생성하자 **/
    public String generateToken(User user, Duration expTime) {
        Date now = new Date();
        return makeToken(user, new Date(now.getTime()+expTime.toMillis()));
        // JwtProperties에서 가져온 expTime(ms)을 로그인하는 현재 시간에 더해서 유효기간 설정
    }

    private String makeToken(User user, Date expiration) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.issuer())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** 2. 올바른 토큰인지 유효성을 검사하자 **/
    public boolean validateToken(String token) {

        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 메서드에 전달된 토큰을 파싱하고 서명을 검증

            return true; // 토큰이 유효하다면, true 반환

        } catch (ExpiredJwtException e) { // 토큰 만료
            throw new CustomException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) { // 지원하지 않는 토큰
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) { // 잘못된 형식의 토큰
            throw new CustomException(MALFORMED_TOKEN);
        } catch (JwtException e) { // 올바르지 않은 서명
            throw new CustomException(WRONG_SIGNATURE_TOKEN);
        } catch (IllegalArgumentException e) { // 인증 토큰 없음
            throw new CustomException(UNKNOWN_TOKEN);
        }
    }

    /** 3. 토큰에서 필요한 정보를 가져오자 **/
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities); // 사용자 인증
    }
}
