package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String trackingNumber;
    private String senderName;
    private String receiverName;


    @Builder.Default
    private LocalDateTime updated = null;

    //Define pre-update and pre-persist life cycle methods in the “Parcel” entity to update the last updated timestamp automatically.

    @PrePersist
    private void prePersist() {
        updated = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        updated = LocalDateTime.now();
    }
}