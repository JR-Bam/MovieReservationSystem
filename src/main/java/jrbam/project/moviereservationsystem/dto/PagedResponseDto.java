package jrbam.project.moviereservationsystem.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponseDto {
    int totalPages;
    long totalElements;
    List<?> pageData;
    int currentItemCount;
}
