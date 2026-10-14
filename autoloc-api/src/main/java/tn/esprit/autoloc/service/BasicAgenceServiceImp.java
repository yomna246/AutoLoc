package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
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
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agence" , id.toString()));
    }

    @Override
    public List<Agence> findAll() {
        return repository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        Agence existing = findById(id);
        if(!check(agence, false)){
            throw new IllegalArgumentException("certains champs sont invalides!!");
        }
        existing.setNom(agence.getNom());
        existing.setVille(agence.getVille());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Agence", id.toString());
        }
        repository.deleteById(id);

    }

    @Override
    public boolean hasVehicules(Long id) {
        return !findById(id).getVehicules().isEmpty();
    }
}
