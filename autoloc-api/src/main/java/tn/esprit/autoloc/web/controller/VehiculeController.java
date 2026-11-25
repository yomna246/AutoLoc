package tn.esprit.autoloc.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.service.IVehiculeService;

import java.util.List;
@SuppressWarnings("java:S4684")

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final IVehiculeService vehiculeService;

    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @GetMapping("/{id}")
    public Vehicule findById(@PathVariable Long id) {
        return vehiculeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule vehicule) {
        return vehiculeService.create(vehicule);
    }

    @PutMapping("/{id}")
    public Vehicule update(@PathVariable Long id, @RequestBody Vehicule vehicule) {
        return vehiculeService.update(id, vehicule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        vehiculeService.deleteById(id);
    }

    @DeleteMapping("/{id}/agence")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecter(@PathVariable Long id) {
        vehiculeService.desaffecter(id);
    }

    @PutMapping("/{idVehicule}/agence/{idAgence}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecter(@PathVariable Long idVehicule, @PathVariable Long idAgence) {
        vehiculeService.affecter(idVehicule, idAgence);
    }

    @PutMapping("/{idVehicule}/equipements/{idEquipement}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterEquipement(@PathVariable Long idVehicule, @PathVariable Long idEquipement) {
        vehiculeService.affecterEquipement(idVehicule, idEquipement);
    }
}