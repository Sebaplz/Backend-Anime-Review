package com.anime.review.controllers;

import com.anime.review.dto.ApiResponse;
import com.anime.review.dto.anime.AnimeResponse;
import com.anime.review.entity.Anime;
import com.anime.review.exceptions.common.AlreadyExistsException;
import com.anime.review.exceptions.common.NotFoundException;
import com.anime.review.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/animes")
@CrossOrigin(origins = "*")
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping("/all")
    public ResponseEntity<Page<AnimeResponse>> getAllAnimes(@PageableDefault(size = 10) Pageable pageable) {
        Page<AnimeResponse> animes = animeService.findAll(pageable);
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeResponse> getAnimeById(@PathVariable Long id) {
        Optional<AnimeResponse> anime = animeService.findAnimeResponseById(id);
        return anime.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAnime(@Valid @RequestBody Anime anime) {
        try {
            animeService.saveAnime(anime, false);
            return ResponseEntity.ok(new ApiResponse(true, "Anime creado con éxito"));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAnime(@PathVariable Long id, @Valid @RequestBody Anime animeDetails) {
        try {
            Optional<Anime> anime = animeService.findById(id);
            if (anime.isPresent()) {
                animeDetails.setId(id);
                animeService.saveAnime(animeDetails, true);
                return ResponseEntity.ok(new ApiResponse(true, "Anime actualizado con éxito"));
            } else {
                throw new NotFoundException("Anime no encontrado!");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Anime no encontrado!"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAnime(@PathVariable Long id) {
        Optional<Anime> anime = animeService.findById(id);
        if (anime.isPresent()) {
            animeService.deleteAnime(id);
            return ResponseEntity.ok(new ApiResponse(true, "Anime eliminado con éxito"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
