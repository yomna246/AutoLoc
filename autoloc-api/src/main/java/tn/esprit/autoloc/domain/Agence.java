package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    // Q11 : champ mappé entier
    private int effectif;

    @OneToMany(mappedBy = "agence", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Vehicule> vehicules = new ArrayList<>();

    // Accepté en entrée (JSON imbriqué), jamais renvoyé en sortie
    @OneToMany(mappedBy = "agence")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Builder.Default
    private List<Employe> employes = new ArrayList<>();

    public void addVehicule(Vehicule v) {
        vehicules.add(v);
        v.setAgence(this);
    }

    // Q10 : relation agence/employé symétrique, les deux côtés restent cohérents
    public void addEmploye(Employe e) {
        employes.add(e);
        e.setAgence(this);
    }

    public void removeEmploye(Employe e) {
        employes.remove(e);
        e.setAgence(null);
    }
}