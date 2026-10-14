package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BasicAgenceServiceImp implements IAgenceService {
    private final IAgenceRepository repository;
    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public Agence create(Agence agence) {
        if(agence.getIdAgence() !=null){
            throw new IllegalArgumentException("id agence doit etre null!!!");
        }
        if(!check(agence,true)){
            throw new IllegalArgumentException(" certains champs sont invalides !");
        }

        return repository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return null;
    }

    @Override
    public List<Agence> findAll() {
        return List.of();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public boolean hasVehicules(Long id) {
        return false;
    }
}
