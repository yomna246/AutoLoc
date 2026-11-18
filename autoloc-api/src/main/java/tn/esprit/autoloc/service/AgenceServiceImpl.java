package tn.esprit.autoloc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.Utils;
import tn.esprit.autoloc.calcul.VehiculeOffre;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.io.IRapport;
import tn.esprit.autoloc.io.ModeRapport;
import tn.esprit.autoloc.io.RapportFactory;
import tn.esprit.autoloc.repository.IAgenceRepository;

import java.util.List;

@Service
@Primary
public class AgenceServiceImpl extends GenericServiceImpl<Agence, Long> implements IAgenceCompleteService {

    private final IVehiculeService vehiculeService;
    private final IEmployeService employeService;
    private final RapportFactory rapportFactory;
    private final ModeRapport modeRapport;

    public AgenceServiceImpl(IAgenceRepository repository,
                             IVehiculeService vehiculeService,
                             IEmployeService employeService,
                             RapportFactory rapportFactory,
                             @Value("${autoloc.rapport.mode:TEXTE}") ModeRapport modeRapport) {
        super(repository);
        this.vehiculeService = vehiculeService;
        this.employeService = employeService;
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

    // Q12 + Q17 : tout ou rien grâce à @Transactional
    @Override
    @Transactional
    public Agence ajouterEmployes(Long idAgence, Agence agence) {
        // 1. mettre à jour d'abord l'agence
        Agence existing = update(idAgence, agence);

        // 2. utiliser le service des employés pour insérer les employés
        List<Employe> employes = agence.getEmployes();
        for (Employe employe : employes) {
            existing.addEmploye(employe);
            employeService.create(employe);
        }

        // 3. mettre à jour l'effectif avec la taille de la liste
        existing.setEffectif(employes.size());
        return repository.save(existing);
    }
}