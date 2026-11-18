package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.repository.IEquipementRepository;

class EquipementServiceImplTest extends AbstractGenericServiceTest<Equipement> {

    @Mock
    private IEquipementRepository repository;

    private EquipementServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EquipementServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Equipement, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Equipement, Long> repository() {
        return repository;
    }

    @Override
    protected Equipement valid() {
        return Equipement.builder().libelle("GPS").build();
    }

    @Override
    protected Equipement invalid() {
        return Equipement.builder().libelle("  ").build();
    }

    @Override
    protected Equipement validWithId() {
        Equipement equipement = valid();
        equipement.setIdEquipement(ID);
        return equipement;
    }
}