package app.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CastDTO {
    private Long id;
    private String name;
    private String character;
    private Integer order;
}

