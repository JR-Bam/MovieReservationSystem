package jrbam.project.moviereservationsystem.service;

import jrbam.project.moviereservationsystem.dto.MovieDto;
import jrbam.project.moviereservationsystem.entity.Movie;
import jrbam.project.moviereservationsystem.enums.MovieGenre;
import jrbam.project.moviereservationsystem.exception.MovieNotFoundException;
import jrbam.project.moviereservationsystem.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Cacheable(
        key = "#id",
        value = "movies",
        unless = "#result == null"
    )
    public Movie getMovieById(Long id) {
        return movieRepository.findMovieById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found", HttpStatus.NOT_FOUND));
    }

    @Cacheable(
        key = "'all_' + #page + '_' + #limit",
        value = "movies_page",
        unless = "#result == null || #result.isEmpty()"
    )
    public Page<Movie> getMovies(int page, int limit) {
        return movieRepository.findAll(PageRequest.of(page, limit));
    }

    @Cacheable(
        key = "'genre_' + #genre + '_' + #page + '_' + #limit",
        value = "movies_genre_page",
        unless = "#result == null || #result.isEmpty()"
    )
    public Page<Movie> getMoviesOfGenre(int page, int limit, MovieGenre genre) {
        return movieRepository.findAllByGenre(genre, PageRequest.of(page, limit));
    }

    @Caching(
        evict = {
            @CacheEvict(value = "movies_page", allEntries = true),
            @CacheEvict(value = "movies_genre_page", allEntries = true)
        }
    )
    public Movie addMovie(MovieDto movieDto) {
        return movieRepository.save(Movie.builder()
                .title(movieDto.getTitle())
                .description(movieDto.getDescription())
                .posterUrl(movieDto.getPosterUrl())
                .genres(new HashSet<>(movieDto.getGenres()))
                .build());
    }

    @Caching(
            put = @CachePut(key = "#id", value = "movies"),
            evict = {
                    @CacheEvict(value = "movies_page", allEntries = true),
                    @CacheEvict(value = "movies_genre_page", allEntries = true)
            }
    )
    public Movie updateMovie(Long id, MovieDto movieDto) {
        return movieRepository.findById(id)
                .map( movie -> {
                    movie.setTitle(movieDto.getTitle());
                    movie.setDescription(movieDto.getDescription());
                    movie.setPosterUrl(movieDto.getPosterUrl());
                    movie.setGenres(new HashSet<>(movieDto.getGenres()));
                    return movieRepository.save(movie);
                })
                .orElseThrow(() -> new MovieNotFoundException("Movie not found", HttpStatus.NOT_FOUND));
    }

    @Caching(
        evict = {
            @CacheEvict(key = "#id", value = "movies"),
            @CacheEvict(value = "movies_page", allEntries = true),
            @CacheEvict(value = "movies_genre_page", allEntries = true)
        }
    )
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
