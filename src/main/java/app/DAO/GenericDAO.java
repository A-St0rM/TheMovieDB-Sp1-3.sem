package app.DAO;

import app.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenericDAO<T> {

    private final Class<T> entityClass;
    protected EntityManagerFactory emf;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    //For test
    public GenericDAO(Class<T> entityClass, EntityManagerFactory emf) {
        this.entityClass = entityClass;
        this.emf = emf;
    }

    public void save(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public T findById(Object id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }

    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void update(T entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(Object id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
