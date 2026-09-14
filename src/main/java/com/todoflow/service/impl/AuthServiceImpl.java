package com.todoflow.service.impl;

import com.todoflow.dto.auth.LoginRequestDto;
import com.todoflow.dto.auth.RefreshTokenRequestDto;
import com.todoflow.dto.auth.TokenPairDto;
import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;
import com.todoflow.entity.RefreshToken;
import com.todoflow.entity.User;
import com.todoflow.enums.Role;
import com.todoflow.exception.BadRequestException;
import com.todoflow.exception.NotFoundException;
import com.todoflow.mapper.UserMapper;
import com.todoflow.repository.RefreshTokenRepository;
import com.todoflow.security.JwtService;
import com.todoflow.service.IAuthService;
import com.todoflow.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserResponseDto register(UserCreateRequestDto request) {

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setRole(Role.USER);
        user.setEnabled(true);

        User savedUser = userService.save(user);

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public TokenPairDto login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userService.findByUsername(request.username())
                .orElseThrow(() ->
                        new NotFoundException("Kullanıcı bulunamadı")
                );

        String accessToken =
                jwtService.generateAccessToken(user.getUsername());

        String refreshToken =
                jwtService.generateRefreshToken(user.getUsername());

        RefreshToken refreshTokenEntity = new RefreshToken(
                refreshToken,
                user,
                Instant.now().plusSeconds(7 * 24 * 60 * 60)
        );

        refreshTokenRepository.save(refreshTokenEntity);

        return new TokenPairDto(
                accessToken,
                refreshToken
        );

    }
    @Override
    public TokenPairDto refresh(RefreshTokenRequestDto request) {

        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Geçersiz refresh token");
        }

        RefreshToken refreshTokenEntity =
                refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() ->
                                new NotFoundException("Refresh token bulunamadı")
                        );

        if (refreshTokenEntity.isRevoked()) {
            throw new BadRequestException("Refresh token iptal edilmiş");
        }

        if (refreshTokenEntity.isUsed()) {
            throw new BadRequestException("Refresh token daha önce kullanılmış");
        }

        if (refreshTokenEntity.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Geçersiz refresh token");
        }

        User user = refreshTokenEntity.getUser();

        // Eski refresh token artık kullanılamaz
        refreshTokenEntity.setUsed(true);
        refreshTokenRepository.save(refreshTokenEntity);

        // Yeni tokenlar oluştur
        String newAccessToken =
                jwtService.generateAccessToken(user.getUsername());

        String newRefreshToken =
                jwtService.generateRefreshToken(user.getUsername());

        // Yeni refresh tokenı DB'ye kaydet
        RefreshToken newRefreshTokenEntity =
                new RefreshToken(
                        newRefreshToken,
                        user,
                        Instant.now().plusSeconds(7 * 24 * 60 * 60)
                );

        refreshTokenRepository.save(newRefreshTokenEntity);

        return new TokenPairDto(
                newAccessToken,
                newRefreshToken
        );
    }
    @Override
    public void logout(RefreshTokenRequestDto request) {

        String refreshToken = request.refreshToken();

        RefreshToken refreshTokenEntity =
                refreshTokenRepository.findByToken(refreshToken)
                        .orElseThrow(() ->
                                new NotFoundException("Refresh token bulunamadı")
                        );

        refreshTokenEntity.setRevoked(true);

        refreshTokenRepository.save(refreshTokenEntity);
    }
}