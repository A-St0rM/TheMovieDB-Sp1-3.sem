package app.DAO;

import app.entities.Parcel;

import java.util.List;

public interface IParcelDAO {

    List<Parcel> getAll();

    Parcel getById(int id);

    void save(Parcel parcel);

    void delete(int id);

}
