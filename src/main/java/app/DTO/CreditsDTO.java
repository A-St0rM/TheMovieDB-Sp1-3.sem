package app.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CreditsDTO {
    private List<CastDTO> cast;
    private List<CrewDTO> crew;
}

