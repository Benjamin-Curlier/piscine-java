package piscine.moulinette.console.git;

/** Ref poussée lors du dernier {@code git push}. */
public record RefUpdate(String ref, String oldSha, String newSha) {}
