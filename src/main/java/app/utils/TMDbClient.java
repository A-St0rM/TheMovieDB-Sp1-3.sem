package app.utils;

import app.DTO.DiscoverItemDTO;
import app.DTO.DiscoverResponseDTO;
import app.DTO.MovieDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TMDbClient {
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String apiKey;

    public TMDbClient(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public DiscoverResponseDTO discoverMovies(int page) throws Exception {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey
                + "&with_origin_country=DK"
                + "&primary_release_date.gte=2020-01-01"
                + "&primary_release_date.lte=2025-09-17"
                + "&include_adult=false&page=" + page;
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(URI.create(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), DiscoverResponseDTO.class);
    }

    public MovieDetailsDTO fetchMovieDetails(Long id) throws Exception {
        String url = "https://api.themoviedb.org/3/movie/" + id
                + "?api_key=" + apiKey
                + "&append_to_response=credits";
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(URI.create(url)).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), MovieDetailsDTO.class);
    }

    public List<DiscoverItemDTO> fetchDiscoverMovies(
            String country, String startDate, String endDate, int page) throws Exception {

        String url = String.format(
                "https://api.themoviedb.org/3/discover/movie?api_key=%s&region=%s&with_original_language=da&primary_release_date.gte=%s&primary_release_date.lte=%s&page=%d",
                apiKey, country, startDate, endDate, page
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        DiscoverResponseDTO dto = mapper.readValue(response.body(), DiscoverResponseDTO.class);
        return dto.getResults();
    }

}

