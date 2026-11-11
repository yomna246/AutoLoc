package tn.esprit.autoloc.io;

import org.springframework.stereotype.Component;

@Component
public class RapportFactory {

    public IRapport creer(ModeRapport mode) {
        return switch (mode) {
            case TEXTE -> new RapportTexte();
            case CONSOLE -> new RapportConsole(new RapportTexte());
        };
    }
}