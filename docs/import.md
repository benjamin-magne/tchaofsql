# Format des données et règles d'import

Un import associe des valeurs textuelles XML à une table PostgreSQL existante. PostgreSQL valide les conversions et les contraintes lors de l'exécution des requêtes.

[Accueil](../README.md) · [Commandes](commandes.md) · [Architecture](developpement.md)

## Format XML

Le format attendu est un export WinDev avec des lignes `TABLE_CONTENU` sous une racine `WINDEV_TABLE`, contenant des champs simples :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<WINDEV_TABLE>
    <TABLE_CONTENU>
        <IDPERSONNE>1</IDPERSONNE>
        <NOM>Dupont</NOM>
    </TABLE_CONTENU>
    <TABLE_CONTENU>
        <IDPERSONNE>2</IDPERSONNE>
        <NOM>Martin</NOM>
    </TABLE_CONTENU>
</WINDEV_TABLE>
```

Les balises `WINDEV_TABLE`, `TABLE_CONTENU` et `N__Enr` sont ignorées comme colonnes. Les noms des champs sont utilisés tels quels. Les attributs ne sont pas transformés en colonnes et les structures XML imbriquées arbitraires ne sont pas prises en charge. Les DTD et les entités externes sont désactivées.

Le fichier doit exister, porter l'extension `.xml` et ne pas être vide. Le chargement des colonnes et celui des lignes sont distincts. Les lignes restent en mémoire : la taille de l'import doit être adaptée à la mémoire disponible.

## Correspondances et valeurs

Une correspondance relie une colonne XML sélectionnée à une colonne PostgreSQL chargée. Chaque ligne doit fournir les champs associés. Un champ absent provoque une erreur de préparation ; un champ vide reste une chaîne vide, sans conversion automatique en `NULL`.

Une colonne PostgreSQL sans correspondance peut recevoir une valeur explicite par `pg defaults add`. Cette valeur est envoyée pour chaque ligne. Les colonnes sans correspondance ni valeur explicite sont omises de l'INSERT : PostgreSQL applique son défaut ou vérifie ses contraintes. Lors d'une mise à jour, seules les colonnes envoyées, hors clé, sont modifiées.

Les valeurs sont liées aux paramètres JDBC avec le type de la colonne cible. Pour les colonnes date-heure (`TIMESTAMP` ou `TIMESTAMP_WITH_TIMEZONE` JDBC), les DateHeure WinDev au format `jj/MM/aaaa HH:mm:ss` sont converties avant l'envoi : `01/08/2026 11:50:00` devient `2026-08-01 11:50:00`. Les dates et heures impossibles sont rejetées avec le nom de la colonne. Cette règle s'applique aussi aux valeurs explicites définies par `pg defaults add`.

Les valeurs déjà au format ISO restent inchangées, de même que les colonnes texte. La source WinDev ne fournit aucun fuseau : la conversion n'en ajoute pas. Pour une cible avec fuseau, l'interprétation reste celle du pilote et de la connexion PostgreSQL. Les autres conversions sont effectuées par le pilote et PostgreSQL.

## Clé et UPSERT

`map pk add` sélectionne une seule colonne XML déjà associée. Sa valeur doit être non vide dans chaque ligne. La colonne PostgreSQL correspondante doit disposer d'une contrainte ou d'un index d'unicité compatible avec `ON CONFLICT (colonne)` ; une clé primaire simple convient. La commande ne prend pas en charge les clés composites.

Le SQL suit cette logique :

```sql
INSERT INTO "public"."personnes" ("id", "nom")
VALUES (?, ?)
ON CONFLICT ("id") DO UPDATE SET "nom" = EXCLUDED."nom";
```

Les valeurs sont paramétrées et les identifiants SQL sont entourés de guillemets. Respecte les noms exacts des colonnes. Si seule la clé est envoyée, un conflit produit `DO NOTHING`. Plusieurs lignes source portant la même clé sont traitées successivement, sans déduplication préalable.

## Transactions

| Mode | Résultat |
| --- | --- |
| Sans option ou `--rollback` | Annulation après exécution, même sans erreur |
| `--nocommitiferror` | Validation si toutes les lignes réussissent, sinon annulation globale |
| `--commitiferror` | Validation des réussites malgré des erreurs sur d'autres lignes |

Les correspondances, la clé et les lignes sont vérifiées avant l'écriture. Une erreur de préparation, comme un champ associé absent, interrompt l'import avant la boucle SQL, même avec `--commitiferror`.

Pendant l'écriture, chaque ligne dispose d'un point de sauvegarde. En cas d'erreur SQL sur cette ligne, l'application revient à ce point, signale son numéro et continue. La décision de validation ou d'annulation intervient à la fin. Une erreur globale de transaction provoque une annulation.

Le rollback exécute réellement les requêtes : il peut déclencher des verrous et des triggers. Il ne restaure pas les séquences PostgreSQL et ne garantit pas l'annulation d'effets externes déclenchés par la base.

Le bilan compte les lignes exécutées sans exception, y compris un éventuel `DO NOTHING`. Il ne distingue pas insertions et mises à jour. Une erreur de ligne produit un résultat d'échec même si le mode partiel conserve les réussites. En mode interactif, consulte le bilan affiché avant de poursuivre.
