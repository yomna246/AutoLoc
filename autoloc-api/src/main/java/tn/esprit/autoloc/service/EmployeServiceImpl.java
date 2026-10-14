package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.repository.IEmployeRepository;

@Service
public class EmployeServiceImpl extends GenericServiceImpl<Employe, Long> implements IEmployeService {

    public EmployeServiceImpl(IEmployeRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Employe";
    }

    @Override
    protected Long getId(Employe entity) {
        return entity.getIdEmploye();
    }

    @Override
    protected void setFields(Employe source, Employe existing) {
        existing.setNom(source.getNom());
        existing.setPrenom(source.getPrenom());
        existing.setRole(source.getRole());
    }

    @Override
    public boolean check(Employe e, boolean isNew) {
        return Utils.isNotEmpty(e.getNom())
                && Utils.isNotEmpty(e.getPrenom())
                && e.getRole() != null;
    }
}