package etnc.m3;

/**
 * Exercice 3.4.4 — Evenement : hierarchie sealed (FOURNIE, ne pas modifier).
 */
public sealed interface Evenement permits Connexion, Deconnexion, Erreur {
}
