package com.anime.review.service;

import com.anime.review.dto.genre.GenreResponse;
import com.anime.review.entity.Genre;
import com.anime.review.exceptions.common.AlreadyExistsException;
import com.anime.review.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private GenreResponse mapToResponse(Genre genre) {
        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setId(genre.getId());
        genreResponse.setName(genre.getName());
        return genreResponse;
    }

    public Page<GenreResponse> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Optional<GenreResponse> findGenreResponseById(Long id) {
        return genreRepository.findById(id).map(this::mapToResponse);
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public void saveGenre(Genre genre, boolean isUpdate) throws AlreadyExistsException {
        if (!isUpdate) {
            Optional<Genre> existingGenre = genreRepository.findByName(genre.getName());
            if (existingGenre.isPresent()) {
                throw new AlreadyExistsException("Ya existe un género con el mismo nombre!");
            }
        }
        genreRepository.save(genre);
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

}
