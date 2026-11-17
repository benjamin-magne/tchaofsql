# tcHaoFSQL

## 1. Présentation
Logiciel permettant de transférer les données depuis une table `HFSQL Client/Serveur` vers une table `PostgreSQL`.

## 2. Périmètre
**Le logiciel doit être capable de :**
- Charger un fichier XML
- Charger la liste des colonnes
- Charger la liste des valeurs d'un fichier
- Se connecter à une table d'un schéma d'une base de données PostgreSQL **uniquement**.
- Récupérer le nom, la nul-labilité et le type de chaque colonne de la table.
- De mapper chaque colonne XML trouvée à une colonne PgSQL.
- De mapper en conséquence chaque valeur d'un fichier à sa colonne PgSQL.
- De vérifier si une colonne nullable de PgSQL n'a pas été mappé, dans quel cas proposer une valeurs par défauts
- Vérifier la cohérence des valeurs (vérifier la possibilité de conversion).
- Définir la colonne XML correspondant à la colonne PgSQL de clé primaire.
- D'exécuter une requête UPSERT sur la table.
- // en rollback.
- De proposer une interface CLI permettant d'utiliser l'app.

## 3. Première traduction du périmètre

| Périmètre défini                                                                                               | Ce que cela prévoit de plus                                                                                                                                                                                       |
| -------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Charger un fichier XML                                                                                         | - Vérifier l'existence du fichier<br>- Vérifier l'extension du fichier<br>- Vérifier le contenu du fichier                                                                                                        |
| Charger la liste des colonnes                                                                                  | - Éliminer les colonnes ne présentant pas de valeur<br>- Enregistrer le résultat<br>- Pouvoir modifier le résultat (ajouter, modifier, supprimer)                                                                 |
| Charger la liste des valeurs d'un fichier                                                                      | - Enregistrer le résultat                                                                                                                                                                                         |
| Se connecter à une table d'un schéma d'une base de données PostgreSQL **uniquement**                           | - Enregistrer la configuration<br>- Tester une connexion                                                                                                                                                          |
| Récupérer le nom, la nul-labilité et le type de chaque colonne de la table                                     | - Enregistrer le résultat                                                                                                                                                                                         |
| De mapper chaque colonne XML trouvée à une colonne PgSQL                                                       | - Enregistrer le résultat                                                                                                                                                                                         |
| De mapper en conséquence chaque valeur d'un fichier à sa colonne PgSQL                                         | - Enregistrer le résultat                                                                                                                                                                                         |
| De vérifier si une colonne nullable de PgSQL n'a pas été mappé, dans quel cas proposer une valeurs par défauts | - Recherche de correspondance entre les enregistrements mappés et les colonnes de la table<br>- Proposer à l'utilisateur de saisir une valeur par défaut<br>- - Vérifier la posibilité de conversion de la valeur |
| Vérifier la cohérence des valeurs (vérifier la possibilité de conversion)                                      |                                                                                                                                                                                                                   |
| Définir la colonne XML correspondant à la colonne PgSQL de clé primaire                                        | - Enregistrer le résultat                                                                                                                                                                                         |
| D'exécuter une requête UPSERT sur la table                                                                     | - Prendre en compte la colonne XML de clé primaire                                                                                                                                                                |
| // en rollback                                                                                                 |                                                                                                                                                                                                                   |
| De proposer une interface CLI permettant d'utiliser l'app                                                      | - PERSISTANCE DES DONNEES                                                                                                                                                                                         |

## 4. Réponse à la première traduction
### Persistance des données

| Problème                                                   | Solution                                                                                  |
| ---------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| Comment persister les données dans une seule session CLI ? | - SLT-01 : Une (ou plusieurs) data class représentant les différentes données à persister |
### Interface CLI

| Problème                                                                                                   | Solution       |
| ---------------------------------------------------------------------------------------------------------- | -------------- |
| Comment créer une interface CLI avec une arborescence de commande utilisant les fonctionnalités de l'app ? |  SLT-01: Clikt |
## 5. Deuxième traduction : architecture du projet
### Arborescence du CLI : `tchaofsql`
> NB : la commande `clear` aussi.
#### xml
##### file
###### load
###### show
##### columns
###### load
###### show
###### add
###### remove
##### records
###### load
###### show
###### add
###### remove
#### db
##### config
###### show
##### check

#### pg
##### columns
###### load
###### show
###### add
###### remove
##### execute
###### --commitiferror
###### --nocommitiferror
###### --rollback
#### map
##### columns
###### show
###### add
###### remove
##### pk
###### show
###### add
###### remove
### Interfaces

| Interface                    | Contrat                                                                                                                                                                                                                                  |
| ---------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| I-01 : IFileLoaderService    | - Vérifier l'existence d'un fichier (public)<br>- Vérifier l'extension du fichier (privé)<br>- Vérifier le contenu du fichier (privé)                                                                                                    |
| I-02 : IXmlReaderService     | - Charger la liste des colonnes (public)<br>- Charger la liste des valeurs (public)<br>- Éliminer les colonnes inutiles (privé)                                                                                                          |
| I-03 : IDatabaseService      | - Tester la connexion à une base de donnée PostgreSQL (public)<br>- Se connecter à une base de donnée PostgreSQL (public)<br>- Vérifier la configuration donnée (privé)                                                                  |
| I-04 : IPostgreReaderService | - Récupérer les informations des colonnes d'une table PostgreSQL (public)                                                                                                                                                                |
| I-05 : IPostgreWriterService | - Exécuter une requête UPSERT en commit (public)<br>- Exécuter une requête USERT en rollback (privé)                                                                                                                                     |
| I-06 :IMapperService         | - Associé une colonne XML à une colonne PostgreSQL(public)<br>- Associé une valeur XML à une colonne PostgreSQL (public)<br>- Associé une colonne XML à une colonne clé primaire (public)<br>- Vérifier l'existence des colonnes (privé) |
| I-07 : IConverterService     | - Convertir une valeur XML vers le type demandé par une colonne PostgreSQL (public)                                                                                                                                                      |
| I-08 : ISqlWriter            | - Écris la requête SQL d'UPSERT à destination de la table PostgreSQL (public)                                                                                                                                                            |
| I-09 : ISessionService       | - Embarque une instance de l'objet contenant les persistances (privé)<br>- Donne accès aux getter et setter de l'objet (public)<br>- Permet de consulter les valeurs des attributs de l'objet (public)                                   |
