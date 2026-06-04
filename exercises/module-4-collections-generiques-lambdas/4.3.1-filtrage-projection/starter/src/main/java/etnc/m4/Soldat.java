package etnc.m4;

/**
 * FOURNI — ne pas modifier.
 *
 * <p>Enregistrement immuable representant un soldat.
 * anciennete = annees de service (&gt;= 0).</p>
 */
public record Soldat(String nom, Grade grade, int anciennete) { }
