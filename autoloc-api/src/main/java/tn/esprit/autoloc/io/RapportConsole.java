package tn.esprit.autoloc.io;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class RapportConsole implements IRapport {

    @Delegate
    private final IRapport rapport;

    @Override
    public void ecrireEntete(String entete) {
        System.out.println(entete);
    }

    @Override
    public void ecrireTotal(double total) { rapport.ecrireTotal(total); }

    @Override
    public String contenu() { return rapport.contenu(); }
}