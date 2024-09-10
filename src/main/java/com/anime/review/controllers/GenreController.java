package com.anime.review.controllers;

import com.anime.review.dto.ApiResponse;
import com.anime.review.dto.genre.GenreResponse;
import com.anime.review.entity.Genre;
import com.anime.review.exceptions.common.AlreadyExistsException;
import com.anime.review.exceptions.common.NotFoundException;
import com.anime.review.service.GenreService;
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
@RequestMapping("/api/v1/genres")
@CrossOrigin(origins = "*")
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/all")
    public ResponseEntity<Page<GenreResponse>> getAllGenre(@PageableDefault(size = 10) Pageable pageable) {
        Page<GenreResponse> genre = genreService.findAll(pageable);
        return ResponseEntity.ok(genre);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long id) {
        Optional<GenreResponse> genre = genreService.findGenreResponseById(id);
        return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createGenre(@Valid @RequestBody Genre genre) {
        try {
            genreService.saveGenre(genre, false);
            return ResponseEntity.ok(new ApiResponse(true, "Genero creado con éxito"));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateGenre(@PathVariable Long id, @Valid @RequestBody Genre genreDetails) {
        try{
            Optional<Genre> genre = genreService.findById(id);
            if (genre.isPresent()) {
                genreDetails.setId(id);
                genreService.saveGenre(genreDetails, true);
                return ResponseEntity.ok(new ApiResponse(true, "Genero actualizado con éxito"));
            } else {
                throw new NotFoundException("Genero no encontrado!");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Genero no encontrado!"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteGenre(@PathVariable Long id) {
        Optional<Genre> genre = genreService.findById(id);
        if (genre.isPresent()) {
            genreService.deleteGenre(id);
            return ResponseEntity.ok(new ApiResponse(true, "Genero eliminado con éxito"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
