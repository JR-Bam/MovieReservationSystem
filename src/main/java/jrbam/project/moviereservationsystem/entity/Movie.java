package jrbam.project.moviereservationsystem.entity;

import jakarta.persistence.*;
import jrbam.project.moviereservationsystem.enums.MovieGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "Movie", timeToLive = 3600)
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String posterUrl;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private Set<MovieGenre> genres = new HashSet<>();
}
