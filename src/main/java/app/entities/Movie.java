package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "actors", "genres", "director"})
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Long tmdbId;
    private String title;

    @Column(length = 5000)
    private String overview;
    private String releaseDate;
    private Double voteAverage;
    private Double popularity;

    //Maybe make a join table if having extra time
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //TODO: Maybe not cascade here?
    @Builder.Default
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //TODO: Maybe not cascade here?
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //TODO: Maybe not cascade here?
    private Director director;
}