package piscine.moulinette.console.gamification;

import java.util.List;

/**
 * Profil de jeu calculé pour un stagiaire.
 *
 * @param xp                  total d'XP accumulé
 * @param niveau              niveau courant (1 = Débutant)
 * @param titre               libellé du niveau ("Apprenti", "Maître", …)
 * @param xpProchainNiveau    XP nécessaire pour atteindre le niveau suivant ({@code -1} si niveau max)
 * @param progressionPourcent part des exercices validés, en %
 * @param badges              badges débloqués
 */
public record Profil(int xp, int niveau, String titre, int xpProchainNiveau,
                     int progressionPourcent, List<Badge> badges) { }
