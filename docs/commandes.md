# Référence des commandes

Ces commandes s'exécutent dans la session `tcHaoFSQL >`. Remplace les paramètres entre chevrons sans conserver les chevrons. Entoure les chemins et les valeurs contenant des espaces avec des guillemets doubles.

[Accueil](../README.md) · [Exemple complet](utilisation.md) · [Règles d'import](import.md)

## Connexion PostgreSQL

| Commande | Responsabilité |
| --- | --- |
| `db config set [options]` | Modifier les paramètres de connexion |
| `db config show` | Afficher la configuration, mot de passe masqué |
| `db check` | Valider les paramètres et tester la connexion |

| Option de `db config set` | Valeur initiale après configuration |
| --- | --- |
| `--host` | `127.0.0.1` |
| `--port` | `5432`, valeurs autorisées de 1 à 65535 |
| `--username` | `postgres` |
| `--password` | À renseigner, non vide |
| `--database` | `postgres` |
| `--schema` | `public` |
| `--table` | À renseigner |

Les options omises conservent leur valeur précédente. Les valeurs initiales s'appliquent lors de `db config set`, pas au démarrage. Une modification effective de configuration efface les colonnes PostgreSQL chargées, les valeurs explicites, les correspondances et la clé sélectionnée.

## Fichier et colonnes XML

| Commande | Responsabilité |
| --- | --- |
| `xml file load "<chemin.xml>"` | Vérifier et sélectionner le fichier |
| `xml file show` | Afficher le chemin sélectionné |
| `xml columns load` | Charger toutes les colonnes et les afficher |
| `xml columns show` | Afficher les colonnes sélectionnées |
| `xml columns add <colonne>` | Sélectionner une colonne existante du fichier |
| `xml columns remove <colonne>` | Retirer une colonne de la sélection |

Sélectionner un fichier efface les colonnes XML, les lignes, les correspondances et la clé. `xml columns load` réinitialise les correspondances et la clé. Retirer une colonne retire aussi sa correspondance et sa sélection comme clé éventuelle.

## Lignes XML

| Commande | Responsabilité |
| --- | --- |
| `xml records load` | Remplacer les lignes en mémoire par celles du fichier |
| `xml records show` | Afficher les lignes avec leurs indices |
| `xml records add <colonne=valeur> ...` | Ajouter une ligne en mémoire |
| `xml records remove <indice>` | Retirer une ligne, indices à partir de 1 |

Exemple : `xml records add IDPERSONNE=4 "NOM=Marie Dupont"`. Les colonnes doivent être sélectionnées et la ligne doit fournir les champs utilisés par les correspondances existantes. Ces opérations ne modifient pas le fichier XML.

## Colonnes et valeurs PostgreSQL

| Commande | Responsabilité |
| --- | --- |
| `pg columns load` | Charger les métadonnées de la table |
| `pg columns show` | Afficher noms, types et possibilité de NULL |
| `pg columns add <colonne>` | Charger une colonne depuis la table |
| `pg columns remove <colonne>` | Retirer une colonne de la sélection |
| `pg defaults add <colonne> "<valeur>"` | Fixer une valeur explicite sans source XML |
| `pg defaults show` | Afficher les valeurs explicites |
| `pg defaults remove <colonne>` | Retirer une valeur explicite |

`pg columns load` efface les correspondances, la clé et les valeurs explicites. Retirer une colonne supprime sa correspondance, sa valeur explicite et la clé si elle en dépend. Une valeur explicite ne peut pas être ajoutée pour une colonne déjà associée au XML.

## Correspondances et clé

| Commande | Responsabilité |
| --- | --- |
| `map columns add <colonne_xml> <colonne_pg>` | Associer deux colonnes chargées |
| `map columns show` | Afficher les correspondances |
| `map columns remove <colonne_xml>` | Retirer une correspondance |
| `map pk add <colonne_xml>` | Choisir une colonne déjà associée comme clé |
| `map pk show` | Afficher la clé choisie |
| `map pk remove` | Effacer la sélection de clé |

Associer de nouveau une colonne XML remplace sa cible. Deux colonnes XML ne peuvent pas viser la même colonne PostgreSQL. Une nouvelle correspondance retire la valeur explicite éventuellement définie pour sa cible.

## Exécution et session

| Commande | Responsabilité |
| --- | --- |
| `pg execute` | Tester puis annuler, mode par défaut |
| `pg execute --rollback` | Tester puis annuler explicitement |
| `pg execute --nocommitiferror` | Valider seulement si toutes les lignes réussissent |
| `pg execute --commitiferror` | Valider les réussites malgré des erreurs de lignes |
| `--help` | Afficher l'aide générale |
| `pg execute --help` | Afficher l'aide d'une commande |
| `clear` | Effacer la configuration et les données de session |
| `exit` | Fermer la session interactive |

Les trois options d'exécution sont exclusives. `--commitiferror` ne contourne pas une erreur de préparation ou une défaillance globale de transaction. Voir les [règles d'import](import.md).

`app.bat --help` exécute une commande puis termine le processus. Chaque lancement crée une nouvelle session : utilise le mode interactif pour un import complet. Dans ce mode, une erreur affiche un message et rend la main, sans fermer automatiquement la session.
