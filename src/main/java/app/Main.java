package app;

import app.DAO.IParcelDAO;
import app.DAO.ParcelDAOImpl;
import app.config.HibernateConfig;
import app.entities.Parcel;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        try {
            IParcelDAO parcelDAO = new ParcelDAOImpl(emf);

            // EKSEMPEL: opret → læs → slet
            Parcel p = Parcel.builder()
                    .trackingNumber("ABC123")
                    .senderName("Haidar")
                    .receiverName("Andrea")
                    .build();

            parcelDAO.save(p);
            System.out.println("Saved id: " + p.getId());

            parcelDAO.getAll().forEach(System.out::println);

            parcelDAO.delete(p.getId());
            System.out.println("Deleted id: " + p.getId());

        } finally {
            emf.close(); // vigtigt at lukke EFTER brug
        }
    }
}
