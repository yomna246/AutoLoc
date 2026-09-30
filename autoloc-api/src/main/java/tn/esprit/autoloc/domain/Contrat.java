package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "contrat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idContrat;

    private LocalDate dateSignature;

    @Column(precision = 10, scale = 2)
    private BigDecimal montantTotal;

    private boolean valide;

    @OneToOne(mappedBy = "contrat")
    private Reservation reservation;

    @OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Paiement> paiements = new HashSet<>();

    public void addPaiement(Paiement p) {
        paiements.add(p);
        p.setContrat(this);
    }
}