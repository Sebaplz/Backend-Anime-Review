package com.anime.review.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "animes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty!")
    @Size(min = 1, max = 100, message = "The title must be between 1 and 100 characters.")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "URL image cannot be empty!")
    @Column(nullable = false)
    private String urlImage;

    @NotBlank(message = "Synopsis cannot be empty!")
    @Size(max = 500, message = "The synopsis cannot exceed 500 characters.")
    @Column(nullable = false, length = 500)
    private String synopsis;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @ManyToMany
    @JoinTable(
            name = "anime_genre",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;
}
