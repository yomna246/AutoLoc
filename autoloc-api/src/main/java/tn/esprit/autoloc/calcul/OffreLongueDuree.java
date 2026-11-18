package tn.esprit.autoloc.calcul;

import org.springframework.stereotype.Component;

@Component("longueDuree")
public class OffreLongueDuree implements IOffreStrategy {

    @Override
    public double calculer(double tarifJournalier, int nbJours) {
        double total = tarifJournalier * 0.75 * nbJours;
        if (total > 2000) {
            total *= 0.95;
        }
        return total;
    }
}