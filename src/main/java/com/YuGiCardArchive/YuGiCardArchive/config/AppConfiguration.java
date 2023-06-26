package com.YuGiCardArchive.YuGiCardArchive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.YuGiCardArchive.YuGiCardArchive.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final UserRepo repo;
    
    /**
     * Configures the UserDetailsService bean.
     *
     * @return an implementation of UserDetailsService that retrieves user details from the repository
     * @throws UsernameNotFoundException if a user is not found with the given username
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repo.findByEmail(username)
            .orElseThrow( () -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures the AuthenticationProvider bean.
     *
     * @return an instance of DaoAuthenticationProvider configured with the userDetailsService and passwordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the PasswordEncoder bean.
     *
     * @return an instance of BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the AuthenticationManager bean.
     *
     * @param config the AuthenticationConfiguration used to obtain the AuthenticationManager
     * @return the AuthenticationManager instance obtained from the configuration
     * @throws Exception if there is an error obtaining the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationMenager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
