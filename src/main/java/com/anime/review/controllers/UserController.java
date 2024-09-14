package com.anime.review.controllers;

import com.anime.review.dto.auth.UpdateImageRequest;
import com.anime.review.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final AuthService authService;
    @PatchMapping("/{email}/image")
    public ResponseEntity<Void> updateImage(@PathVariable String email, @RequestBody UpdateImageRequest request) {
        authService.updateImage(email, request.getUrlImage());
        return ResponseEntity.ok().build();
    }

}
