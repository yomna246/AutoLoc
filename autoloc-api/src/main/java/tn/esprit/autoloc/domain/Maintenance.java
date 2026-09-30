package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaintenance;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Vehicule vehicule;
}