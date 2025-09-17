package app.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DiscoverResponseDTO {
    private int page;
    private List<DiscoverItemDTO> results = new ArrayList<DiscoverItemDTO>();
}

