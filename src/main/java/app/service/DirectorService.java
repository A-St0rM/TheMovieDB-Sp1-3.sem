package app.service;

import app.DAO.DirectorDAO;
import app.entities.Director;

import java.util.HashSet;
import java.util.Set;

public class DirectorService {
    private final DirectorDAO directorDAO = new DirectorDAO();

    public Set<Director> getAllDirectors() {
        return new HashSet<>(directorDAO.findAll());
    }
}
