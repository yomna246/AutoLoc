package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.repository.IMaintenanceRepository;

@Service
public class MaintenanceServiceImpl extends GenericServiceImpl<Maintenance, Long> implements IMaintenanceService {

    public MaintenanceServiceImpl(IMaintenanceRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Maintenance";
    }

    @Override
    protected Long getId(Maintenance entity) {
        return entity.getIdMaintenance();
    }

    @Override
    protected void setFields(Maintenance source, Maintenance existing) {
        existing.setDateDebut(source.getDateDebut());
        existing.setDateFin(source.getDateFin());
        existing.setDescription(source.getDescription());
    }

    @Override
    public boolean check(Maintenance m, boolean isNew) {
        return m.getDateDebut() != null
                && Utils.isNotEmpty(m.getDescription())
                && (m.getDateFin() == null || !m.getDateFin().isBefore(m.getDateDebut()));
    }
}