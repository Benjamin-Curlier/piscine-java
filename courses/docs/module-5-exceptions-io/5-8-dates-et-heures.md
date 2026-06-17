---
id: 5-8-dates-et-heures
sidebar_position: 8
title: "Dates et heures (java.time)"
description: "Manipuler des dates et des heures avec l'API moderne java.time : LocalDate/LocalDateTime, calculs de durées, immuabilité, parsing et formatage."
---

# Dates et heures (java.time)

## Pourquoi ce chapitre

Tôt ou tard, votre code manipulera des **dates** : un horodatage dans un journal, une échéance, une
durée entre deux événements. Avant Java 8, c'était un cauchemar (`Date`, `Calendar`, des bugs de
fuseaux…). Depuis, l'API **`java.time`** offre des types **clairs**, **immuables** et sûrs. C'est
celle qu'on utilise partout aujourd'hui.

## Ce que vous saurez faire à la fin

- Choisir le bon type : `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`.
- Créer, lire et **calculer** sur des dates (ajouter des jours, mesurer un écart).
- Comprendre que ces objets sont **immuables**.
- **Parser** et **formater** une date.

## 1. Les types principaux

| Type | Représente | Exemple |
|------|-----------|---------|
| `LocalDate` | une date (sans heure) | `2026-06-17` |
| `LocalTime` | une heure (sans date) | `14:30` |
| `LocalDateTime` | date **et** heure | `2026-06-17T14:30` |
| `Instant` | un instant machine (UTC), pour horodater | `2026-06-17T12:30:00Z` |
| `Period` | une durée en **années/mois/jours** | `P1M15D` (1 mois 15 jours) |
| `Duration` | une durée en **heures/minutes/secondes** | `PT2H30M` (2 h 30) |

Tous sont dans le paquet `java.time` (`import java.time.LocalDate;`).

## 2. Créer et lire

```java
LocalDate aujourdhui = LocalDate.now();
LocalDate noel = LocalDate.of(2026, 12, 25);

int annee = noel.getYear();           // 2026
int jour  = noel.getDayOfMonth();     // 25
DayOfWeek jourSemaine = noel.getDayOfWeek();  // FRIDAY
```

## 3. Calculer

```java
LocalDate dansUneSemaine = aujourdhui.plusDays(7);
LocalDate hier           = aujourdhui.minusDays(1);

// Écart entre deux dates
long jours = java.time.temporal.ChronoUnit.DAYS.between(aujourdhui, noel);
Period ecart = Period.between(aujourdhui, noel);   // « X mois, Y jours »
```

## 4. Attention : c'est **immuable**

Comme `String`, un objet `java.time` ne change **jamais** : les méthodes renvoient un **nouvel**
objet. Le piège classique :

```java
LocalDate d = LocalDate.of(2026, 1, 1);
d.plusDays(10);                 // ❌ le résultat est ignoré : d vaut toujours le 1er janvier
d = d.plusDays(10);             // ✅ on réaffecte
```

## 5. Parser et formater

```java
// Texte → date (format ISO par défaut : aaaa-MM-jj)
LocalDate d = LocalDate.parse("2026-06-17");

// Date → texte, format personnalisé
DateTimeFormatter fr = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String texte = d.format(fr);    // "17/06/2026"
```

Un texte mal formé lève une `DateTimeParseException` — l'occasion d'appliquer le chapitre
**[5-2 try/catch](5-2-try-catch-finally.md)**.

## En résumé

- `LocalDate` / `LocalDateTime` pour les dates lisibles ; `Instant` pour horodater une machine.
- Les calculs se font avec `plusX` / `minusX`, les écarts avec `Period` ou `ChronoUnit`.
- Ces objets sont **immuables** : pensez à **réaffecter** le résultat.
- `parse` (texte → date) et `format` (date → texte) avec un `DateTimeFormatter`.

## Pour aller plus loin (optionnel)

- Affichez le nombre de jours jusqu'à votre prochain anniversaire.
- Découvrez `ZonedDateTime` (dates avec **fuseau horaire**) et `Duration.between` sur des heures.
- Comparez `Instant` (pour les logs/horodatages) et `LocalDateTime` (pour l'affichage humain).
