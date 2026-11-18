package tn.esprit.autoloc;

public class Utils {

    private Utils() {
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}