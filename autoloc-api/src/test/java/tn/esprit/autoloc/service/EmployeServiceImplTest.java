package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.RoleEmploye;
import tn.esprit.autoloc.repository.IEmployeRepository;

class EmployeServiceImplTest extends AbstractGenericServiceTest<Employe> {

    @Mock
    private IEmployeRepository repository;

    private EmployeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new EmployeServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Employe, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Employe, Long> repository() {
        return repository;
    }

    @Override
    protected Employe valid() {
        return Employe.builder()
                .nom("Trabelsi")
                .prenom("Ali")
                .role(RoleEmploye.AGENT)
                .build();
    }

    @Override
    protected Employe invalid() {
        return Employe.builder().nom("Trabelsi").prenom("Ali").build();
    }

    @Override
    protected Employe validWithId() {
        Employe employe = valid();
        employe.setIdEmploye(ID);
        return employe;
    }
}