# Installation de tcHaoFSQL

La release contient l'application et ses bibliothèques. Pour l'utiliser sous Windows, configure Java 26, extrais le ZIP et lance le script dans PowerShell.

[Accueil](../README.md) · [Premier import](utilisation.md)

## Prérequis

| Élément | Attendu |
| --- | --- |
| Java | JDK 26 avec `JAVA_HOME` configuré |
| PostgreSQL | Serveur accessible, base et table déjà créées |
| Compte PostgreSQL | Connexion autorisée et droits nécessaires à l'UPSERT sur la table |
| Fichier source | XML d'export WinDev, voir le [format attendu](import.md#format-xml) |

Gradle et le code source ne sont pas nécessaires pour lancer une release. Java n'est pas inclus dans le ZIP.

## Configurer Java 26

Installe un JDK 26 et repère son dossier : il doit contenir `bin\java.exe`. Remplace `C:\Java\jdk-26` dans les exemples par ce dossier.

### Interface Windows

1. Cherche **Modifier les variables d'environnement pour votre compte** dans le menu Démarrer.
2. Dans **Variables utilisateur**, crée ou modifie `JAVA_HOME`.
3. Indique le dossier du JDK 26, sans guillemets et sans ajouter `\bin`.
4. Valide toutes les fenêtres avec **OK**.
5. Ferme puis relance les terminaux et IntelliJ IDEA.

### PowerShell

```powershell
$jdkPath = 'C:\Java\jdk-26' # Adapter au dossier installé
[Environment]::SetEnvironmentVariable('JAVA_HOME', $jdkPath, 'User') # Enregistrer pour ton compte
$env:JAVA_HOME = $jdkPath # Appliquer au terminal actuel
```

La valeur enregistrée sera récupérée par les applications relancées. Pour utiliser aussi la commande `java` seule, ajoute `%JAVA_HOME%\bin` au **Path** utilisateur via l'interface Windows.

### Vérification

```powershell
$env:JAVA_HOME # Afficher le dossier configuré
& "$env:JAVA_HOME\bin\java.exe" -version # Vérifier la version 26
```

Dans **cmd**, utilise `echo %JAVA_HOME%` pour afficher la variable.

## Télécharger et lancer

1. Ouvre les [releases](https://github.com/benjamin-magne/tchaofsql/releases).
2. Télécharge **app.zip** dans **Assets** et extrais tout le fichier.
3. Ouvre PowerShell dans le dossier `app` extrait, qui contient `bin` et `lib`.

```powershell
.\bin\app.bat --help # Vérifier le lancement
.\bin\app.bat # Ouvrir la session interactive
```

Conserve `bin` et `lib` ensemble. Les chemins relatifs partent du dossier où tu lances l'application. Pour changer de version, extrais la nouvelle release dans un autre dossier et utilise son script.

## Dépannage

| Symptôme | Action |
| --- | --- |
| `JAVA_HOME is not set` ou dossier invalide | Vérifier le dossier du JDK et relancer le terminal |
| `Unrecognized option: --enable-native-access=ALL-UNNAMED` | Remplacer l'ancien Java utilisé par Java 26 |
| `UnsupportedClassVersionError` | Vérifier que le script utilise Java 26 |
| Script introuvable | Ouvrir PowerShell dans le dossier contenant `bin` et `lib` |
| Classe ou bibliothèque introuvable | Extraire tout le ZIP et conserver `lib` à côté de `bin` |
| Connexion PostgreSQL refusée | Vérifier adresse, port, serveur et règles d'accès |
| Authentification refusée | Vérifier le compte et le mot de passe |

Poursuivre avec le [premier import](utilisation.md).
