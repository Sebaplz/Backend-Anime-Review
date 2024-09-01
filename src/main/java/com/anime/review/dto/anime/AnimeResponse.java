package com.anime.review.dto.anime;

import com.anime.review.dto.genre.GenreResponse;
import lombok.Data;

import java.util.List;

@Data
public class AnimeResponse {
    private Long id;
    private String title;
    private String urlImage;
    private String synopsis;
    private Integer releaseYear;
    private List<GenreResponse> genres;
}
