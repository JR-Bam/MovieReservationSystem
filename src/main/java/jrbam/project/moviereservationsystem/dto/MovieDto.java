package jrbam.project.moviereservationsystem.dto;

import jrbam.project.moviereservationsystem.enums.MovieGenre;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto {
    private String title;
    private String description;
    private String posterUrl;
    private List<MovieGenre> genres;
}
