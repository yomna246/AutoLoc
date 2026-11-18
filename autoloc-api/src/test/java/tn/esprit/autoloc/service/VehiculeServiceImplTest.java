package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.calcul.IOffreStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class VehiculeServiceImplTest extends AbstractGenericServiceTest<Vehicule> {

    private static final Long AGENCE_ID = 2L;
    private static final Long EQUIPEMENT_ID = 3L;

    @Mock
    private IVehiculeRepository repository;

    @Mock
    private IAgenceRepository agenceRepository;

    @Mock
    private IEquipementRepository equipementRepository;

    @Mock
    private IOffreStrategy strategieStandard;

    private VehiculeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new VehiculeServiceImpl(repository,
                Map.of("standard", strategieStandard),
                agenceRepository,
                equipementRepository);
    }

    @Override
    protected GenericServiceImpl<Vehicule, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Vehicule, Long> repository() {
        return repository;
    }

    @Override
    protected Vehicule valid() {
        return Vehicule.builder()
                .immatriculation("123 TUN 4567")
                .marque("Renault")
                .modele("Clio")
                .tarifJournalier(new BigDecimal("100.00"))
                .build();
    }

    @Override
    protected Vehicule invalid() {
        return Vehicule.builder().marque("Renault").build();
    }

    @Override
    protected Vehicule validWithId() {
        Vehicule vehicule = valid();
        vehicule.setIdVehicule(ID);
        return vehicule;
    }

    @Test
    void calculTarifUsesMatchingStrategy() {
        when(strategieStandard.calculer(100.0, 3)).thenReturn(300.0);

        double tarif = service.calculTarif(valid(), 3, VehiculeOffre.STANDARD);

        assertEquals(300.0, tarif);
    }

    @Test
    void calculTarifUnsupportedOfferThrowsException() {
        Vehicule vehicule = valid();

        assertThrows(IllegalArgumentException.class,
                () -> service.calculTarif(vehicule, 3, VehiculeOffre.WEEKEND));
    }

    @Test
    void desaffecterRemovesAgence() {
        Vehicule vehicule = validWithId();
        vehicule.setAgence(new Agence());
        when(repository.findById(ID)).thenReturn(Optional.of(vehicule));

        service.desaffecter(ID);

        assertNull(vehicule.getAgence());
    }

    @Test
    void affecterSetsAgence() {
        Vehicule vehicule = validWithId();
        Agence agence = new Agence();
        when(repository.findById(ID)).thenReturn(Optional.of(vehicule));
        when(agenceRepository.findById(AGENCE_ID)).thenReturn(Optional.of(agence));

        service.affecter(ID, AGENCE_ID);

        assertSame(agence, vehicule.getAgence());
    }

    @Test
    void affecterUnknownAgenceThrowsNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.of(validWithId()));
        when(agenceRepository.findById(AGENCE_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.affecter(ID, AGENCE_ID));
    }

    @Test
    void affecterEquipementAddsVehiculeOnlyOnce() {
        Vehicule vehicule = validWithId();
        Equipement equipement = Equipement.builder().libelle("GPS").build();
        when(repository.findById(ID)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(EQUIPEMENT_ID)).thenReturn(Optional.of(equipement));

        service.affecterEquipement(ID, EQUIPEMENT_ID);
        service.affecterEquipement(ID, EQUIPEMENT_ID);

        assertEquals(1, equipement.getVehicules().size());
    }

    @Test
    void affecterEquipementUnknownEquipementThrowsNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.of(validWithId()));
        when(equipementRepository.findById(EQUIPEMENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.affecterEquipement(ID, EQUIPEMENT_ID));
    }
}