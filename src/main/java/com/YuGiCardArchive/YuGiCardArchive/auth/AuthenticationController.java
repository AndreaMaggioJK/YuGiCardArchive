package com.YuGiCardArchive.YuGiCardArchive.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("cards/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    
    
    /**
     * Registers a user with the provided registration request.
     * @param request the registration request containing user details (Email and Password)
     * @return a ResponseEntity with the authentication response if successful, or an error response if a duplicate entry occurs
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request
    ){
        try {
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (DuplicateEntryException e) {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(AuthenticationResponse.builder().errorMessage(errorMessage).build());
        }
    }

    /**
     * Authenticates a user with the provided authentication request.
     *
     * @param request the authentication request containing user credentials (Email and Password)
     * @return a ResponseEntity with the authentication response if successful
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Refreshes the authentication token using the provided refresh token passed as parameter in the body.
     *
     * @param refreshToken the refresh token
     * @return a ResponseEntity with the refreshed authentication response if successful, or an error response if the refresh token is invalid
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @RequestBody String refreshToken
    ) {
        try {
            return ResponseEntity.ok(service.refreshToken(refreshToken));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Invalid refresh token")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Logs out a user by invalidating their tokens.
     *
     * @param accessToken the access token to retrive the user
     * @return a ResponseEntity with a boolean indicating whether the logout was successful
     */
    @PostMapping("/log-out")
    public ResponseEntity<Boolean> logOut(@RequestBody String accessToken) {
        boolean isLoggedOut = service.logOut(accessToken);
        return ResponseEntity.ok(isLoggedOut);
    }

}
