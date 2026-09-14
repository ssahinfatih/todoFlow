package com.todoflow.service;


import com.todoflow.dto.auth.LoginRequestDto;
import com.todoflow.dto.auth.RefreshTokenRequestDto;
import com.todoflow.dto.auth.TokenPairDto;
import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;
import com.todoflow.entity.RefreshToken;
import com.todoflow.entity.User;
import com.todoflow.enums.Role;
import com.todoflow.exception.BadRequestException;
import com.todoflow.mapper.UserMapper;
import com.todoflow.security.JwtService;
import com.todoflow.repository.RefreshTokenRepository;
import com.todoflow.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock// sahte nesne verir.
    private IUserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                userService,
                userMapper,
                passwordEncoder,
                authenticationManager,
                jwtService,
                refreshTokenRepository
        );
    }

    @Test
    void register_kullanici_basariyla_kaydeder() {

        // Test verisi
        UserCreateRequestDto request =
                new UserCreateRequestDto(
                        "fatih",
                        "fatih@test.com",
                        "123456"
                );

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "123456",
                        Role.USER
                );

        User savedUser = user;

        UserResponseDto response =
                new UserResponseDto(
                        1L,
                        "fatih",
                        "fatih@test.com",
                        Role.USER,
                        true,
                        null,
                        null
                );

        // Mock davranışları
        when(userMapper.toEntity(request))
                .thenReturn(user);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encoded-password");

        when(userService.save(user))
                .thenReturn(savedUser);

        when(userMapper.toResponseDto(savedUser))
                .thenReturn(response);

        // Test edilen metod
        UserResponseDto result =
                authService.register(request);

        // Sonuç kontrolleri
        assertNotNull(result);
        assertEquals("fatih", result.username());
        assertEquals("fatih@test.com", result.email());
        assertEquals(Role.USER, result.role());
        assertTrue(result.enabled());

        // Davranış kontrolleri
        verify(userMapper).toEntity(request);
        verify(passwordEncoder).encode("123456");
        verify(userService).save(user);
        verify(userMapper).toResponseDto(savedUser);
    }
    @Test
    void login_basariyla_token_dondurur() {

        LoginRequestDto request =
                new LoginRequestDto(
                        "fatih",
                        "123456"
                );

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "encoded-password",
                        Role.USER
                );

        TokenPairDto tokenPair =
                new TokenPairDto(
                        "access-token",
                        "refresh-token"
                );

        when(userService.findByUsername("fatih"))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken("fatih"))
                .thenReturn("access-token");

        when(jwtService.generateRefreshToken("fatih"))
                .thenReturn("refresh-token");

        TokenPairDto result =
                authService.login(request);

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(refreshTokenRepository)
                .save(any(RefreshToken.class));
    }
    @Test
    void refresh_gecerli_refresh_token_ile_yeni_tokenlar_uretmelidir() {

        String oldRefreshToken = "old-refresh-token";

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "password",
                        Role.USER
                );

        RefreshToken refreshToken =
                new RefreshToken(
                        oldRefreshToken,
                        user,
                        Instant.now().plusSeconds(3600)
                );

        when(jwtService.isRefreshToken(oldRefreshToken))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken(oldRefreshToken))
                .thenReturn(Optional.of(refreshToken));

        when(jwtService.generateAccessToken("fatih"))
                .thenReturn("new-access-token");

        when(jwtService.generateRefreshToken("fatih"))
                .thenReturn("new-refresh-token");

        TokenPairDto result =
                authService.refresh(
                        new RefreshTokenRequestDto(oldRefreshToken)
                );

        assertEquals(
                "new-access-token",
                result.accessToken()
        );

        assertEquals(
                "new-refresh-token",
                result.refreshToken()
        );

        assertTrue(refreshToken.isUsed());

        verify(refreshTokenRepository, times(2))
                .save(any(RefreshToken.class));
    }
    @Test
    void kullanilmis_refresh_token_reddedilmelidir() {

        String refreshTokenValue = "used-token";

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "password",
                        Role.USER
                );

        RefreshToken refreshToken =
                new RefreshToken(
                        refreshTokenValue,
                        user,
                        Instant.now().plusSeconds(3600)
                );

        refreshToken.setUsed(true);

        when(jwtService.isRefreshToken(refreshTokenValue))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken(refreshTokenValue))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(
                BadRequestException.class,
                () -> authService.refresh(
                        new RefreshTokenRequestDto(refreshTokenValue)
                )
        );
    }
    @Test
    void revoked_refresh_token_reddedilmelidir() {

        String refreshTokenValue = "revoked-token";

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "password",
                        Role.USER
                );

        RefreshToken refreshToken =
                new RefreshToken(
                        refreshTokenValue,
                        user,
                        Instant.now().plusSeconds(3600)
                );

        refreshToken.setRevoked(true);

        when(jwtService.isRefreshToken(refreshTokenValue))
                .thenReturn(true);

        when(refreshTokenRepository.findByToken(refreshTokenValue))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(
                BadRequestException.class,
                () -> authService.refresh(
                        new RefreshTokenRequestDto(refreshTokenValue)
                )
        );
    }
    @Test
    void logout_refresh_tokeni_revoke_etmelidir() {

        String refreshTokenValue = "refresh-token";

        User user =
                new User(
                        "fatih",
                        "fatih@test.com",
                        "password",
                        Role.USER
                );

        RefreshToken refreshToken =
                new RefreshToken(
                        refreshTokenValue,
                        user,
                        Instant.now().plusSeconds(3600)
                );

        when(refreshTokenRepository.findByToken(refreshTokenValue))
                .thenReturn(Optional.of(refreshToken));

        authService.logout(
                new RefreshTokenRequestDto(refreshTokenValue)
        );

        assertTrue(refreshToken.isRevoked());

        verify(refreshTokenRepository)
                .save(refreshToken);
    }

}