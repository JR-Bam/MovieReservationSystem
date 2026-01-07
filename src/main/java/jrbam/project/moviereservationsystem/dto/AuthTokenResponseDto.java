package jrbam.project.moviereservationsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenResponseDto {
    private String token;
}
