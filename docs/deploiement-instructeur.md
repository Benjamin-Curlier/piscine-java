# Déploiement Piscine ETNC sur un PC stagiaire

Ce guide s'adresse à l'**instructeur** qui prépare et déploie l'application sur les postes des stagiaires. Le PC stagiaire n'a **rien à pré-installer** (ni Java, ni Maven, ni internet).

## 1. Prérequis (machine de l'instructeur uniquement)

- Le dépôt Piscine ETNC cloné.
- Un **JDK 25** (Temurin recommandé). Par défaut le script prend `JAVA_HOME` ; sinon passez `-JdkPath`.
- `git` et un Maven (`mvn` système ou le wrapper `./mvnw`).

## 2. Construire le bundle

```powershell
# Windows
.\scripts\build-bundle.ps1            # utilise %JAVA_HOME%
.\scripts\build-bundle.ps1 -JdkPath "E:\java\jdk-25.0.3+9"
```
```bash
# Linux/macOS
./scripts/build-bundle.sh --jdk /opt/jdk-25
```

Résultat : `dist/piscine-etnc-stagiaire-<date>.zip` (contient le JDK portable, l'uber-jar, les exercices et un lanceur).

## 3. Déployer sur le PC stagiaire

1. Copier le ZIP sur le poste (clé USB, partage réseau).
2. Décompresser **où on veut** (Bureau, `C:\Piscine`, une clé USB…). Aucun droit administrateur requis.

## 4. Lancer

- Windows : double-cliquer **`piscine.bat`**.
- Linux/macOS : `./piscine.sh`.

Au **premier lancement**, l'espace de travail du stagiaire est créé automatiquement (`workspace/`), puis la console interactive (REPL) démarre. Les lancements suivants reprennent le même espace.

## 5. Ce que fait le stagiaire ensuite

Voir [`piscine-stagiaire.md`](piscine-stagiaire.md) (inclus dans le bundle sous `piscine/docs/`). En résumé : `submit-start 1.1` → éditer le code → `git add` / `commit` / `push origin rendu/1.1` → lecture du rapport.

## 6. Dépannage

| Symptôme | Cause probable | Solution |
|---|---|---|
| « java introuvable » / la fenêtre se ferme | bundle incomplet (dossier `jdk/` manquant) | Reconstruire le bundle, vérifier que `jdk/bin/java.exe` existe dans le ZIP. |
| L'antivirus bloque `piscine.bat` | politique locale | Autoriser le script, ou lancer la commande du `.bat` à la main dans un terminal. |
| Erreurs sur des chemins avec accents/espaces | chemin d'extraction exotique | Extraire dans un chemin simple (ex. `C:\Piscine`). |
| « Où sont mes résultats ? » | — | `workspace\.piscine\reports\<exo>.md` (et `.json`). |
| Repartir de zéro | — | Fermer la console, supprimer le dossier `workspace\`, relancer `piscine.bat`. |
| La correction est toujours OK alors que le code est faux | tests de référence absents pour cet exo | Vérifier que `piscine/exercises/.../<exo>/tests` et `tests-prives` sont bien dans le bundle. |

## 7. Limites connues (phase de test)

- Le bundle contient `tests-prives/` et `solution/` : un stagiaire curieux peut les ouvrir. Acceptable en phase de test ; l'anti-triche est prévu ultérieurement (tâche #29).
- Le style est en mode **advisory** (non bloquant) pendant la beta : un problème de style est signalé mais ne bloque pas la validation (tâche #53 pour le durcissement).
- Bundle Windows-first ; la variante `.sh` est fournie mais non durcie.
