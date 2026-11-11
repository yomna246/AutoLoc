package tn.esprit.autoloc.io;

public class RapportTexte implements IRapport {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public void ecrireEntete(String entete) {
        sb.append(entete).append("\n");
    }

    @Override
    public void ecrireTotal(double total) {
        sb.append("Total : ").append(total).append("\n");
    }

    @Override
    public String contenu() {
        return sb.toString();
    }
}