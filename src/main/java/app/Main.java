package app;

import app.DTO.DiscoverItemDTO;
import app.DTO.MovieDetailsDTO;
import app.entities.Movie;
import app.service.ActorService;
import app.service.DirectorService;
import app.service.GenreService;
import app.service.MovieService;
import app.utils.TMDbClient;
import app.utils.Utils;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        String apiKey = Utils.getPropertyValue("TMDB_API_KEY", "config.properties");
        TMDbClient client = new TMDbClient(apiKey);
        MovieService movieService = new MovieService();
        ActorService actorService = new ActorService();
        DirectorService directorService = new DirectorService();
        GenreService genreService = new GenreService();

        boolean fetchAndSave = false;
        if(fetchAndSave) {

            int currentYear = LocalDate.now().getYear();
            int startYear = currentYear - 5;

            System.out.println("Fetching Danish movies from " + startYear + " to " + currentYear + "…");

            int page = 1;
            boolean hasMore = true;
            while (hasMore) {
                List<DiscoverItemDTO> discoverPage = client.fetchDiscoverMovies(
                        "DK", startYear + "-01-01", currentYear + "-12-31", page);

                if (discoverPage.isEmpty()) {
                    hasMore = false;
                } else {
                    for (DiscoverItemDTO item : discoverPage) {
                        // fetch full movie details + credits
                        MovieDetailsDTO details = client.fetchMovieDetails(item.getId());
                        movieService.saveMovie(details);
                        System.out.println("Saved movie: " + details.getTitle());
                    }
                    page++;
                }
            }
        }

        //TODO: Multithreading for database for more effective runtime
        //TODO: Missing MovieService.getMoviesByGenre(String name).

        //MovieService.updateMovie(Long tmdbId, String newTitle, LocalDate newDate)

        //MovieService.deleteMovie(Long tmdbId)

        //MovieService.getTop10LowestRated()
        System.out.println("=== Data fetch complete ===");


        System.out.println("\nAll movies:");
        Set<Movie> allMovies = movieService.getAllMovies();
        allMovies.forEach(m -> System.out.println(" - " + m.getTitle()));

        System.out.println("\nSearch movies containing 'Krigen':");
        Set<Movie> searched = movieService.searchMovies("Krigen");
        searched.forEach(m -> System.out.println(" - " + m.getTitle()));

        System.out.println("\nTop 10 highest rated:");
        movieService.getTop10HighestRated().forEach(m ->
                System.out.println(" - " + m.getTitle() + " (" + m.getVoteAverage() + ")"));

        System.out.println("\nTop 10 most popular:");
        movieService.getTop10MostPopular().forEach(m ->
                System.out.println(" - " + m.getTitle() + " (" + m.getPopularity() + ")"));

        System.out.println("\nAverage rating of all movies:");
        System.out.println(movieService.getAverageRating());

        System.out.println("\nAll actors:");
        actorService.getAllActors().forEach(a -> System.out.println(" - " + a.getName()));

        System.out.println("\nAll directors:");
        directorService.getAllDirectors().forEach(d -> System.out.println(" - " + d.getName()));

        System.out.println("\nAll genres:");
        genreService.getAllGenres().forEach(g -> System.out.println(" - " + g.getName()));
    }

}

