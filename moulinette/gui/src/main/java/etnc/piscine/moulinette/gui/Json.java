package etnc.piscine.moulinette.gui;

/**
 * Utilitaire JSON volontairement minimal (même politique que ReportGenerator :
 * pas de dépendance tant que le format reste trivial — migration Jackson prévue #55).
 */
final class Json {

    private Json() {}

    /** Échappe une chaîne pour l'inclure entre guillemets dans un document JSON. */
    static String escape(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 8);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Extrait la valeur chaîne d'un champ d'un objet JSON plat ({@code {"cmd":"..."}}).
     * Suffisant pour l'API terminal ; rend {@code null} si le champ est absent.
     */
    static String stringField(String json, String field) {
        String needle = "\"" + field + "\"";
        int i = json.indexOf(needle);
        if (i < 0) return null;
        int colon = json.indexOf(':', i + needle.length());
        if (colon < 0) return null;
        int start = json.indexOf('"', colon + 1);
        if (start < 0) return null;
        StringBuilder sb = new StringBuilder();
        for (int p = start + 1; p < json.length(); p++) {
            char c = json.charAt(p);
            if (c == '\\' && p + 1 < json.length()) {
                char n = json.charAt(++p);
                switch (n) {
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'u' -> {
                        sb.append((char) Integer.parseInt(json, p + 1, p + 5, 16));
                        p += 4;
                    }
                    default -> sb.append(n); // \" \\ \/
                }
            } else if (c == '"') {
                return sb.toString();
            } else {
                sb.append(c);
            }
        }
        return null;
    }
}
