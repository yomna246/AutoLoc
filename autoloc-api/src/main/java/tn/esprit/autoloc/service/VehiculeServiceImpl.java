package tn.esprit.autoloc.service;

import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
public class VehiculeServiceImpl extends GenericServiceImpl<Vehicule, Long> implements IVehiculeService {

    public VehiculeServiceImpl(IVehiculeRepository repository) {
        super(repository);
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

    public double calculTarif(Vehicule vehicule, int nbJours, VehiculeOffre offre) {

        double tarifJournalier = vehicule.getTarifJournalier().doubleValue();

        if (offre == VehiculeOffre.WEEKEND) {
            tarifJournalier *= 0.8;
        }

        if (offre == VehiculeOffre.LONGUE_DUREE) {
            tarifJournalier *= 0.75;
        }

        double total = tarifJournalier * nbJours;

        if (offre == VehiculeOffre.LONGUE_DUREE && total > 2000) {
            total *= 0.95;
        }

        return total;
    }

}