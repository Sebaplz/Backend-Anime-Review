package com.anime.review.service;


import com.anime.review.dto.AuthResponse;
import com.anime.review.dto.AuthenticationRequest;
import com.anime.review.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register (RegisterRequest request);
    AuthResponse authenticate (AuthenticationRequest request);
}