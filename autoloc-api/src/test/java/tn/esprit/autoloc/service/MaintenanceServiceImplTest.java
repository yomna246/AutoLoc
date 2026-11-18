package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

import java.time.LocalDate;

class MaintenanceServiceImplTest extends AbstractGenericServiceTest<Maintenance> {

    @Mock
    private IMaintenanceRepository repository;

    private MaintenanceServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new MaintenanceServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Maintenance, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Maintenance, Long> repository() {
        return repository;
    }

    @Override
    protected Maintenance valid() {
        return Maintenance.builder()
                .dateDebut(LocalDate.of(2026, 3, 1))
                .dateFin(LocalDate.of(2026, 3, 5))
                .description("Vidange")
                .build();
    }

    @Override
    protected Maintenance invalid() {
        // la date de fin est avant la date de début
        return Maintenance.builder()
                .dateDebut(LocalDate.of(2026, 3, 5))
                .dateFin(LocalDate.of(2026, 3, 1))
                .description("Vidange")
                .build();
    }

    @Override
    protected Maintenance validWithId() {
        Maintenance maintenance = valid();
        maintenance.setIdMaintenance(ID);
        return maintenance;
    }
}