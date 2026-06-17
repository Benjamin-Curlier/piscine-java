---
id: 7-2-persistance
sidebar_position: 2
title: "Découverte : la persistance"
description: "Un aperçu de la sauvegarde des données au-delà des fichiers : bases de données relationnelles, SQL, et comment Java leur parle (JDBC, puis JPA/Hibernate)."
---

# Découverte : la persistance

:::note Chapitre « découverte »
Comme le chapitre concurrence, c'est un **aperçu** sans exercice noté. Le but : savoir **ce qui
existe** et **où aller** pour stocker durablement des données. C'est un grand sujet de toute suite
« Java avancée ».
:::

## Le problème : la mémoire oublie tout

Vos programmes manipulent des objets en **mémoire vive**. Dès qu'ils s'arrêtent, **tout est perdu**.
Pour qu'une application se souvienne (un compte, un panier, un historique) entre deux lancements, il
faut **persister** les données quelque part.

## Option 1 : les fichiers (vous savez déjà !)

Au **module 5**, vous avez appris à lire et écrire des fichiers. C'est la persistance la plus
simple, parfaite pour de petites données (une config, un export CSV). Ses limites apparaissent vite :
chercher une donnée précise, gérer plusieurs accès en même temps, garantir la cohérence… deviennent
pénibles à coder à la main.

## Option 2 : les bases de données relationnelles + SQL

Pour des données structurées et nombreuses, on utilise une **base de données**. La plus répandue est
**relationnelle** : les données vivent dans des **tables** (lignes × colonnes), comme un tableur
costaud. On les interroge avec le langage **SQL** :

```sql
-- une table
CREATE TABLE membre (id INTEGER PRIMARY KEY, nom TEXT, niveau TEXT);

-- les 4 opérations « CRUD »
INSERT INTO membre (nom, niveau) VALUES ('Martin', 'SENIOR');   -- Create
SELECT nom FROM membre WHERE niveau = 'SENIOR';                 -- Read
UPDATE membre SET niveau = 'LEAD' WHERE nom = 'Martin';         -- Update
DELETE FROM membre WHERE nom = 'Martin';                        -- Delete
```

La base s'occupe de la recherche rapide (index), de la cohérence (contraintes) et des accès
concurrents — tout ce qui était pénible avec des fichiers.

## Comment Java parle à une base : JDBC (aperçu)

L'API standard s'appelle **JDBC**. Pour débuter, **SQLite** est idéal : la base **est un simple
fichier**, sans serveur à installer.

```java
try (Connection cx = DriverManager.getConnection("jdbc:sqlite:piscine.db");
     PreparedStatement st = cx.prepareStatement("SELECT nom FROM membre WHERE niveau = ?")) {
    st.setString(1, "SENIOR");                 // paramètre sécurisé
    try (ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("nom"));
        }
    }
}
```

:::warning Toujours des `PreparedStatement`
Ne **concaténez jamais** une valeur saisie dans une requête SQL (`"... WHERE nom = '" + saisie + "'"`).
C'est la faille **injection SQL**. Utilisez les **`?`** et `setString(...)` : la valeur est traitée
comme une donnée, jamais comme du code.
:::

## Au-dessus : les ORM (JPA / Hibernate)

Écrire du SQL à la main pour chaque objet est répétitif. Un **ORM** (*Object-Relational Mapping*)
comme **JPA/Hibernate** fait la correspondance **objet ↔ table** pour vous : vous manipulez des
objets Java, il génère le SQL. C'est le standard dans les applications **Spring Boot**.

## En résumé

- En mémoire = volatile ; **persister** = survivre aux redémarrages.
- **Fichiers** pour le simple ; **base de données relationnelle + SQL** pour le structuré et le volumineux.
- Java y accède via **JDBC** (commencez avec **SQLite**) ; toujours des **`PreparedStatement`**.
- Les **ORM** (JPA/Hibernate) automatisent la correspondance objet ↔ table.

## Pour aller plus loin

- Installez SQLite, créez une table `membre`, et faites un CRUD complet depuis Java via JDBC.
- Découvrez **Spring Data JPA** : un *repository* vous donne `findAll()`, `save()`, etc. presque gratuitement.
- C'est, avec Spring Boot et les API REST, le cœur d'une **suite avancée** à cette Piscine
  (voir **« Périmètre & la suite »** du README).
