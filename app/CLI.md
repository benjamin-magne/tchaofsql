# Utilisation

Java 26 est requis. Depuis la racine du dépôt :

```powershell
.\gradlew.bat :app:installDist
.\app\build\install\app\bin\app.bat
```

Sans argument, le programme ouvre une session interactive. Les données restent en
mémoire jusqu'à `exit`, Ctrl+D ou `clear`. Chaque lancement crée une nouvelle
session. `--help` fonctionne sur chaque commande.

Exemple à saisir dans la même session (adapter le fichier et les noms de colonnes) :

```text
xml file load "C:/exports/personnes.xml"
xml columns load
xml records load
xml columns show
xml records show
db config set --host localhost --port 5432 --username postgres --password secret --database demo --schema public --table personnes
db check
pg columns load
pg columns show
map columns add IDPERSONNE id
map columns add NOM nom
map pk add IDPERSONNE
map columns show
map pk show
pg execute --rollback
pg execute --nocommitiferror
exit
```

La cible de la clé doit posséder une contrainte PRIMARY KEY ou UNIQUE dans
PostgreSQL. Les valeurs sont liées avec les types JDBC des colonnes ; PostgreSQL
valide les conversions et les contraintes lors de l'exécution. Le mode rollback
permet de vérifier l'import sans conserver les lignes (les séquences PostgreSQL
peuvent néanmoins avancer).

- `pg execute` ou `pg execute --rollback` : tout annuler.
- `pg execute --nocommitiferror` : valider seulement si toutes les lignes réussissent.
- `pg execute --commitiferror` : conserver les lignes réussies malgré les erreurs.

Les options sont exclusives. Toute erreur de ligne donne un code de sortie 1.
Les colonnes sans correspondance sont omises de l'INSERT : PostgreSQL applique
son défaut ou NULL, et signale les contraintes non satisfaites. Une valeur
explicite peut être ajoutée avec `pg defaults add colonne valeur`, consultée avec
`pg defaults show` et supprimée avec `pg defaults remove colonne`.

Les branches `xml columns`, `xml records`, `pg columns` proposent `load`, `show`,
`add` et `remove`. Pour les enregistrements, `add` reçoit des paires
`colonne=valeur` (entourer chaque paire de guillemets si elle contient des espaces),
et `remove` reçoit un indice à partir de 1. Les colonnes ajoutées doivent exister
dans la source ; les suppressions ne modifient ni le fichier ni la table.

`map columns remove` reçoit le nom XML. `map pk remove` enlève la clé sélectionnée.
Un nouveau fichier, un rechargement des colonnes ou un changement de configuration
invalide les correspondances concernées. `db config set` conserve les champs omis ;
`db config show` masque le mot de passe.
