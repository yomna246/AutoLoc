package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.repository.IReservationRepository;

import java.time.LocalDate;

class ReservationServiceImplTest extends AbstractGenericServiceTest<Reservation> {

    @Mock
    private IReservationRepository repository;

    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReservationServiceImpl(repository);
    }

    @Override
    protected GenericServiceImpl<Reservation, Long> service() {
        return service;
    }

    @Override
    protected JpaRepository<Reservation, Long> repository() {
        return repository;
    }

    @Override
    protected Reservation valid() {
        return Reservation.builder()
                .dateDebut(LocalDate.of(2026, 5, 1))
                .dateFin(LocalDate.of(2026, 5, 10))
                .statut(StatutReservation.EN_ATTENTE)
                .build();
    }

    @Override
    protected Reservation invalid() {
        // la date de fin est avant la date de début
        return Reservation.builder()
                .dateDebut(LocalDate.of(2026, 5, 10))
                .dateFin(LocalDate.of(2026, 5, 1))
                .build();
    }

    @Override
    protected Reservation validWithId() {
        Reservation reservation = valid();
        reservation.setIdReservation(ID);
        return reservation;
    }
}