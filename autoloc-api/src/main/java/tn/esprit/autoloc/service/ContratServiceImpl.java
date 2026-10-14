package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

@Service
public class ContratServiceImpl extends GenericServiceImpl<Contrat, Long> implements IContratService {

    public ContratServiceImpl(IContratRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Contrat";
    }

    @Override
    protected Long getId(Contrat entity) {
        return entity.getIdContrat();
    }

    @Override
    protected void setFields(Contrat source, Contrat existing) {
        existing.setDateSignature(source.getDateSignature());
        existing.setMontantTotal(source.getMontantTotal());
        existing.setValide(source.isValide());
    }

    @Override
    public boolean check(Contrat c, boolean isNew) {
        return c.getDateSignature() != null && c.getMontantTotal() != null;
    }
}