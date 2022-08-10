package org.jwt.userManagement.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jwt.userManagement.model.AppUser;
import org.jwt.userManagement.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.jwt.userManagement.enumeration.AccountStatus.LOCKED;

@RequiredArgsConstructor
@Slf4j
public class UsernameAndPasswordAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey secretKey;
    private UsernameAndPasswordRequest authenticationRequest;
    private final UserService userService;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        authenticationRequest =
                new ObjectMapper().readValue(request.getInputStream(), UsernameAndPasswordRequest.class);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        return authenticationManager.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AppUser user = userService.findByUsername(authenticationRequest.getUsername());
        if(user != null && user.getAuthAttempts() > 0){
            userService.updateAuthAttempts(0, user.getId());
        }
        String key = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setIssuedAt(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationDays())))
                .signWith(secretKey.getSecretKey())
                .compact();
        String token = jwtConfig.getPrefix() + key;
        response.addHeader(jwtConfig.getAuthorizationHeader(), token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("Unsuccessful authentication. Credentials:\nusername: {}\npassword: {}",
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        AppUser user = userService.findByUsername(authenticationRequest.getUsername());
        if(user != null){
            user.setAuthAttempts(user.getAuthAttempts()+1);
            if(user.getAuthAttempts() > 3) {
                log.warn("Account by username {} is Locked", user.getUsername());
                user.setAccountStatus(LOCKED);
            }
            userService.updateAuthAttempts(user.getAuthAttempts(), user.getId());
        }
        response.sendError(SC_UNAUTHORIZED);
    }
}
