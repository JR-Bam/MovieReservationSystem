package jrbam.project.moviereservationsystem.repository;

import jrbam.project.moviereservationsystem.entity.Movie;
import jrbam.project.moviereservationsystem.enums.MovieGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g = :genre")
    Page<Movie> findAllByGenre(MovieGenre genre, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g IN :genres")
    Page<Movie> findAllByGenres(List<MovieGenre> genres, Pageable pageable);


    Optional<Movie> findMovieById(Long id);
}
