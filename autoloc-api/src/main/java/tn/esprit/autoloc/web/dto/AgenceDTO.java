package tn.esprit.autoloc.web.dto;

public record AgenceDTO(
        Long idAgence,
        String nom,
        String ville,
        String adresse,
        String telephone,
        int effectif
) {
}