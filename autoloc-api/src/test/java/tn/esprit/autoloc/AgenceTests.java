package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import static org.junit.jupiter.api.Assertions.fail;
import java.math.BigDecimal;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock agenceRepository;

    @Test
    public void addAgence() {
        Agence agence = Agence.builder()
                .nom("AutoLoc Tunis Centre")
                .ville("Tunis")
                .adresse("12 Avenue Habib Bourguiba")
                .telephone("71 000 000")
                .build();

        Vehicule v1 = Vehicule.builder()
                .immatriculation("123 TUN 4567")
                .marque("Renault")
                .modele("Clio")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("80.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .build();

        Vehicule v2 = Vehicule.builder()
                .immatriculation("234 TUN 8910")
                .marque("Peugeot")
                .modele("508")
                .categorie(CategorieVehicule.BERLINE)
                .tarifJournalier(new BigDecimal("150.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .build();

        agence.addVehicule(v1);
        agence.addVehicule(v2);

        agenceRepository.save(agence);
    }
}

interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}