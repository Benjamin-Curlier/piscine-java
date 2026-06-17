package piscine.moulinette.console.gamification;

/** Vue minimale d'un exercice pour la gamification : id canonique, module, difficulté. */
public record Exo(String id, int module, Difficulte difficulte) { }
