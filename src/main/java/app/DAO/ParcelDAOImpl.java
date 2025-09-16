package app.DAO;

import app.entities.Parcel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ParcelDAOImpl implements IParcelDAO {

    private final EntityManagerFactory emf;

    public ParcelDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Parcel> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Parcel> q = em.createQuery("SELECT p FROM Parcel p", Parcel.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Parcel getById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Parcel.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Parcel parcel) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (parcel.getId() == 0) {
                em.persist(parcel);   // ny
            } else {
                em.merge(parcel);     // update
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Parcel parcel = em.find(Parcel.class, id);
            if (parcel != null) em.remove(parcel);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
