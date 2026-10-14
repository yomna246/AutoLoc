package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.repository.IReservationRepository;

@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation, Long> implements IReservationService {

    public ReservationServiceImpl(IReservationRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Reservation";
    }

    @Override
    protected Long getId(Reservation entity) {
        return entity.getIdReservation();
    }

    @Override
    protected void setFields(Reservation source, Reservation existing) {
        existing.setDateDebut(source.getDateDebut());
        existing.setDateFin(source.getDateFin());
        existing.setStatut(source.getStatut());
    }

    @Override
    public boolean check(Reservation r, boolean isNew) {
        return r.getDateDebut() != null
                && r.getDateFin() != null
                && !r.getDateFin().isBefore(r.getDateDebut());
    }
}