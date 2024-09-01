package com.anime.review.service;

import com.anime.review.dto.anime.AnimeResponse;
import com.anime.review.dto.genre.GenreResponse;
import com.anime.review.entity.Anime;
import com.anime.review.entity.Genre;
import com.anime.review.exceptions.common.AlreadyExistsException;
import com.anime.review.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    private AnimeResponse mapToResponse(Anime anime) {
        AnimeResponse animeResponse = new AnimeResponse();
        animeResponse.setId(anime.getId());
        animeResponse.setTitle(anime.getTitle());
        animeResponse.setUrlImage(anime.getUrlImage());
        animeResponse.setSynopsis(anime.getSynopsis());
        animeResponse.setReleaseYear(anime.getReleaseYear());
        animeResponse.setGenres(mapGenresToResponses(anime.getGenres()));
        return animeResponse;
    }

    private List<GenreResponse> mapGenresToResponses(List<Genre> genres) {
        return genres.stream().map(this::mapGenreToResponse).collect(Collectors.toList());
    }

    private GenreResponse mapGenreToResponse(Genre genre) {
        GenreResponse genreResponse = new GenreResponse();
        genreResponse.setId(genre.getId());
        genreResponse.setName(genre.getName());
        return genreResponse;
    }

    public Page<AnimeResponse> findAll(Pageable pageable) {
        return animeRepository.findAll(pageable).map(this::mapToResponse);
    }

    public Optional<AnimeResponse> findAnimeResponseById(Long id) {
        return animeRepository.findById(id).map(this::mapToResponse);
    }

    public Optional<Anime> findById(Long id) {
        return animeRepository.findById(id);
    }

    public void saveAnime(Anime anime, boolean isUpdate) throws AlreadyExistsException {
        if (!isUpdate) {
            Optional<Anime> existingAnime = animeRepository.findByTitle(anime.getTitle());
            if (existingAnime.isPresent()) {
                throw new AlreadyExistsException("Ya existe un anime con el mismo nombre!");
            }
        }
        animeRepository.save(anime);
    }

    public void deleteAnime(Long id) {
        animeRepository.deleteById(id);
    }
}
