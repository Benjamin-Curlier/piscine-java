package piscine.m4;

/**
 * FOURNI — ne pas modifier.
 *
 * <p>Enregistrement immuable representant un membre.
 * anciennete = annees de service (&gt;= 0).</p>
 */
public record Membre(String nom, Niveau niveau, int anciennete) { }
