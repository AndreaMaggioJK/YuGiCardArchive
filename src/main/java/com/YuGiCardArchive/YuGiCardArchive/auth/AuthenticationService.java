package com.YuGiCardArchive.YuGiCardArchive.auth;

import java.io.IOException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.YuGiCardArchive.YuGiCardArchive.config.JwtService;
import com.YuGiCardArchive.YuGiCardArchive.model.Role;
import com.YuGiCardArchive.YuGiCardArchive.model.User;
import com.YuGiCardArchive.YuGiCardArchive.repo.UserRepo;
import com.YuGiCardArchive.YuGiCardArchive.token.Token;
import com.YuGiCardArchive.YuGiCardArchive.token.TokenRepository;
import com.YuGiCardArchive.YuGiCardArchive.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;


    /**
     * Registers a user with the provided registration request.
     *
     * @param request the registration request containing user details (Email,Password)
     * @return an AuthenticationResponse object containing the access token and refresh token
     * @throws DuplicateEntryException if the email already exists in the system
     */
    public AuthenticationResponse register(RegisterRequest request){
        try{
            var user = User.builder()
            .nickName(request.getNickName())
            .email(request.getEmail())
            .pw(passwordEncoder.encode(request.getPw()) )
            .role(Role.USER)
            .build();
        var savedUser = repo.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                .build();
        }catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Email already exists");
        }
    }

    /**
     * Authenticates a user with the provided authentication request.
     *
     * @param request the authentication request containing user credentials (Email,Password)
     * @return an AuthenticationResponse object containing the access token and refresh token
     * @throws AuthenticationException if the authentication fails
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw())
        );
        var user = repo.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Saves the user token in the token repository.
     *
     * @param user     the user associated with the token
     * @param jwtToken the JWT token to be saved
     */
    private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
        tokenRepository.save(token);
    }

    /**
     * Revokes all valid tokens associated with the user.
     *
     * @param user the user whose tokens need to be revoked
     */
    private void revokeAllUserTokens(User user) {
        
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Refreshes the authentication token using the provided refresh token.
     *
     * @param refreshToken the refresh token used to generate a new access token
     * @return an AuthenticationResponse object containing the new access token and the original refresh token
     * @throws RuntimeException if the refresh token is invalid
     */    
    public AuthenticationResponse refreshToken(
            @RequestParam(required = true) String refreshToken
    ){
        var userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repo.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return authResponse;
            }
        }
        throw new RuntimeException("Invalid refresh token");
    }

    /**
     * Logs out a user by revoking their access tokens.
     *
     * @param accessToken the access token of the user to log out
     * @return true if the logout was successful, false otherwise
     */
    public boolean logOut(
        @RequestParam(required = true) String accessToken
    ){
        var userEmail = jwtService.extractUsername(accessToken);
        System.out.println("logOut: "+userEmail);
        if (userEmail != null) {
            var user = this.repo.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(accessToken, user)) {
                System.out.println("logOut TRUE: "+userEmail);
                revokeAllUserTokens(user);
                return true;
            }
        }
        return false;
    }

    
}
