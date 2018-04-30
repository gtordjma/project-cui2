# project-cui2

Sujet: Programmer une application web de messagerie sociale, inspirée du site Twitter.

## Installation :

Clonez le projet et l'ouvrir comme projet Gradle sur intellij

Crée votre base de données (PostgreSQL) d'utilisateur a l'aide du fichier /main/resources/sql/bdd.sql

Les tweets seront stocker dans ElasticSeach, veillez a bien demarrer le serveur ElasticSearch avant de lancer le programme

## Fonctions

### Guest

* S'inscrire
* Se connecter
* Consulter la timeline de tous les tweets classer par date de post
* Consulter le Profil d'un utilisateur
* Rechercher a l'aide de la barre de recherche qui effectue des recherches dans les tweets, les utilisateurs et par hashtags (#)


### User

Il possede toutes les fonctionnalités d'un Guest mais peut aussi:

* Acceder a/ Modifier ses information
* Tweeter
* Supprimer un tweet
* Follow/Unfollow un autre utilisateurs
* A acces a une timeline exclusive qui lui montre les messages en fonctions des utilisateurs suivi