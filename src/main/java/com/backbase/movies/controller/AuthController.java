package com.backbase.movies.controller;

import com.backbase.movies.model.UserRecord;
import com.backbase.movies.service.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
public class AuthController {

    private final ReactiveUserDetailsService users;
    private final JWTService jwtService;
    private final PasswordEncoder encoder;

    public AuthController(ReactiveUserDetailsService users, JWTService jwtService, PasswordEncoder encoder) {
        this.users = users;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody UserRecord user) {
        Mono<UserDetails> userFound = users.findByUsername(user.username())
                .defaultIfEmpty(User.builder().username("null")
                .password("null").build());

        return userFound.flatMap(u -> {
            if(!u.getUsername().equals("null")) {
                // check if the password is correct
                if(encoder.matches(user.password(), u.getPassword())) {
                    return Mono.just(ResponseEntity.ok(jwtService.generateToken(u.getUsername())));
                }
                return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid Credentials"));
            }
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
        });

    }

}
