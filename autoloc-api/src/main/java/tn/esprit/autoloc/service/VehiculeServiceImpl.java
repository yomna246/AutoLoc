package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.IOffreStrategy;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.Map;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    private final Map<String, IOffreStrategy> strategies;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    public VehiculeServiceImpl(IVehiculeRepository repository,
                               Map<String, IOffreStrategy> strategies,
                               IAgenceRepository agenceRepository,
                               IEquipementRepository equipementRepository) {
        super(repository);
        this.strategies = strategies;
        this.agenceRepository = agenceRepository;
        this.equipementRepository = equipementRepository;
    }

    @Override
    protected String getEntityName() {
        return "Vehicule";
    }

    @Override
    protected Long getId(Vehicule entity) {
        return entity.getIdVehicule();
    }

    @Override
    protected void setFields(Vehicule source, Vehicule existing) {
        existing.setImmatriculation(source.getImmatriculation());
        existing.setMarque(source.getMarque());
        existing.setModele(source.getModele());
        existing.setCategorie(source.getCategorie());
        existing.setTarifJournalier(source.getTarifJournalier());
        existing.setStatut(source.getStatut());
    }

    @Override
    public boolean check(Vehicule v, boolean isNew) {
        return Utils.isNotEmpty(v.getImmatriculation())
                && Utils.isNotEmpty(v.getMarque())
                && Utils.isNotEmpty(v.getModele())
                && v.getTarifJournalier() != null;
    }

    @Override
    public double calculTarif(Vehicule vehicule, int nbJours, VehiculeOffre offre) {
        IOffreStrategy strategie = strategies.get(offre.getBeanName());
        if (strategie == null) {
            throw new IllegalArgumentException("Offre non supportée : " + offre);
        }
        return strategie.calculer(vehicule.getTarifJournalier().doubleValue(), nbJours);
    }

    @Override
    @Transactional
    public void desaffecter(Long idVehicule) {
        findById(idVehicule).setAgence(null);
    }

    @Override
    @Transactional
    public void affecter(Long idVehicule, Long idAgence) {
        Vehicule vehicule = findById(idVehicule);
        Agence agence = agenceRepository.findById(idAgence)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", idAgence.toString()));
        vehicule.setAgence(agence);
    }

    @Override
    @Transactional
    public void affecterEquipement(Long idVehicule, Long idEquipement) {
        Vehicule vehicule = findById(idVehicule);
        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", idEquipement.toString()));
        if (!equipement.getVehicules().contains(vehicule)) {
            equipement.getVehicules().add(vehicule);
        }
    }
}