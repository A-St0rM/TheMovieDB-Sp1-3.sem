package app.service;

import app.DAO.ActorDAO;
import app.entities.Actor;

import java.util.HashSet;
import java.util.Set;

public class ActorService {

    private final ActorDAO actorDAO = new ActorDAO();

    public Set<Actor> getAllActors() {
        return new HashSet<>(actorDAO.findAll());
    }
}
