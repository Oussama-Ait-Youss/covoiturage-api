# Covoiturage API — Plateforme de covoiturage multi-étapes

## Description du projet

Covoiturage API est une application REST développée avec Java et Spring Boot permettant de gérer une plateforme de covoiturage multi-étapes.

La particularité du projet est de permettre aux conducteurs de publier des trajets avec plusieurs points d'arrêt et aux passagers de réserver des places sur un trajet complet ou sur une partie du trajet.

**Exemple :** Un conducteur propose un trajet Casablanca → Settat → Marrakech. Un passager peut réserver de Casablanca à Marrakech ou uniquement de Settat à Marrakech.

## Technologies utilisées

- Java 17
- Spring Boot
- Spring Data JPA / Hibernate
- PostgreSQL
- Docker et Docker Compose
- Maven
- MapStruct
- Lombok
- Jakarta Bean Validation
- Swagger / OpenAPI
- JUnit 5 et Mockito (pour les tests unitaires, selon leur implémentation dans le projet)

## Architecture du projet

Le projet utilise une architecture en couches :

- **Controller :** expose les endpoints REST et reçoit les requêtes HTTP.
- **DTO :** transporte les données entre le client et l'application.
- **Mapper :** convertit les DTO en entités et inversement.
- **Service :** contient les règles métier.
- **Repository :** communique avec PostgreSQL grâce à Spring Data JPA.
- **Entity :** représente les données persistées.
- **Exception :** gère les erreurs métier et les réponses HTTP.

## Fonctionnalités principales

### Gestion des utilisateurs
- Gestion des conducteurs, passagers et administrateurs.
- Création et consultation des profils.

### Gestion des véhicules
- Ajout des véhicules des conducteurs.
- Consultation des informations des véhicules.
- Gestion de la capacité des véhicules.

### Gestion des trajets
- Publication de trajets.
- Gestion des points d'arrêt.
- Consultation et recherche des trajets.
- Gestion des statuts : `PUBLIE`, `COMPLET`, `TERMINE`, `ANNULE`.

### Gestion des réservations
- Réservation d'un trajet complet ou partiel.
- Calcul du prix selon les points de montée et de descente.
- Vérification des places disponibles sur les segments.
- Annulation des réservations.
- Gestion des statuts : `CONFIRMEE`, `ANNULEE`.

## Modèle de données

Les principales entités sont :

`Utilisateur`, `Conducteur`, `Passager`, `Admin`, `Vehicule`, `Trajet`, `PointArret` et `Reservation`.

L'entité abstraite `Utilisateur` est héritée par les différents types d'utilisateurs.

Les trajets possèdent plusieurs points d'arrêt ordonnés et les réservations référencent les points de montée et de descente.

## Installation et démarrage

### Prérequis

- JDK 17 ou compatible avec la version du projet.
- Docker Desktop et Docker Compose.
- Maven ou Maven Wrapper, selon les fichiers présents dans le projet.

### 1. Cloner le dépôt

```bash
git clone <URL_DU_DEPOT>
cd covoiturage-api
```

### 2. Démarrer PostgreSQL

```bash
docker compose up -d
```

### 3. Configurer l'application

Vérifier les paramètres de connexion PostgreSQL dans `application.properties` ou `application.yml` :

- URL JDBC
- Nom de la base
- Utilisateur PostgreSQL
- Mot de passe

### 4. Démarrer Spring Boot

Avec Maven Wrapper :

```bash
./mvnw spring-boot:run
```

Sur Windows :

```powershell
.\mvnw.cmd spring-boot:run
```

## Documentation de l'API

Après le démarrage de l'application, consulter Swagger UI à l'adresse habituelle suivante, si Springdoc est configuré sur cette route :

`http://localhost:8080/swagger-ui/index.html`

Swagger permet de consulter et de tester les endpoints REST.

## Tests

Pour exécuter les tests Maven :

```bash
./mvnw test
```

Sous Windows :

```powershell
.\mvnw.cmd test
```

Les tests unitaires peuvent utiliser JUnit 5 et Mockito afin de vérifier les règles métier en isolant les dépendances.

## Conception

Les diagrammes UML du projet, notamment les diagrammes de classes et de cas d'utilisation, sont disponibles dans le dossier `diagram/` si ce dossier est présent dans le dépôt.

## Auteur

**Oussama Ait Youss**

Projet réalisé dans le cadre de la formation Java / Spring Boot à YouCode Youssoufia.
