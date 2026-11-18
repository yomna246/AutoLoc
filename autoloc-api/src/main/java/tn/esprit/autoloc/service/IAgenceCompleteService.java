package tn.esprit.autoloc.service;
import tn.esprit.autoloc.domain.Agence;
/** Réunit les opérations des agences et la simulation dans un seul contrat. */
public interface IAgenceCompleteService extends IAgenceService, ISimulationService {
    Agence ajouterEmployes(Long idAgence, Agence agence);
}