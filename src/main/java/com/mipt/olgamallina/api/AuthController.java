package com.mipt.olgamallina.api;

import com.mipt.olgamallina.dto.LoginRequest;
import com.mipt.olgamallina.dto.LoginResponse;
import com.mipt.olgamallina.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(user);

        return new LoginResponse(
                "Bearer",
                token,
                jwtUtils.expiresAt(token),
                user.getUsername(),
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }
}