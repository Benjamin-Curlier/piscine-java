---
id: 5-8-dates-et-heures
sidebar_position: 8
title: "Dates et heures (java.time)"
description: "Manipuler des dates et des heures avec l'API moderne java.time : LocalDate/LocalTime/LocalDateTime, Period vs Duration, parsing et formatage avec DateTimeFormatter, immuabilité et thread-safety."
---

# Dates et heures (java.time)

## Pourquoi ce chapitre

Tôt ou tard, votre code manipulera des **dates** et des **heures** : l'horodatage d'une ligne de journal, une échéance à respecter, la durée écoulée entre deux événements. Avant Java 8, cette tâche reposait sur `java.util.Date` et `java.util.Calendar` — des classes **mutables**, aux mois numérotés à partir de 0, et non *thread-safe* (en anglais : pas sûres en accès concurrent). Source intarissable de bugs.

Depuis Java 8, l'API **`java.time`** (parfois appelée JSR-310) remplace tout cela par des types **clairs**, **immuables** et **thread-safe**. C'est l'API qu'on utilise partout aujourd'hui. Ce chapitre clôt le module : il s'appuie sur la gestion d'exceptions (chapitre 5-2) pour traiter les dates mal formées, et complète votre boîte à outils pour les exercices de la Piscine.

## Ce que vous saurez faire à la fin

- **Choisir** le bon type : `LocalDate`, `LocalTime`, `LocalDateTime`.
- **Créer** une date avec `now()` et `of(...)`, et **lire** ses composantes (`getYear`, `getDayOfWeek`…).
- **Calculer** sur des dates avec `plusX` / `minusX` en tenant compte de l'**immuabilité**.
- **Distinguer** `Period` (durée en années/mois/jours) de `Duration` (durée en heures/minutes/secondes).
- **Parser** et **formater** une date avec `DateTimeFormatter.ofPattern(...)`.
- **Expliquer** pourquoi `java.time` et `DateTimeFormatter` sont **immuables et thread-safe**, contrairement à `SimpleDateFormat`.

## 1. Les types principaux

L'API `java.time` propose un type par besoin. Le mot **`Local`** signifie « sans fuseau horaire » : c'est ce que vous utiliserez dans la quasi-totalité des exercices.

| Type | Représente | Exemple |
|------|-----------|---------|
| `LocalDate` | une date seule (sans heure) | `2026-06-17` |
| `LocalTime` | une heure seule (sans date) | `14:30` |
| `LocalDateTime` | une date **et** une heure | `2026-06-17T14:30` |
| `Period` | une durée en **années/mois/jours** | `P1M15D` (1 mois 15 jours) |
| `Duration` | une durée en **heures/minutes/secondes** | `PT2H30M` (2 h 30 min) |

Tous ces types vivent dans le paquet `java.time` ; `Period` et `Duration` aussi. Le format `P1M15D` ou `PT2H30M` est la notation **ISO 8601** des durées (le `T` sépare la partie date de la partie heure).

### À retenir

> - `LocalDate` = date seule, `LocalTime` = heure seule, `LocalDateTime` = les deux.
> - `Period` mesure une durée **en dates** (jours/mois/années) ; `Duration` mesure une durée **en temps** (heures/minutes/secondes).
> - Tout est dans `java.time` ; chaque symbole utilisé doit être importé.

## 2. Créer et lire une date

On crée une date soit avec `now()` (l'instant présent, lu sur l'horloge système), soit avec `of(...)` (une date précise que vous fournissez).

Attention : avec `LocalDate.of`, le **mois va de 1 à 12** (janvier = 1), contrairement à l'ancien `Calendar` où janvier valait 0. Vous pouvez aussi passer une constante de l'énumération `Month` pour plus de lisibilité.

### Exemple

```java compile
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.DayOfWeek;

public class CreerDates {

    public static void main(String[] args) {

        // now() : lit l'horloge système
        LocalDate aujourdhui = LocalDate.now();
        LocalTime maintenant = LocalTime.now();
        LocalDateTime instant = LocalDateTime.now();

        // of(...) : une date précise (mois de 1 à 12, janvier = 1)
        LocalDate noel = LocalDate.of(2026, 12, 25);
        LocalTime midi = LocalTime.of(12, 0);
        LocalDateTime rdv = LocalDateTime.of(2026, 6, 17, 14, 30);

        // Lire les composantes d'une date
        int annee = noel.getYear();              // 2026
        int jour = noel.getDayOfMonth();         // 25
        DayOfWeek jourSemaine = noel.getDayOfWeek(); // FRIDAY

        System.out.println("Noël 2026 tombe un " + jourSemaine); // FRIDAY
        System.out.println("Rendez-vous : " + rdv);              // 2026-06-17T14:30
    }
}
```

### À retenir

> - `now()` lit l'instant présent ; `of(...)` construit une date précise.
> - Avec `of`, **le mois va de 1 à 12** (janvier = 1), pas de 0 comme dans l'ancien `Calendar`.
> - `getYear()`, `getDayOfMonth()`, `getDayOfWeek()` lisent les composantes sans modifier l'objet.

## 3. Calculer sur les dates (et l'immuabilité)

Les méthodes `plusDays`, `minusWeeks`, `plusMonths`, etc. décalent une date. Mais comme `String`, un objet `java.time` est **immuable** : il ne change **jamais**. Chaque méthode renvoie un **nouvel** objet et laisse l'original intact.

C'est le piège le plus fréquent : si vous ignorez la valeur de retour, votre date n'a pas bougé.

### Exemple

```java
import java.time.LocalDate;

public class CalculerDates {

    public static void main(String[] args) {

        LocalDate depart = LocalDate.of(2026, 1, 1);

        // PIÈGE : le résultat est ignoré, depart vaut TOUJOURS le 1er janvier
        depart.plusDays(10);
        System.out.println(depart); // 2026-01-01 (inchangé !)

        // CORRECT : on réaffecte (ou on stocke dans une nouvelle variable)
        LocalDate dansDixJours = depart.plusDays(10);
        System.out.println(dansDixJours); // 2026-01-11

        // Les calculs s'enchaînent (chaque appel renvoie un nouvel objet)
        LocalDate echeance = depart.plusMonths(1).minusDays(1);
        System.out.println(echeance); // 2026-01-31
    }
}
```

### À retenir

> - Les types `java.time` sont **immuables** : `plusDays`, `minusMonths`… renvoient un **nouvel** objet.
> - **Réaffectez** toujours le résultat (`d = d.plusDays(10)`) ou stockez-le dans une nouvelle variable.
> - Les appels se chaînent : `depart.plusMonths(1).minusDays(1)`.

## 4. Period vs Duration : mesurer un écart

Pour mesurer la distance entre deux dates ou deux instants, deux outils existent — ne les confondez pas.

- **`Period`** mesure une durée **basée sur le calendrier** : années, mois, jours. On l'obtient avec `Period.between(dateDebut, dateFin)` sur des `LocalDate`.
- **`Duration`** mesure une durée **basée sur le temps** : heures, minutes, secondes, nanosecondes. On l'obtient avec `Duration.between(debut, fin)` sur des `LocalTime` ou des `LocalDateTime`.

Pour un simple nombre de jours (ou de mois, d'heures…) entre deux dates, `ChronoUnit` est souvent plus direct : `ChronoUnit.DAYS.between(a, b)` renvoie un `long`.

### Exemple

```java
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class EcartsDeDates {

    public static void main(String[] args) {

        LocalDate debut = LocalDate.of(2026, 1, 1);
        LocalDate fin = LocalDate.of(2026, 6, 17);

        // Period : écart calendaire (mois + jours)
        Period periode = Period.between(debut, fin);
        System.out.println(periode.getMonths() + " mois, "
                + periode.getDays() + " jours"); // 5 mois, 16 jours

        // ChronoUnit : un nombre brut de jours
        long nbJours = ChronoUnit.DAYS.between(debut, fin);
        System.out.println("Soit " + nbJours + " jours"); // 167 jours

        // Duration : écart de temps (sur des heures)
        LocalTime ouverture = LocalTime.of(9, 0);
        LocalTime fermeture = LocalTime.of(17, 30);
        Duration journee = Duration.between(ouverture, fermeture);
        System.out.println(journee.toHours() + " h "
                + journee.toMinutesPart() + " min"); // 8 h 30 min
    }
}
```

### À retenir

> - `Period` = durée **en dates** (années/mois/jours), via `Period.between` sur des `LocalDate`.
> - `Duration` = durée **en temps** (heures/minutes/secondes), via `Duration.between` sur des `LocalTime`/`LocalDateTime`.
> - `ChronoUnit.DAYS.between(a, b)` (ou `HOURS`, `MINUTES`…) renvoie directement un `long` — pratique pour un simple décompte.

## 5. Parser et formater avec DateTimeFormatter

Convertir un texte en date (**parser**) ou une date en texte (**formater**) passe par la classe `DateTimeFormatter`.

Par défaut, `LocalDate.parse(...)` attend le format **ISO 8601** (`aaaa-MM-jj`, par exemple `2026-06-17`). Pour un autre format, on crée un `DateTimeFormatter` avec `ofPattern(...)`. Les lettres de motif sont **sensibles à la casse** : `MM` = mois, `mm` = minutes ; `yyyy` = année, `dd` = jour du mois.

Un texte qui ne correspond pas au motif lève une `DateTimeParseException` (une *unchecked* : on n'est pas obligé de la déclarer, mais on la rattrape pour donner un message clair — voir chapitre 5-2).

### Exemple

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FormaterDates {

    public static void main(String[] args) {

        // Texte ISO → date (format par défaut aaaa-MM-jj)
        LocalDate d = LocalDate.parse("2026-06-17");

        // Date → texte avec un motif personnalisé
        DateTimeFormatter fr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String texte = d.format(fr);
        System.out.println(texte); // 17/06/2026

        // Texte personnalisé → date : le motif doit correspondre EXACTEMENT
        LocalDate relue = LocalDate.parse("25/12/2026", fr);
        System.out.println(relue); // 2026-12-25

        // Un texte mal formé lève DateTimeParseException : on le rattrape
        try {
            LocalDate.parse("17-06-2026", fr); // ❌ tirets au lieu de slashs
        } catch (DateTimeParseException e) {
            System.out.println("Date invalide : " + e.getMessage());
        }
    }
}
```

### Pourquoi `java.time` est immuable et thread-safe

Comme tous les types `java.time`, **`DateTimeFormatter` est immuable et thread-safe**. Vous pouvez créer un formateur une seule fois (par exemple dans une constante `static final`) et le partager entre plusieurs threads sans risque.

C'est une rupture nette avec l'ancien `java.text.SimpleDateFormat`, qui était **mutable** et **non thread-safe** : partagé entre threads, il produisait silencieusement des dates fausses. C'est l'une des raisons majeures de préférer `java.time` à l'ancienne API.

### À retenir

> - `LocalDate.parse(texte)` attend le format **ISO** `aaaa-MM-jj` par défaut.
> - `DateTimeFormatter.ofPattern("dd/MM/yyyy")` définit un motif personnalisé pour `parse` et `format`.
> - Les lettres de motif sont **sensibles à la casse** : `MM` (mois) ≠ `mm` (minutes), `yyyy` (année) ≠ `dd` (jour).
> - `DateTimeFormatter` est **immuable et thread-safe** — contrairement à `SimpleDateFormat`, qu'on n'utilise plus.

## Erreurs fréquentes

- **Ignorer le résultat d'un calcul** : écrire `d.plusDays(10);` sans réaffecter laisse `d` inchangé. Cause : les objets `java.time` sont immuables, la méthode renvoie un **nouvel** objet. Correction : `d = d.plusDays(10);` ou stocker dans une nouvelle variable.

- **Croire que le mois part de 0** : par réflexe hérité de `Calendar`, écrire `LocalDate.of(2026, 0, 25)` pour janvier. Cause : dans `java.time`, **le mois va de 1 à 12** (janvier = 1) ; passer `0` lève `DateTimeException`. Correction : `LocalDate.of(2026, 1, 25)` (ou `Month.JANUARY`).

- **Confondre `MM` et `mm` dans un motif** : `ofPattern("dd/mm/yyyy")` produit `17/30/2026` (les minutes de l'heure courante au lieu du mois). Cause : les lettres de motif sont sensibles à la casse — `MM` = mois, `mm` = minutes. Correction : utiliser `MM` pour le mois.

- **Motif qui ne correspond pas au texte** : `LocalDate.parse("17-06-2026", DateTimeFormatter.ofPattern("dd/MM/yyyy"))` lève `DateTimeParseException` car le texte utilise des tirets et le motif des slashs. Cause : `parse` exige une correspondance **exacte**. Correction : aligner le motif sur le texte d'entrée, et rattraper `DateTimeParseException` si l'entrée n'est pas maîtrisée.

- **Confondre `Period` et `Duration`** : appeler `Duration.between` sur deux `LocalDate` (qui n'ont pas de composante horaire) échoue à la compilation, ou `Period.between` sur des `LocalTime`. Cause : `Period` est calendaire (dates), `Duration` temporel (heures). Correction : `Period` pour des `LocalDate`, `Duration` pour des `LocalTime`/`LocalDateTime`.

- **Réutiliser un `SimpleDateFormat` entre threads** : partager une instance de l'ancien `SimpleDateFormat` produit des dates corrompues sous charge. Cause : `SimpleDateFormat` est mutable et non thread-safe. Correction : utiliser `DateTimeFormatter` (immuable, thread-safe), partageable sans crainte.

## Exercice guidé

**Contexte** : vous écrivez un petit utilitaire d'échéances. À partir d'une date de départ fournie au format `jj/MM/aaaa`, vous calculez la date d'échéance (30 jours plus tard) et le nombre de jours qui séparent les deux dates.

**Objectif** : parser la date, calculer l'échéance, mesurer l'écart, et tout réafficher au format français.

**Pas à pas :**

1. Créez un `DateTimeFormatter` avec le motif `"dd/MM/yyyy"` (constante `static final`, puisqu'il est immuable et réutilisable).

2. Parsez la chaîne `"01/01/2026"` en `LocalDate` avec ce formateur.

3. Calculez la date d'échéance en ajoutant 30 jours avec `plusDays(30)` (pensez à **réaffecter** ou à stocker le résultat).

4. Mesurez l'écart en jours avec `ChronoUnit.DAYS.between(...)` (ce sera 30).

5. Affichez la date de départ et l'échéance **formatées** avec le même formateur, ainsi que l'écart.

<details>
<summary>Solution (à n'ouvrir qu'après avoir essayé)</summary>

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CalculateurEcheance {

    // DateTimeFormatter est immuable et thread-safe : on le partage sans risque
    private static final DateTimeFormatter FORMAT_FR =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {

        String saisie = "01/01/2026";

        try {
            // Étape 2 : texte → LocalDate avec le motif personnalisé
            LocalDate depart = LocalDate.parse(saisie, FORMAT_FR);

            // Étape 3 : +30 jours (on STOCKE le résultat, immuabilité oblige)
            LocalDate echeance = depart.plusDays(30);

            // Étape 4 : écart en jours
            long jours = ChronoUnit.DAYS.between(depart, echeance);

            // Étape 5 : réaffichage formaté
            System.out.println("Départ   : " + depart.format(FORMAT_FR));   // 01/01/2026
            System.out.println("Échéance : " + echeance.format(FORMAT_FR)); // 31/01/2026
            System.out.println("Écart    : " + jours + " jours");          // 30 jours
        } catch (DateTimeParseException e) {
            // l'entrée ne correspond pas au motif jj/MM/aaaa
            System.out.println("Date invalide : " + e.getMessage());
        }
    }
}
```

Sortie attendue :
```text
Départ   : 01/01/2026
Échéance : 31/01/2026
Écart    : 30 jours
```

**Points clés** :
- Le formateur est une constante `static final` : créé une fois, réutilisé partout (possible car immuable).
- `plusDays(30)` renvoie un nouvel objet — on le stocke dans `echeance`.
- `ChronoUnit.DAYS.between` donne le nombre de jours sous forme de `long`.
- Le `try`/`catch` autour de `parse` applique le chapitre 5-2 : une entrée mal formée lève `DateTimeParseException`.

</details>

## Vérifiez vos acquis

- Quelle est la différence entre `LocalDate`, `LocalTime` et `LocalDateTime` ? Lequel choisissez-vous pour stocker une date d'anniversaire ?
- Pourquoi `d.plusDays(10);` (sans réaffectation) ne modifie-t-il pas `d` ? Que faut-il écrire à la place ?
- Quand utilisez-vous `Period` plutôt que `Duration` ? Donnez un exemple pour chacun.
- Dans un motif `DateTimeFormatter`, quelle est la différence entre `MM` et `mm` ? Et entre `yyyy` et `dd` ?
- Quelle exception est levée si le texte ne correspond pas au motif lors d'un `parse` ? À quel chapitre cela vous renvoie-t-il ?
- Pourquoi peut-on partager un `DateTimeFormatter` entre threads, alors que c'était dangereux avec `SimpleDateFormat` ?

## Pour aller plus loin

- [The Date Time API](https://dev.java/learn/date-time/) (dev.java) — tutoriel officiel Oracle sur `java.time`.
- [LocalDate](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/time/LocalDate.html) (Javadoc OpenJDK 25) — documentation complète de `LocalDate` et de ses méthodes.
- [Guide to DateTimeFormatter](https://www.baeldung.com/java-datetimeformatter) (Baeldung) — les lettres de motif et les formats prédéfinis en détail.

## Prochain chapitre

→ **[Module 6 · Chapitre 6-1 — Pourquoi tester](../module-6-tests-git/6-1-pourquoi-tester)**
