package jrbam.project.moviereservationsystem.controller;

import jrbam.project.moviereservationsystem.dto.MovieDto;
import jrbam.project.moviereservationsystem.dto.PagedResponseDto;
import jrbam.project.moviereservationsystem.dto.ResponseDto;
import jrbam.project.moviereservationsystem.entity.Movie;
import jrbam.project.moviereservationsystem.enums.MovieGenre;
import jrbam.project.moviereservationsystem.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .message("Fetched movie with id: " + id)
                        .data(movie)
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<PagedResponseDto> getAll(
            @RequestParam(required = false) MovieGenre genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<Movie> moviePage;
        if (genre == null) {
            moviePage = movieService.getMovies(page, limit);
        } else {
            moviePage = movieService.getMoviesOfGenre(page, limit, genre);
        }
        List<Movie> movies = moviePage.getContent();
        return ResponseEntity.ok(
                PagedResponseDto.builder()
                        .totalPages(moviePage.getTotalPages())
                        .totalElements(moviePage.getTotalElements())
                        .pageData(movies)
                        .build()
        );
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addNewMovie(@RequestBody MovieDto movieDto) {
        Movie movie = movieService.addMovie(movieDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.builder()
                        .message("Movie successfully created")
                        .data(movie)
                        .build()
                );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieDto movieDto
    ) {
        Movie movie = movieService.updateMovie(id, movieDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.builder()
                        .message("Movie successfully updated")
                        .data(movie)
                        .build()
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

