package tn.esprit.autoloc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.io.IRapport;
import tn.esprit.autoloc.io.ModeRapport;
import tn.esprit.autoloc.io.RapportFactory;
import tn.esprit.autoloc.repository.IAgenceRepository;

@Service
@Primary
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceCompleteService {

    private final IVehiculeService vehiculeService;
    private final RapportFactory rapportFactory;
    private final ModeRapport modeRapport;

    public AgenceServiceImpl(IAgenceRepository repository,
                             IVehiculeService vehiculeService,
                             RapportFactory rapportFactory,
                             @Value("${autoloc.rapport.mode:TEXTE}") ModeRapport modeRapport) {
        super(repository);
        this.vehiculeService = vehiculeService;
        this.rapportFactory = rapportFactory;
        this.modeRapport = modeRapport;
    }

    @Override
    protected String getEntityName() {
        return "Agence";
    }

    @Override
    protected Long getId(Agence entity) {
        return entity.getIdAgence();
    }

    @Override
    protected void setFields(Agence source, Agence existing) {
        existing.setNom(source.getNom());
        existing.setVille(source.getVille());
        existing.setAdresse(source.getAdresse());
        existing.setTelephone(source.getTelephone());
    }

    @Override
    public boolean check(Agence agence, boolean isNew) {
        return Utils.isNotEmpty(agence.getNom()) && Utils.isNotEmpty(agence.getTelephone());
    }

    @Override
    public boolean hasVehicules(Long id) {
        return !findById(id).getVehicules().isEmpty();
    }

    @Override
    public String simuler(Agence agence, int nbJours, VehiculeOffre offre) {
        IRapport rapport = rapportFactory.creer(modeRapport);

        rapport.ecrireEntete("Id : " + agence.getIdAgence()
                + "; Nom : " + agence.getNom()
                + "; Tel : " + agence.getTelephone());

        double total = 0;
        for (Vehicule vehicule : agence.getVehicules()) {
            total += vehiculeService.calculTarif(vehicule, nbJours, offre);
        }

        rapport.ecrireTotal(total);
        return rapport.contenu();
    }
}