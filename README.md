# tcHaoFSQL

## Prérequis

- **JDK Java 26**, avec la variable d'environnement `JAVA_HOME` pointant vers son dossier d'installation, sans `\bin`.
- **PostgreSQL** accessible, avec une base et une table déjà créées, ainsi qu'un compte autorisé à y écrire.
- Un fichier **XML au format d'export WinDev** (`WINDEV_TABLE` / `TABLE_CONTENU`).
- Une connexion Internet pour télécharger les dépendances à la première compilation.

## Que fait l'application ?

L'application importe les données d'un fichier XML dans une table PostgreSQL. Tu associes les colonnes XML aux colonnes de la table et choisis une clé : les nouvelles lignes sont insérées et les lignes déjà présentes sont mises à jour (**UPSERT**).

## Utilisation standard

### 1. Compiler et lancer

Dans PowerShell, à la racine du projet :

```powershell
.\gradlew.bat :app:installDist # Compiler et préparer l'application
.\app\build\install\app\bin\app.bat # Ouvrir la session interactive
```

### 2. Préparer l'import

À l'invite `tcHaoFSQL >`, saisis les commandes suivantes dans l'ordre, dans la même session. Adapte les identifiants, le fichier, la table et les noms de colonnes à tes données.

L'exemple utilise [le XML fourni](app/src/test/resources/data.xml) et une table `public.personnes` contenant `id` (entier, clé primaire) et `nom` (texte), dans une base `demo`.

| Commande | Responsabilité |
| --- | --- |
| `db config set --host 127.0.0.1 --port 5432 --username postgres --password "mot_de_passe" --database demo --schema public --table personnes` | Configurer la connexion et la table |
| `db check` | Vérifier la connexion |
| `xml file load "app/src/test/resources/data.xml"` | Sélectionner le fichier XML |
| `xml columns load` | Charger les colonnes XML |
| `xml records load` | Charger les lignes XML |
| `xml records show` | Consulter les lignes |
| `pg columns load` | Charger les colonnes PostgreSQL |
| `map columns add IDPERSONNE id` | Associer l'identifiant XML à `id` |
| `map columns add NOM nom` | Associer le nom XML à `nom` |
| `map pk add IDPERSONNE` | Choisir la clé pour l'UPSERT |
| `map columns show` | Vérifier les correspondances |

### 3. Tester puis enregistrer

Toujours dans la même session :

| Commande | Responsabilité |
| --- | --- |
| `pg execute --rollback` | Tester l'import puis annuler la transaction |
| `pg execute --nocommitiferror` | Enregistrer si toutes les lignes réussissent |
| `exit` | Quitter l'application |

La configuration et les données chargées restent en mémoire pendant la session et sont perdues à la fermeture. Pour afficher l'aide, saisis `--help` ou, par exemple, `pg execute --help`.
