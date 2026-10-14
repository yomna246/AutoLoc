package tn.esprit.autoloc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class AgenceTests {

    @Autowired
    private AgenceRepositoryMock basicAgenceRepository;

    @Autowired
    private IAgenceRepository fullAgenceRepository;

    // Étape 12
    @Autowired
    private IAgenceService agenceService;

    // ---------- Chapitre 4 : ajout ----------

    private void addAgence(CrudRepository<Agence, Long> repository) {
        int suffix = (int) System.currentTimeMillis();

        Agence agence = Agence.builder()
                .nom("AutoLoc Tunis Centre")
                .ville("Tunis")
                .adresse("12 Avenue Habib Bourguiba")
                .telephone("71 000 000")
                .build();

        Vehicule v1 = Vehicule.builder()
                .immatriculation("123 TUN 4567 " + suffix)
                .marque("Renault")
                .modele("Clio")
                .categorie(CategorieVehicule.CITADINE)
                .tarifJournalier(new BigDecimal("80.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .build();

        Vehicule v2 = Vehicule.builder()
                .immatriculation("234 TUN 8910 " + suffix)
                .marque("Peugeot")
                .modele("508")
                .categorie(CategorieVehicule.BERLINE)
                .tarifJournalier(new BigDecimal("150.00"))
                .statut(StatutVehicule.DISPONIBLE)
                .build();

        agence.addVehicule(v1);
        agence.addVehicule(v2);

        repository.save(agence);
    }

    @Test
    public void basicAddAgence() {
        addAgence(basicAgenceRepository);
    }

    @Test
    public void fullAddAgence() {
        addAgence(fullAgenceRepository);
    }

    // ---------- Chapitre 4 : chargement ----------

    private void loadAgence(CrudRepository<Agence, Long> repository, String typeDepot) {
        Iterable<Agence> agences = repository.findAll();

        StringBuilder sb = new StringBuilder("\n");
        sb.append("Type de dépôt : ").append(typeDepot).append("\n");
        for (Agence a : agences) {
            sb.append(a.getIdAgence()).append(" | Agence ").append(a.getNom()).append("\n");
            sb.append("Vehicules Count : ").append(a.getVehicules().size()).append("\n");
            for (Vehicule v : a.getVehicules()) {
                sb.append("=== ").append(v.getIdVehicule())
                        .append("|").append(v.getImmatriculation()).append("\n");
            }
        }

        fail(sb.toString());
    }

    @Test
    public void basicLoadAgence() {
        loadAgence(basicAgenceRepository, "basic (CrudRepository)");
    }

    @Test
    public void fullLoadAgence() {
        loadAgence(fullAgenceRepository, "full (JpaRepository)");
    }

    @Test
    public void loadSortedAgences() {
        List<Agence> agences = fullAgenceRepository.findAll(Sort.by(Sort.Direction.DESC, "idAgence"));

        StringBuilder sb = new StringBuilder("\n");
        for (Agence a : agences) {
            sb.append(a.getIdAgence()).append(" | Agence ").append(a.getNom()).append("\n");
        }

        fail(sb.toString());
    }

    @Test
    public void loadPagedAgences() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "idAgence"));
        Page<Agence> page = fullAgenceRepository.findAll(pageable);

        StringBuilder sb = new StringBuilder("\n");
        sb.append("Total pages : ").append(page.getTotalPages()).append("\n");
        sb.append("Page en cours : ").append(page.getNumber()).append("\n");
        for (Agence a : page.getContent()) {
            sb.append(a.getIdAgence()).append(" | Agence ").append(a.getNom()).append("\n");
        }

        fail(sb.toString());
    }
    /*erreur est Spring a trouvé deux classes qui peuvent répondre à IAgenceService et ne sait pas laquelle injecter dans AgenceTests.*/
    @Test
    public void createAgence() {
        Agence agence = Agence.builder()
                .nom("AutoLoc Sfax")
                .ville("Sfax")
                .adresse("5 Rue de la République")
                .telephone("74 000 000")
                .build();

        Agence saved = agenceService.create(agence);

        assertNotNull(saved.getIdAgence());
    }
}

// Étape 11
@Repository
interface AgenceRepositoryMock extends CrudRepository<Agence, Long> {
}