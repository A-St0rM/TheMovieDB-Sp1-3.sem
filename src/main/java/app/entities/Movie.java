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
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String overview;
    private String releaseDate;

    //Maybe make a join table if having extra time
    @ManyToMany(cascade = CascadeType.ALL) //TODO: Maybe not cascade here?
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL) //TODO: Maybe not cascade here?
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL) //TODO: Maybe not cascade here?
    private Director director;
}