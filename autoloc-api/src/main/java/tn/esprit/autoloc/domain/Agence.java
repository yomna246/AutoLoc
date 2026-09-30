package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agence")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAgence;

    private String nom;
    private String ville;
    private String adresse;
    private String telephone;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "agence")
    @Builder.Default
    private List<Employe> employes = new ArrayList<>();

    public void addVehicule(Vehicule v) {
        vehicules.add(v);
        v.setAgence(this);
    }

    public void addEmploye(Employe e) {
        employes.add(e);
        e.setAgence(this);
    }
}