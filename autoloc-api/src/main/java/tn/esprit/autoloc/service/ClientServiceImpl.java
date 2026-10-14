package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;

@Service
public class ClientServiceImpl extends GenericServiceImpl<Client, Long> implements IClientService {

    public ClientServiceImpl(IClientRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Client";
    }

    @Override
    protected Long getId(Client entity) {
        return entity.getIdClient();
    }

    @Override
    protected void setFields(Client source, Client existing) {
        existing.setNom(source.getNom());
        existing.setPrenom(source.getPrenom());
        existing.setEmail(source.getEmail());
        existing.setTelephone(source.getTelephone());
        existing.setNumPermis(source.getNumPermis());
        existing.setDateInscription(source.getDateInscription());
    }

    @Override
    public boolean check(Client c, boolean isNew) {
        return Utils.isNotEmpty(c.getNom())
                && Utils.isNotEmpty(c.getPrenom())
                && Utils.isNotEmpty(c.getEmail())
                && Utils.isNotEmpty(c.getNumPermis());
    }
}