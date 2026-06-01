package etnc.piscine.moulinette.console;

/**
 * Mode de fonctionnement de la console.
 *
 * <p>{@link #LOCAL} : workspace stagiaire local, remote bare en file://. C'est le mode du MVP.
 * <p>{@link #NOMINAL} : déploiement plateforme (auth, rendu serveur). Non implémenté au MVP.
 */
public enum Mode { LOCAL, NOMINAL }
