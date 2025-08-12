package com.byllameister.modelstore.auth;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final IpAddressUtils ipAddressUtils;
    private final IpBruteForceProtectionService ipBruteForceProtectionService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        String ip = ipAddressUtils.getClientIP(httpServletRequest);
        if (ipBruteForceProtectionService.isBlocked(ip)) {
            throw new TooManyAuthenticationRequestsException();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );

            ipBruteForceProtectionService.loginSucceeded(ip);

            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var accessToken = jwtService.generateAccessToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            var cookie = new Cookie("refreshToken", refreshToken.toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/auth/refresh");
            cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
            cookie.setSecure(true);
            httpServletResponse.addCookie(cookie);

            return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
        } catch (BadCredentialsException e) {
            ipBruteForceProtectionService.loginFailed(ip);
            throw e;
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user =  userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        return  ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto("Invalid credentials"));
    }

    @ExceptionHandler(TooManyAuthenticationRequestsException.class)
    public ResponseEntity<ErrorDto> handleTooManyAuthenticationRequestsException(TooManyAuthenticationRequestsException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ErrorDto(ex.getMessage()));
    }
}
