# POC JHipster - Air France Admin Tool

Cette application JHipster a été générée pour tester l'approche "générateur de code" dans le cadre du benchmarking des solutions d'administration de bases de données.

## Architecture

- **Backend** : Spring Boot 3.4.5 + Java 21
- **Frontend** : Angular 19 avec Material Design
- **Base de données** : PostgreSQL (connexion à `poc_airfrance`)
- **Authentification** : JWT
- **ORM** : JPA/Hibernate avec Spring Data
- **API** : REST avec Swagger/OpenAPI
- **Tests** : JUnit 5, Mockito, Cypress

## Entités générées

### Vol
- `id` : Identifiant auto-incrémenté
- `origin` : Code IATA origine (3 lettres)
- `destination` : Code IATA destination (3 lettres)
- `allerRetour` : Boolean (aller-retour)
- `prix` : Montant décimal
- `createdAt` / `updatedAt` : Timestamps automatiques

### Offre
- `id` : Identifiant auto-incrémenté
- `priorite` : Enum (ELEVEE, NORMALE, BASSE)
- `vol` : Relation ManyToOne vers Vol
- `createdAt` / `updatedAt` : Timestamps automatiques

## Prérequis

1. **Docker** : Base de données PostgreSQL doit tourner
   ```bash
   cd ../../db
   docker compose up -d
   ```

2. **Java 21** : Installé et configuré dans le PATH

3. **Node.js 22+** : Déjà installé par le générateur dans `target/node`

## Démarrage rapide

### 1. Lancer le backend Spring Boot

```powershell
# Depuis le dossier jhipster-admin
./mvnw
```

Le backend démarre sur http://localhost:8080

**Endpoints disponibles** :
- API REST : http://localhost:8080/api/
  - `/api/vols` : CRUD complet sur les vols
  - `/api/offres` : CRUD complet sur les offres
- Swagger UI : http://localhost:8080/swagger-ui/index.html
- Health : http://localhost:8080/management/health

### 2. Lancer le frontend Angular

```powershell
# Dans un nouveau terminal, depuis jhipster-admin
npm start
```

Le frontend démarre sur http://localhost:4200

**Navigation** :
- Page d'accueil : http://localhost:4200
- Login : Utilisez les credentials JHipster par défaut
  - Username: `admin`
  - Password: `admin`
- Menu "Entities" > "Vol" : Interface CRUD pour les vols
- Menu "Entities" > "Offre" : Interface CRUD pour les offres

## Connexion à la base de données existante

L'application est configurée pour se connecter à la base PostgreSQL créée via `db/init.sql` :

**Configuration** : `src/main/resources/config/application-dev.yml`

```yaml
datasource:
  url: jdbc:postgresql://localhost:5432/poc_airfrance
  username: dev_user
  password: Dev123!
```

**Important** : Liquibase est désactivé car nous utilisons une DB existante avec schéma préexistant.

## Fonctionnalités générées automatiquement

### Backend (Spring Boot)

✅ **Controllers REST** :
- `VolResource.java` : CRUD complet avec pagination, filtres, tri
- `OffreResource.java` : CRUD complet avec pagination, filtres, tri

✅ **Services** :
- `VolService.java` : Logique métier
- `OffreService.java` : Logique métier
- `VolQueryService.java` : Filtres dynamiques via Criteria API
- `OffreQueryService.java` : Filtres dynamiques via Criteria API

✅ **Repositories** :
- `VolRepository.java` : Interface Spring Data JPA
- `OffreRepository.java` : Interface Spring Data JPA

✅ **DTOs et Mappers** :
- `VolDTO.java` + `VolMapper.java` : Couche de séparation entité/API
- `OffreDTO.java` + `OffreMapper.java` : Couche de séparation entité/API

✅ **Tests** :
- Tests unitaires pour chaque composant
- Tests d'intégration pour l'API REST
- Tests de mapping, criteria, etc.

### Frontend (Angular)

✅ **Composants générés** :
- **Liste** : Table paginée avec tri et filtres
- **Détail** : Vue en lecture seule d'une entité
- **Formulaire Create/Update** : Formulaire réactif avec validation
- **Dialog Delete** : Confirmation de suppression

✅ **Services Angular** :
- `vol.service.ts` : Appels HTTP vers l'API backend
- `offre.service.ts` : Appels HTTP vers l'API backend

✅ **Routing** : Navigation entre liste, détail, création, édition

✅ **Internationalisation** : Traductions FR/EN générées

## Déploiement

### Build de production

```powershell
# Build Maven qui inclut le build du frontend Angular
./mvnw -Pprod clean package

# Le JAR est disponible dans target/
# airfrance-admin-0.0.1-SNAPSHOT.jar
```

### Docker

```powershell
# Build image Docker
./mvnw -Pprod jib:dockerBuild

# Run container
docker run -p 8080:8080 airfrance-admin:0.0.1-SNAPSHOT
```

### Azure

JHipster génère les fichiers de déploiement Azure :
- Configuration Azure App Service
- Support Azure Container Registry
- Intégration Azure KeyVault possible

## Tests

```powershell
# Tests backend (JUnit + Mockito)
./mvnw test

# Tests d'intégration backend
./mvnw verify

# Tests frontend (Jest)
npm test

# Tests E2E (Cypress)
npm run e2e
```

## Structure du code

```
jhipster-admin/
├── src/main/java/com/airfrance/admin/
│   ├── domain/           # Entités JPA (Vol.java, Offre.java)
│   ├── repository/       # Repositories Spring Data
│   ├── service/          # Services métier
│   │   ├── dto/          # DTOs
│   │   ├── mapper/       # MapStruct mappers
│   │   └── criteria/     # Filtres dynamiques
│   ├── web/rest/         # Controllers REST
│   ├── config/           # Configuration Spring
│   └── security/         # Sécurité JWT
├── src/main/webapp/app/  # Application Angular
│   ├── entities/
│   │   ├── vol/          # Module Vol (list, detail, update, delete)
│   │   └── offre/        # Module Offre (list, detail, update, delete)
│   ├── admin/            # Administration JHipster
│   ├── account/          # Gestion compte utilisateur
│   └── shared/           # Composants partagés
└── src/test/             # Tests unitaires et d'intégration
```

## Évaluation pour Air France

### ✅ Forces

1. **Stack native Air France** :
   - ✅ Spring Boot (backend standard Air France)
   - ✅ Angular (frontend standard Air France)
   - ✅ PostgreSQL (DB standard Air France)
   - ✅ Maven (build standard Air France)

2. **Code source propriétaire** :
   - ✅ Code généré modifiable à 100%
   - ✅ Pas de vendor lock-in runtime
   - ✅ Possibilité d'ajouter logique métier custom
   - ✅ Intégration DAISY possible (modification du code généré)

3. **Qualité du code** :
   - ✅ Architecture en couches (Controller > Service > Repository)
   - ✅ DTOs et Mappers (MapStruct)
   - ✅ Tests unitaires et d'intégration générés
   - ✅ Code propre et maintenable

4. **Déploiement Azure** :
   - ✅ Compatible Azure App Service
   - ✅ Compatible Azure Kubernetes Service
   - ✅ Support Azure Container Registry (Jib)
   - ✅ Configuration CI/CD générée

5. **Open-source et gratuit** :
   - ✅ Apache License 2.0
   - ✅ Aucun coût de licence
   - ✅ Communauté active (21k+ stars GitHub)

### ⚠️ Limitations

1. **Time-to-market** :
   - ⚠️ Génération initiale : 5-10 minutes
   - ⚠️ Compilation Maven + npm : 2-3 minutes
   - ⚠️ Courbe d'apprentissage : 1-2 jours pour développeur Spring Boot/Angular

2. **Configuration** :
   - ❌ Pas de configuration YAML déclarative
   - ❌ Nécessite de connaître JDL (JHipster Domain Language)
   - ⚠️ Génération "one-time" : modifications ultérieures = code manuel

3. **Maintenance** :
   - ⚠️ Mises à jour JHipster nécessitent regénération partielle
   - ⚠️ Conflicts potentiels entre code généré et code modifié
   - ⚠️ Complexité accrue par rapport à une solution low-code

4. **Intégration écosystème Air France** :
   - ❌ Pas d'intégration DAISY native (nécessite modifications)
   - ⚠️ Habile : intégration OIDC possible mais nécessite configuration
   - ⚠️ Dynatrace : instrumentation manuelle requise
   - ⚠️ Azure Key Vault : configuration manuelle

## Comparaison avec Budibase

| Critère | JHipster | Budibase |
|---------|----------|----------|
| **Time-to-market** | 15-30 min (avec apprentissage JDL) | 5-10 min (UI drag & drop) |
| **Stack technique** | ✅ Native Air France (Spring Boot + Angular) | ❌ Node.js + Vue/Svelte |
| **Vendor lock-in** | ✅ Aucun (code source généré) | ❌ Total (runtime propriétaire) |
| **Personnalisation** | ✅ Illimitée (modification code) | ⚠️ Limitée (JavaScript + queries SQL) |
| **Maintenance** | ⚠️ Modérée (code à maintenir) | ✅ Faible (config YAML) |
| **Intégration DAISY** | ⚠️ Possible (modification code) | ❌ Impossible |
| **Courbe apprentissage** | ⚠️ 1-2 jours | ✅ < 1 heure |
| **Coût** | ✅ Gratuit (open-source) | ✅ Gratuit (self-hosted) |

## Conclusion POC

JHipster est une **excellente solution pour Air France** si :
- ✅ L'équipe maîtrise Spring Boot + Angular
- ✅ Le besoin nécessite de la personnalisation métier complexe
- ✅ L'intégration DAISY est critique
- ✅ Le projet nécessite un contrôle total du code

JHipster est **moins adapté** si :
- ❌ Besoin d'un time-to-market < 1 heure
- ❌ Équipe petite sans compétences Java/Spring Boot
- ❌ Admin tool très simple (CRUD basique uniquement)
- ❌ Aucun besoin de personnalisation

**Recommandation** : Solution hybride idéale combinant approche JHipster (génération code) + configuration YAML déclarative (comme Budibase).
