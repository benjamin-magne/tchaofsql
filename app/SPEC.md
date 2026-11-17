# tcHaoFSQL

## 1. Présentation
Logiciel permettant de transférer les données depuis une table `HFSQL Client/Serveur` vers une table `PostgreSQL`.

## 2. Périmètre
**Le logiciel doit :**
- Se connecter à une base de données `HFSQL Client/Serveur`
- Récupérer ses données
- Créer un script d'insertion en SQL
- Se connecter à une base de données `PostgreSQL`
- Exécuter le script d'insertion
- Récupérer les logs de connexion et d'insertion
  **Le logiciel ne doit pas :**
- Créer le script d'import d'une table `HFSQL Client/Serveur` vers `PostgreSQL`

## 3. Fonctionnalités

| Identifiant | Nom                      | Liste du nécessaires                                                                                                                                                                                                                                                                                                                                    |
| ----------- | ------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| F-001       | Connexion à HFSQL        | - L'utilisateur doit pouvoir renseigner les paramètres de connexion<br>- Le logiciel doit tester les paramètres et se connecter à la base.<br>- Le logiciel doit signaler les erreurs de connexion                                                                                                                                                      |
| F-002       | Lecture HFSQL            | - L'utilisateur doit pouvoir lire les colonnes des tables<br>- L'utilsateur doit pouvoir lire les données des tables                                                                                                                                                                                                                                    |
| F-003       | Récupération des données | - L'utilisateur doit pouvoir choisir les tables à importer<br>- L' utilisateur doit pouvoir choisir les colonnes à importer<br>- Le logiciel doit pouvoir récupérer les données de la ou les tables choisis                                                                                                                                             |
| F-004       | Connexion à PostgreSQL   | - L'utilisateur doit pouvoir renseigner les paramètres de connexion<br>- Le logiciel doit tester les paramètres et se connecter à la base.<br>- Le logiciel doit signaler les erreurs de connexion                                                                                                                                                      |
| F-005       | Paramètres de mapping    | - L'utilisateur doit pouvoir renseigner la database de destination<br>- L'utilisateur doit pouvoir renseigner le schéma de destination<br>- Le logiciel doit demander à l'utilisateur la table de destination pour chaque table importer<br>- Le logiciel doit demander pour chaque colonne de la table importer l'équivalence dans la table PostgreSQL |
| F-006       | Conversions des types    | - Le logiciel doit pouvoir convertir les données d'un type HFSQL vers PostgreSQL quand nécessaire<br>- Le logiciel doit signaler les conversions impossibles<br>- Le logiciel doit signaler les valeurs incompatibles                                                                                                                                   |
| F-007       | Exécution du script      | - Le logiciel doit tester son script d'insertion (avec un rollback) avant de l'exécuter<br>- Le logiciel doit pouvoir exécuter le script<br>- Le logiciel doit produire un rapport à la fin de l'importation                                                                                                                                            |
| F-008       | Configuration            | - L'utilisateur doit pouvoir consulter les valeurs globales renseignées dans l'application                                                                                                                                                                                                                                                              |
## 4. Contraintes techniques

| Technologies                                   | Bibliothèques                                         | Pilotes et composants externes                           |
| ---------------------------------------------- | ----------------------------------------------------- | -------------------------------------------------------- |
| Kotlin<br>PostgreSQL<br>HFSQL Client \ Serveur | JDBC PostgreSQL (pgJDBC)<br>Clikt<br>SLF4J<br>Logback | Pilote ODBC HFSQL (PC SOFT)<br>Pont JDBC–ODBC (Easysoft) |
