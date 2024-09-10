package com.anime.review.service;

import com.anime.review.dto.auth.AuthResponse;
import com.anime.review.dto.auth.AuthenticationRequest;
import com.anime.review.dto.auth.RegisterRequest;
import com.anime.review.entity.Role;
import com.anime.review.entity.User;
import com.anime.review.exceptions.auth.EmailAlreadyExistsException;
import com.anime.review.exceptions.auth.EmailNotFoundException;
import com.anime.review.repository.UserRepository;
import com.anime.review.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        Optional<User> existUser = userRepository.findUserByEmail(request.getEmail());
        if (existUser.isPresent()) {
            throw new EmailAlreadyExistsException("El email ya está registrado!");
        } else {
            var user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        }
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword())
            );
            User user = userOptional.get();
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        } else {
            throw new EmailNotFoundException("El email no está registrado!");
        }
    }
}