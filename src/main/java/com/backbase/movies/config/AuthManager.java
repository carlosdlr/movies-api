package com.backbase.movies.config;

import com.backbase.movies.service.JWTService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthManager implements ReactiveAuthenticationManager {

    private final JWTService jwtService;
    private final ReactiveUserDetailsService users;

    public AuthManager(JWTService jwtService, ReactiveUserDetailsService users) {
        this.jwtService = jwtService;
        this.users = users;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .cast(BearerToken.class)
                .flatMap(auth -> {
                    String username = jwtService.getUsername(auth.getCredentials());
                    Mono<UserDetails> userFound = users.findByUsername(username)
                            .defaultIfEmpty(User.builder().username("null").password("null").build());

                    Mono<Authentication> authenticatedUser = userFound.flatMap(u -> {
                       if (u.getUsername().equals("null")) {
                           Mono.error(new IllegalArgumentException("User not found"));
                       }
                       if(jwtService.isValidateToken(u, auth.getCredentials())) {
                           return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(),
                                   u.getPassword(), u.getAuthorities()));
                       }
                       Mono.error(new IllegalArgumentException("Invalid / expired token"));
                       return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(u.getUsername(),
                                u.getPassword()));
                    });
                    return authenticatedUser;
                });
    }
}
