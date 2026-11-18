package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.io.IRapport;
import tn.esprit.autoloc.io.ModeRapport;
import tn.esprit.autoloc.io.RapportFactory;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AgenceServiceImplTest extends AbstractGenericServiceTest<Agence> {

    @Mock
    private IAgenceRepository repository;

    @Mock
    private IVehiculeService vehiculeService;

    @Mock
    private IEmployeService employeService;

    @Mock
    private RapportFactory rapportFactory;

    @Mock
    private IRapport rapport;

    private AgenceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AgenceServiceImpl(repository, vehiculeService, employeService,
                rapportFactory, ModeRapport.TEXTE);
    }

    @Override
    protected GenericServiceImpl<Agence, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Agence, Long> repository() {
        return repository;
    }

    @Override
    protected Agence valid() {
        return Agence.builder()
                .nom("AutoLoc Tunis")
                .ville("Tunis")
                .adresse("12 Avenue Habib Bourguiba")
                .telephone("71 000 000")
                .build();
    }

    @Override
    protected Agence invalid() {
        return Agence.builder().nom("AutoLoc Tunis").build();
    }

    @Override
    protected Agence validWithId() {
        Agence agence = valid();
        agence.setIdAgence(ID);
        return agence;
    }

    @Test
    void hasVehiculesReturnsFalseWhenEmpty() {
        when(repository.findById(ID)).thenReturn(Optional.of(validWithId()));

        assertFalse(service.hasVehicules(ID));
    }

    @Test
    void hasVehiculesReturnsTrueWhenNotEmpty() {
        Agence agence = validWithId();
        agence.addVehicule(new Vehicule());
        when(repository.findById(ID)).thenReturn(Optional.of(agence));

        assertTrue(service.hasVehicules(ID));
    }

    @Test
    void simulerComputesTotalAndReturnsReport() {
        Agence agence = validWithId();
        Vehicule v1 = new Vehicule();
        Vehicule v2 = new Vehicule();
        agence.addVehicule(v1);
        agence.addVehicule(v2);

        when(rapportFactory.creer(ModeRapport.TEXTE)).thenReturn(rapport);
        when(vehiculeService.calculTarif(v1, 3, VehiculeOffre.STANDARD)).thenReturn(100.0);
        when(vehiculeService.calculTarif(v2, 3, VehiculeOffre.STANDARD)).thenReturn(50.0);
        when(rapport.contenu()).thenReturn("rapport");

        String result = service.simuler(agence, 3, VehiculeOffre.STANDARD);

        assertEquals("rapport", result);
        verify(rapport).ecrireTotal(150.0);
    }

    @Test
    void ajouterEmployesCreatesEmployeesAndUpdatesEffectif() {
        Agence existing = validWithId();
        Agence source = valid();
        Employe employe = Employe.builder()
                .nom("Trabelsi")
                .prenom("Ali")
                .role(RoleEmploye.AGENT)
                .build();
        source.addEmploye(employe);
        when(repository.findById(ID)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Agence result = service.ajouterEmployes(ID, source);

        assertEquals(1, result.getEffectif());
        verify(employeService).create(employe);
    }
}