# Réaliser un import XML vers PostgreSQL

Ce parcours charge trois personnes, teste leur import puis enregistre les données. Exécute toutes les commandes de préparation dans la même session interactive.

[Accueil](../README.md) · [Installation](installation.md) · [Commandes](commandes.md)

## Préparer la table et le fichier

Dans une base PostgreSQL appelée `demo`, exécute ce SQL avec ton outil habituel, par exemple pgAdmin ou psql :

```sql
CREATE TABLE public.personnes (
    id integer PRIMARY KEY,
    nom text NOT NULL
);
```

Télécharge [data.xml](../app/src/test/resources/data.xml) et place-le à côté de `bin` et `lib` dans la release extraite. Le XML d'exemple n'est pas inclus dans le ZIP. Il contient Dupont, Martin et Benjamin, avec les identifiants 1, 2 et 3.

## Ouvrir la session

Dans PowerShell, depuis le dossier contenant `bin` :

```powershell
.\bin\app.bat # Ouvrir la session interactive
```

Les commandes suivantes se saisissent à l'invite `tcHaoFSQL >`. Copie uniquement la commande et adapte les identifiants à ton serveur.

## Charger et associer les données

| Commande | Responsabilité |
| --- | --- |
| `db config set --host 127.0.0.1 --port 5432 --username postgres --password "mot_de_passe" --database demo --schema public --table personnes` | Configurer la connexion et la cible |
| `db config show` | Vérifier la configuration |
| `db check` | Tester la connexion |
| `xml file load "data.xml"` | Sélectionner le fichier |
| `xml columns load` | Charger les colonnes XML |
| `xml records load` | Charger les lignes XML |
| `xml records show` | Consulter les lignes |
| `pg columns load` | Charger les colonnes PostgreSQL |
| `map columns add IDPERSONNE id` | Associer les identifiants |
| `map columns add NOM nom` | Associer les noms |
| `map pk add IDPERSONNE` | Choisir la clé de conflit |
| `map columns show` | Vérifier les correspondances |

Charge les colonnes avant de définir les correspondances : un nouveau chargement des colonnes réinitialise les correspondances et la clé.

## Tester puis enregistrer

| Commande | Responsabilité |
| --- | --- |
| `pg execute --rollback` | Tester puis annuler la transaction |
| `pg execute --nocommitiferror` | Enregistrer si toutes les lignes réussissent |

Le test exécute réellement les requêtes dans PostgreSQL avant d'annuler la transaction. Le bilan attendu est **3 réussites, 0 erreur et une transaction annulée**. Lance ensuite la seconde commande pour conserver les données. Voir les [règles de transaction](import.md#transactions) pour les effets du rollback.

Vérifie le résultat dans ton outil PostgreSQL :

```sql
SELECT id, nom FROM public.personnes ORDER BY id;
```

| id | nom |
| --- | --- |
| 1 | Dupont |
| 2 | Martin |
| 3 | Benjamin |

Un nouvel import avec ces identifiants met à jour les noms existants. Les autres lignes de la table ne sont pas supprimées.

## Corriger et terminer

En cas d'erreur, corrige les données ou les correspondances, puis relance le test. `xml records remove 2` retire par exemple la deuxième ligne de la session, sans réécrire le XML.

| Commande | Responsabilité |
| --- | --- |
| `pg execute --help` | Consulter les modes d'import |
| `clear` | Réinitialiser toute la session |
| `exit` | Quitter l'application |

La fermeture perd les paramètres, les correspondances et les lignes en mémoire. Les données validées dans PostgreSQL restent enregistrées.
