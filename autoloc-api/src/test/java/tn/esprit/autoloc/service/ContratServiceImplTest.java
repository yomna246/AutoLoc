package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

class ContratServiceImplTest extends AbstractGenericServiceTest<Contrat> {

    @Mock
    private IContratRepository repository;

    private ContratServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ContratServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Contrat, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Contrat, Long> repository() {
        return repository;
    }

    @Override
    protected Contrat valid() {
        return Contrat.builder()
                .dateSignature(LocalDate.of(2026, 1, 10))
                .montantTotal(new BigDecimal("500.00"))
                .build();
    }

    @Override
    protected Contrat invalid() {
        return Contrat.builder().build();
    }

    @Override
    protected Contrat validWithId() {
        Contrat contrat = valid();
        contrat.setIdContrat(ID);
        return contrat;
    }
}