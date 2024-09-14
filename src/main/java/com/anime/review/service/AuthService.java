package com.anime.review.service;

import com.anime.review.dto.auth.AuthResponse;
import com.anime.review.dto.auth.AuthenticationRequest;
import com.anime.review.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register (RegisterRequest request);
    AuthResponse authenticate (AuthenticationRequest request);
    void updateImage(String email, String urlImage);
    
}