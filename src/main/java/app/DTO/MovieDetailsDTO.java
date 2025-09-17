package app.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MovieDetailsDTO {
    private Long id;
    private String title;
    private String overview;
    @JsonProperty("release_date")
    private String releaseDate;
    private List<GenreDTO> genres;
    @JsonProperty("vote_average")
    private Double voteAverage;
    private Double popularity;
    private CreditsDTO credits;
}

