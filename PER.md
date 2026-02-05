# Benchmarking des solutions d'administration de bases de données et proposition d'une architecture template pour Air France

---

## Informations

**Auteur :**  
*Baptiste ROYER*

**Formation :**  
*Ingénieur Informatique - Université Côte d'Azur*

**Entreprise d'alternance :**  
*Air France*

**Tuteur entreprise :**  
*Romain SERRA*

**Tuteur académique :**  
*Hélène COLLAVIZZA*

**Année académique :**  
*2025-2026*

**Date :**  
*06/02/2026*

---

## Résumé

*[150-200 mots résumant le contexte, la problématique, la méthodologie, les résultats et les conclusions]*

**Mots-clés :** *admin tools, benchmarking, low-code, database administration, configuration-driven architecture*

---

## Table des matières

1. [Introduction](#1-introduction)
2. [Méthodologie de benchmarking](#2-méthodologie-de-benchmarking)
3. [État de l'art : Solutions du marché](#3-état-de-lart--solutions-du-marché)
4. [Analyse comparative](#4-analyse-comparative)
5. [Processus actuel Air France](#5-processus-actuel-air-france)
6. [Proposition d'architecture template](#6-proposition-darchitecture-template)
7. [Plan de déploiement et adoption](#7-plan-de-déploiement-et-adoption)
8. [Limites et perspectives](#8-limites-et-perspectives)
9. [Conclusion](#9-conclusion)
10. [Références](#10-références)

---

## 1. Introduction

### 1.1 Contexte

Air France opère un écosystème applicatif complexe comprenant plusieurs centaines d'applications, dont plusieurs dizaines sont considérées comme critiques pour l'exploitation quotidienne de l'entreprise. Cet écosystème s'appuie sur une stack technique standardisée : backend Java Spring Boot, frontend Angular avec une librairie graphique d'entreprise, déploiement sur cloud Azure, monitoring via Dynatrace, workflows GitHub, bases de données PostgreSQL, et système d'authentification interne Habile.

Dans le cadre de cette infrastructure, la gestion des données en environnement de production constitue un enjeu stratégique majeur. Pour des raisons de sécurité et de conformité, les développeurs n'ont volontairement aucun accès direct aux bases de données de production. Cette politique de sécurité stricte, bien que justifiée, crée des contraintes opérationnelles importantes pour les équipes de développement.

Les besoins d'interaction avec les bases de données restent néanmoins nombreux et critiques : activation de *canary tests*, gestion des tests A/B, modification de *feature switches* pour déploiements progressifs, débogage en environnement de production, ou encore mises à jour de données métier urgentes. Ces opérations sont essentielles à l'agilité et à la réactivité des équipes face aux incidents ou aux évolutions métier.

### 1.2 Problématique

Face à ces contraintes d'accès, Air France a mis en place un outil d'administration centralisé : le Global Admin Tool (GAT). Cette solution permet aux équipes d'interagir avec leurs données sans accès direct aux bases de production. Chaque application peut disposer d'une section dédiée dans le GAT, fonctionnant comme une base de données administrable, où les équipes peuvent stocker et modifier leurs informations.

Cependant, le processus opérationnel du GAT présente des limitations significatives. Toute demande d'ajout, de modification ou de suppression de données nécessite une demande formelle via le service desk, qui doit être approuvée avant exécution. Ce processus de validation peut prendre plusieurs heures, voire plusieurs jours, ce qui est incompatible avec les besoins de réactivité des équipes, notamment en cas d'incident critique ou de déploiement urgent.

Pour contourner ces délais, de nombreuses équipes ont développé leurs propres interfaces d'administration personnalisées, sur lesquelles elles disposent des pleins pouvoirs. Ces initiatives sont généralement justifiées et approuvées en amont par la hiérarchie, compte tenu de leur valeur opérationnelle : meilleure réactivité en cas d'incident, capacité à gérer les *feature switches* en temps réel, pilotage des tests *canary* et A/B, etc.

Cette approche décentralisée engendre toutefois plusieurs problèmes majeurs :

1. **Absence de standardisation** : Chaque équipe développe sa solution de manière isolée, bien qu'utilisant toujours la même stack technique (Java Spring Boot / Angular / PostgreSQL)

2. **Duplication massive des efforts** : Les solutions développées se ressemblent fortement, chaque équipe "réinvente la roue" en reproduisant des fonctionnalités identiques

3. **Coût en ressources important** : Le développement d'un admin tool personnalisé nécessite de mobiliser un développeur pendant plusieurs semaines, voire plusieurs mois

4. **Inégalité entre équipes** : Seules les équipes disposant de ressources suffisantes peuvent se permettre de développer leur propre solution, créant une fracture avec les petites équipes qui restent dépendantes du GAT

5. **Maintenance dispersée** : Chaque solution doit être maintenue individuellement, sans mutualisation des correctifs ou des évolutions

Cette situation entraîne une perte de temps considérable à l'échelle de l'entreprise et un manque d'équité dans l'accès aux outils d'administration efficaces.

### 1.3 Objectif du benchmarking

L'objectif de ce benchmarking est d'analyser de manière systématique les solutions d'administration de bases de données disponibles sur le marché, afin d'identifier les meilleures pratiques et de les confronter aux approches actuellement mises en œuvre chez Air France.

Cette étude comparative vise à :

1. **Évaluer les solutions existantes** selon des critères objectifs et mesurables pertinents pour le contexte d'Air France

2. **Identifier les forces et faiblesses** de chaque catégorie de solutions (low-code, frameworks, solutions enterprise)

3. **Mesurer l'écart** entre les solutions du marché et les pratiques actuelles d'Air France (GAT et admin tools custom)

4. **Repérer les gaps technologiques** : fonctionnalités manquantes, verrous techniques, limitations des solutions actuelles

5. **Proposer une architecture template** générique et configurable, s'inspirant des meilleures pratiques identifiées, qui permettrait aux équipes de déployer rapidement leur propre interface d'administration

Les critères de comparaison retenus pour ce benchmarking sont les suivants :

1. **Sécurité et mainmise** : Self-hosted, open-source, secrets locaux, conformité
2. **Configurabilité et rapidité de développement** : Configuration déclarative, génération automatique CRUD, time-to-market
3. **Intégration stack Air France** : Azure, Kubernetes, Key Vault, Dynatrace, Habile, PostgreSQL
4. **Maintenance et évolutivité** : Effort de maintenance, stabilité, évolutivité fonctionnelle
5. **Coûts** : Modèle de licence, infrastructure, formation

### 1.4 Périmètre de l'étude

Ce benchmarking couvre plusieurs catégories de solutions permettant de générer ou de faciliter la création d'interfaces d'administration de bases de données :

**1. Plateformes low-code / no-code**
- Solutions permettant de créer des interfaces d'administration avec un minimum de code
- Focus sur : Retool, Budibase
- Critère d'inclusion : capacité à se connecter à une base PostgreSQL existante

**2. Frameworks backend avec génération automatique d'admin**
- Frameworks proposant nativement des interfaces d'administration
- Focus sur : Jmix, JHipster
- Critère d'inclusion : compatibilité avec la stack technique Air France ou adaptabilité

**3. Générateurs d'interfaces basés sur des schémas**
- Outils générant des interfaces à partir de schémas de bases de données
- Focus sur : PostgREST + OpenAPI + Daisy, Hasura

**Méthodologie d'évaluation :**
- **Analyse documentaire approfondie** : Étude de la documentation officielle, spécifications techniques, architectures proposées et guides de démarrage de chaque solution
- **Évaluation sur critères factuels** : Comparaison systématique basée sur des éléments vérifiables (pricing, features documentées, certifications, options de déploiement)
- **Analyse de retours d'expérience** : Étude des reviews utilisateurs sur plateformes vérifiées (G2, Capterra), forums techniques (HackerNews, Reddit), et GitHub issues
- **Tests de démos et sandbox** : Utilisation des environnements de démonstration interactifs proposés par les éditeurs
- **POCs ciblés** : Réalisation de POCs approfondis sur 1-2 solutions open-source les plus prometteuses (permettant une évaluation technique complète sans contraintes de licences)

Ce périmètre volontairement ciblé permet une analyse approfondie et comparative des solutions réellement pertinentes pour le contexte d'Air France, tout en restant réaliste compte tenu des contraintes temporelles d'un PER-alternant.

---

## 2. Méthodologie de benchmarking

### 2.1 Processus appliqué

Cette étude s'appuie sur une méthodologie de benchmarking académique structurée en sept étapes, garantissant la rigueur scientifique tout en produisant des recommandations actionnables pour Air France.

#### Étape 1 : Identification et cadrage

Phase initiale de définition du périmètre (détaillé en section 1.4) incluant l'identification des objectifs, des contraintes Air France, et des exclusions méthodologiques. Cette étape a établi les fondations du benchmarking en collaboration avec le tuteur entreprise et les équipes techniques.

#### Étape 2 : Sélection des solutions

À partir d'une présélection large via recherche documentaire (Gartner, G2, GitHub), forums techniques et analyse communautaire, **6 solutions représentatives** ont été retenues selon quatre critères de sélection : représentativité des approches, maturité des projets, accessibilité pour tests, et pertinence Air France.

**Solutions benchmarkées** :
- **Retool** : Référence commerciale low-code
- **Budibase** : Alternative open-source low-code
- **Jmix** : Framework Spring Boot avec admin intégré
- **JHipster** : Générateur full-stack Spring Boot + Angular
- **Hasura** : API auto-générée avec console admin
- **Template Air France** : Solution proposée (comparaison)

#### Étape 3 : Documentation baseline Air France

Phase d'analyse interne pour établir les métriques de référence :

- **GAT** : Documentation du workflow actuel, mesure des délais, identification des limitations
- **Admin tools custom** : Interviews avec 3-4 équipes, analyse de 2 solutions existantes, estimation des efforts de développement
- **Besoins utilisateurs** : Ateliers avec développeurs, identification des cas d'usage critiques, priorisation des fonctionnalités

Cette baseline permet de mesurer objectivement l'écart entre l'existant Air France et les solutions du marché.

#### Étape 4 : Définition des critères

Cinq familles de critères ont été définies (détaillées en section 2.2) à partir des besoins identifiés et des contraintes Air France. Chaque critère a été décliné en sous-critères mesurables avec grille d'évaluation objective (🟢/🟡/🔴) pour garantir la reproductibilité de l'analyse.

#### Étape 5 : Collecte de données

Collecte multi-sources pour chaque solution :

- **Documentation officielle** : Architecture, pricing, options de déploiement
- **Retours utilisateurs** : Reviews (G2, Capterra), forums techniques, GitHub issues
- **Tests hands-on** : Démos interactives, sandbox PostgreSQL, mesure time-to-market
- **POCs approfondis** : Déploiement Azure de Budibase et Hasura, tests intégration Key Vault et OIDC, évaluation courbe d'apprentissage
- **Données économiques** : Tarification officielle, estimation coûts infrastructure, calcul TCO sur 3 ans

**Infrastructure de test commune** : Pour garantir l'équité et la reproductibilité des POCs, une base de données PostgreSQL standardisée a été créée, simulant un cas d'usage réel Air France. Cette infrastructure Docker comprend :

- **Modèle de données réaliste** : Table `vol` (critique, accès restreint) et table `offre` (modifiable, gestion marketing) avec relations, contraintes et indexation
- **Scénario métier concret** : Développeur devant gérer les priorités d'offres marketing sans accès aux données sensibles des vols
- **Gestion des permissions** : Rôle `admin_role` (accès complet) et `user_role` (CRUD limité à table offre), validant la capacité de chaque solution à respecter la sécurité
- **Déploiement automatisé** : Script SQL d'initialisation avec 20 vols et 12 offres pré-insérés, exécutable via `docker compose up -d`
- **Données de test cohérentes** : Routes Air France réalistes (CDG-JFK, CDG-NRT, CDG-LHR...) avec codes IATA valides, prix réalistes, et priorisation marketing

Cette base commune permet de tester chaque solution sur les mêmes données et contraintes, mesurant objectivement le time-to-market (temps entre connexion DB et interface fonctionnelle), la capacité à gérer les permissions PostgreSQL natives, et la facilité de mise en œuvre. Connexion : `postgresql://dev_user:Dev123!@localhost:5432/poc_airfrance`

#### Étape 6 : Analyse comparative

Consolidation dans des matrices d'évaluation (section 4) :

- Scoring selon critères définis (🟢/🟡/🔴) et agrégation par famille
- Identification des forces/faiblesses de chaque approche
- Détection du **gap stratégique** : aucune solution ne combine configuration YAML + stack native Air France + self-hosted + open-source
- Comparaison vs baseline : GAT (plusieurs jours) vs custom (plusieurs semaines) vs low-code (1-2 jours)
- Analyse tendances marché : essor low-code, importance configurabilité

#### Étape 7 : Recommandations et plan d'action

Transformation de l'analyse en livrables actionnables :

- **Identification du gap** : Besoin d'une solution hybride combinant les meilleures pratiques identifiées
- **Architecture template** (section 6) : Configuration YAML (Retool/Budibase) + génération code (JHipster) + auto-génération API (Hasura) + stack native Air France
- **Plan de déploiement** (section 7) : Roadmap 4 phases, pilotes, stratégie d'adoption, métriques de succès
- **Bénéfices quantifiés** : Réduction time-to-market, standardisation, suppression développements redondants, équité d'accès

Cette approche méthodique garantit la traçabilité des choix, la reproductibilité de l'étude, et l'applicabilité opérationnelle des recommandations.

### 2.2 Critères de comparaison

Sécurité :
  - self hosted && open source
  - limiter les opérations de table / champs par rôle
  - gestion des secrets

Complexité de mise en place :
  - Technos utilisées
  - Temps de configuration
  - Connexion à la db

Fonctionnalité && Évoluabilité :
  - Opérations basiques (Ajout, Visualisation, Modification, Suppression)
  - Ajout de nouvelles tables / colonnes 
  - Création de nouvelles fonctionnalités (modification directe du code pour créer des fonctionnalité exclusives)

Intégration dans l'écosystème Air France
  - Deployable facilement sur Azure
  - Compatibilité Habile
  - Interface DAISY
  - Monitoring Dynatrace

#### 2.2.1 Sécurité et mainmise

**Objectif** : Garantir que la solution respecte les contraintes de sécurité strictes d'Air France et permet un contrôle total sur les données et l'infrastructure.

**Checklist d'évaluation** :

| Critère | Évaluation | Commentaires |
|---------|------------|-------------|
| Self-hosted disponible | 🟢 Oui / 🔴 Non | Possibilité de déployer sur infrastructure Air France |
| Open-source | 🟢 Oui / 🔴 Non | Accès au code source pour audit de sécurité |
| Secrets locaux | 🟢 Oui / 🔴 Non | Les credentials restent dans l'écosystème Air France |
| Conformité | 🟢 Certifié / 🔴 Non | RGPD, SOC2, ou autres certifications |

#### 2.2.2 Configurabilité et rapidité de développement

**Objectif** : Mesurer la facilité et la rapidité avec laquelle une équipe peut déployer un admin tool fonctionnel.

**Checklist d'évaluation** :

| Critère | Évaluation | Commentaires |
|---------|------------|-------------|
| Configuration déclarative | 🟢 YAML/JSON / 🔴 Code uniquement | Définition de l'admin tool via fichiers config |
| Time-to-market | 🟢 < 1 semaine / 🔴 > 1 semaine | Temps estimé pour un admin tool fonctionnel |
| Courbe d'apprentissage | 🟢 Faible / 🟡 Modérée / 🔴 Élevée | Facilité de prise en main pour dev Air France |
| Documentation | 🟢 Excellente / 🟡 Correcte / 🔴 Limitée | Qualité des guides et tutoriels |

#### 2.2.3 Intégration avec la stack technique Air France

**Objectif** : Évaluer la compatibilité de la solution avec l'écosystème technologique standardisé d'Air France.

**Checklist d'évaluation** :

| Critère | Évaluation | Commentaires |
|---------|------------|-------------|
| Déploiement Azure | 🟢 Possible / 🔴 Non supporté | App Service, Container Apps, ou AKS |
| Kubernetes | 🟢 Possible / 🔴 Non supporté | Compatibilité avec workflows K8s |
| Azure Key Vault | 🟢 Possible / 🔴 Non supporté | Gestion des secrets via Key Vault |
| Monitoring Dynatrace | 🟢 Possible / 🔴 Non supporté | APM, logs, traces |
| Habile | 🟢 Possible / 🔴 Non supporté | Intégration avec authentification interne Habile |
| PostgreSQL | 🟢 Support natif / 🔴 Non supporté | Connecteur officiel, ORM compatible |
| CI/CD GitHub Actions | 🟢 Possible / 🔴 Non supporté | Automatisation du déploiement |
| DAISY | 🟢 Possible / 🔴 Non supporté | Interface avec les composants DAISY |

#### 2.2.4 Maintenance et évolutivité

**Objectif** : Mesurer l'effort requis pour maintenir la solution dans le temps et sa capacité à évoluer avec les besoins.

**Checklist d'évaluation** :

| Critère | Évaluation | Commentaires |
|---------|------------|-------------|
| Effort de maintenance | 🟢 Faible / 🟡 Modéré / 🔴 Élevé | Fréquence des mises à jour, complexité |
| Stabilité des dépendances | 🟢 Stable / 🟡 Modérée / 🔴 Instable | Risques de breaking changes |
| Évolutivité fonctionnelle | 🟢 Via config / 🔴 Via code | Facilité d'ajout de tables/champs |

#### 2.2.5 Coûts

**Objectif** : Évaluer le coût total de possession (TCO) de la solution sur une période de 3 ans.

**Checklist d'évaluation** :

| Critère | Évaluation | Valeur estimée |
|---------|------------|----------------|
| Modèle de licence | 🟢 Open-source gratuit / 🟡 Freemium / 🔴 Payant | Type de licence |
| Coût licences (si applicable) | €/utilisateur/mois ou forfait | Pricing officiel |
| Infrastructure Azure | 🟢 Légère / 🟡 Modérée / 🔴 Lourde | Estimation compute/stockage/réseau |
| Temps de formation | 🟢 < 1 jour / 🟡 2-5 jours / 🔴 > 1 semaine | Par développeur |

#### 2.2.6 Note sur la performance

La performance (temps de réponse, latence, capacité de montée en charge) n'est pas retenue comme critère principal de comparaison pour les raisons suivantes :

- Les admin tools sont utilisés par un nombre limité d'utilisateurs simultanés (5-20 personnes par équipe)
- Les opérations effectuées sont principalement des opérations CRUD ponctuelles, non critiques en termes de latence
- La charge de calcul et de requêtage repose sur les bases de données PostgreSQL, déjà dimensionnées pour les applications métier
- Les solutions modernes (architectures web, APIs REST) offrent toutes des performances largement suffisantes pour cet usage

### 2.3 Solutions sélectionnées

*[Liste des 5-7 solutions retenues avec justification de leur sélection]*

### 2.4 Méthode d'évaluation

*[Analyse documentaire, POCs réalisés, interviews avec équipes, tests pratiques]*

---

## 3. État de l'art : Solutions du marché

### 3.1 Plateformes low-code

#### 3.1.1 Retool

##### Description

Retool est la plateforme low-code leader du marché, spécialisée dans la création d'outils internes (admin panels, dashboards, workflows). Fondée en 2017, elle permet de construire des interfaces d'administration via une approche drag-and-drop combinée à du JavaScript pour la logique métier.

**Modèle** : Plateforme runtime (SaaS ou self-hosted). Les applications sont configurées dans l'éditeur Retool et s'exécutent dans le moteur Retool.

**Fonctionnement** :
1. Connexion à une ou plusieurs sources de données (PostgreSQL, APIs REST, services cloud)
2. Écriture de queries (SQL, API calls) dans l'interface
3. Drag & drop de composants UI (tables, formulaires, graphiques, boutons)
4. Binding des données vers les composants via double accolades `{{}}`
5. Logique métier en JavaScript pour orchestrer les interactions
6. Déploiement instantané via URL Retool

**Architecture** : React frontend propriétaire, stockage configuration dans base Retool, self-hosted via Docker/Kubernetes.

**Pricing** : Cloud ($10-50/user/mois), Self-hosted Enterprise (~$50k+/an pour entreprise).

##### Forces
*[Points forts identifiés]*

##### Faiblesses
*[Points faibles identifiés]*

##### Évaluation par critère
*[Tableau de notation selon les 5 critères définis en section 2.2]*

#### 3.1.2 Budibase

##### Description

Budibase est une plateforme low-code open-source (licence GPL v3) lancée en 2020, positionnée comme alternative gratuite à Retool. Elle permet de créer des applications internes via une interface visuelle similaire aux solutions commerciales.

**Modèle** : Plateforme runtime open-source. Les applications sont configurées dans le builder Budibase et s'exécutent dans le runtime Budibase.

**Fonctionnement** :
1. Connexion à PostgreSQL ou autres datasources (REST APIs, MySQL, MongoDB, etc.)
2. Création d'écrans via drag & drop de composants (tables, forms, charts)
3. Définition de workflows et d'automations
4. Binding de données et logique avec expressions JavaScript
5. Déploiement self-hosted (Docker) ou cloud Budibase

**Architecture** : Frontend Vue.js/Svelte, backend Node.js, stockage configuration en CouchDB, images Docker officielles.

**Pricing** : Self-hosted gratuit (open-source), Cloud ($5-25/user/mois pour features premium), Enterprise sur devis.

**Communauté** : Projet actif sur GitHub (~22k stars), documentation complète, plugins communautaires.

##### Forces

**Gain de temps exceptionnel** : Interface CRUD fonctionnelle en 15 minutes (connexion PostgreSQL + génération écrans automatique). Solution open-source (GPL v3) déployable self-hosted sans coûts de licence.

**Fonctionnalités natives complètes** : CRUD basique, queries SQL custom pour vues complexes, import/export CSV/Excel/JSON. Modification directe du schéma DB (relations, contraintes) depuis l'interface, pratique pour le développement ou les applications peu critiques.

**Intégration Azure** : Compatible AKS (Helm charts), Azure Container Apps, et App Service. Support Azure Key Vault via variables d'environnement. Authentification OIDC/SAML pour intégration SSO.

**Évolutivité DB sans redéploiement** : Détection automatique des changements de schéma PostgreSQL (nouvelles tables/colonnes) via simple refresh. Aucune regénération ou redéploiement applicatif requis.

##### Faiblesses

**Intégration écosystème Air France limitée** : Pas d'interface DAISY native (composants UI propriétaires Budibase). Habile assure l'authentification (accès binaire à l'application) mais ne synchronise pas automatiquement les rôles. Solution de contournement : attribuer un rôle PostgreSQL unique et restrictif à Budibase, appliquant les mêmes permissions à tous les utilisateurs authentifiés Habile (perte de granularité).

**Vendor lock-in runtime** : Code généré non exportable, dépendance totale à la plateforme Budibase. Migration vers autre stack (Spring Boot, React) nécessite redéveloppement complet. Pas d'ajout de fonctionnalités métier custom.

**Permissions PostgreSQL non détectées** : L'UI ne s'adapte pas aux permissions DB (boutons Delete/Insert affichés même si rôle PostgreSQL les interdit). Configuration manuelle requise pour masquer les actions non autorisées. Absence d'audit trail natif (nécessite logs PostgreSQL externes).

**Monitoring Dynatrace complexifié** : Intégration possible via OneAgent mais nécessite images Docker custom ou Kubernetes Operator. Observabilité métier limitée (pas de tags custom, pas d'accès code pour instrumentation fine). Maintenance additionnelle des images modifiées. 

##### Évaluation par critère

### 3.2 Frameworks Spring Boot avec génération d'admin

#### 3.2.1 Jmix

##### Description

Jmix est un framework Spring Boot avec génération automatique d'interfaces d'administration, successeur de CUBA Platform (rebranding en 2020). Il cible les développeurs Java souhaitant créer des applications d'entreprise avec backoffice intégré.

**Modèle** : Framework + Studio visuel. Jmix génère du code source Java Spring Boot que vous possédez et modifiez.

**Fonctionnement** :
1. Installation du plugin Jmix Studio (IntelliJ IDEA)
2. Définition des entités JPA (classes Java annotées)
3. Génération automatique des écrans CRUD via le Studio
4. UI générée en Vaadin (Java) ou React/TypeScript
5. Personnalisation via code Java standard
6. Build et déploiement d'un JAR Spring Boot classique

**Architecture** : Spring Boot backend, JPA/Hibernate pour persistence, UI Vaadin Flow (composants Java côté serveur) ou React frontend, PostgreSQL support natif.

**Stack technique** : 100% Java/Spring Boot, écosystème Spring standard (Security, Data, etc.).

**Pricing** : Community Edition gratuite (Apache 2.0), versions commerciales avec addons premium (RAD Studio, BPM, Reports).

##### Forces

##### Faiblesses

##### Évaluation par critère
*[Tableau de notation selon les 5 critères définis en section 2.2]*

#### 3.2.2 JHipster

##### Description

JHipster est un générateur Yeoman open-source créé en 2013, spécialisé dans la génération d'applications Spring Boot + frontend moderne (Angular/React/Vue). C'est l'un des projets les plus populaires de l'écosystème Spring (21k+ stars GitHub).

**Modèle** : Générateur de code one-time. JHipster génère une application complète que vous possédez et modifiez ensuite librement.

**Fonctionnement** :
1. Installation du CLI JHipster (`npm install -g generator-jhipster`)
2. Génération du projet via commande interactive `jhipster`
3. Choix stack : DB (PostgreSQL, MySQL, etc.), frontend (Angular/React/Vue), options (cache, search, etc.)
4. Génération d'entités : `jhipster entity User` crée backend + frontend CRUD complet
5. Code source généré modifiable (Java, TypeScript, HTML)
6. Build Maven/Gradle produit un JAR exécutable

**Architecture** : Spring Boot backend (REST API), JPA/Hibernate, Spring Security, frontend Angular/React/Vue avec Material Design, tests unitaires et e2e inclus.

**Déploiement** : Docker, Kubernetes, Heroku, Cloud (Azure, AWS, GCP) via configurations générées.

**Pricing** : 100% gratuit et open-source (Apache 2.0), aucun coût de licence.

##### Forces
*[Points forts identifiés]*

##### Faiblesses
*[Points faibles identifiés]*

##### Évaluation par critère
*[Tableau de notation selon les 5 critères définis en section 2.2]*

### 3.3 Générateurs d'API avec console d'administration

#### 3.3.1 Hasura

##### Description

Hashura est un moteur GraphQL open-source qui génère automatiquement une API GraphQL complète à partir d'un schéma PostgreSQL existant. Lancé en 2018, il inclut une console d'administration web permettant d'administrer les données sans développement frontend.

**Modèle** : Moteur GraphQL + Console admin intégrée. Hasura introspect votre base PostgreSQL et expose instantanément une API GraphQL.

**Fonctionnement** :
1. Déploiement de Hasura Engine (Docker/Kubernetes)
2. Connexion à base PostgreSQL existante via connection string
3. Hasura détecte automatiquement tables, colonnes, foreign keys
4. API GraphQL générée instantanément (queries, mutations, subscriptions)
5. Console Hasura accessible via navigateur pour CRUD, exploration API
6. Configuration RBAC, actions custom, event triggers via interface

**Architecture** : Hasura Engine (Haskell), Console React, métadonnées stockées dans schema PostgreSQL `hdb_catalog`, WebSockets pour temps réel.

**Console d'administration** : UI React complète pour browse data, insert/update/delete, permissions management, API explorer GraphiQL intégré.

**Pricing** : Open-source gratuit (MIT), Hasura Cloud ($99-299/mois), Enterprise self-hosted sur devis.

##### Forces
*[Points forts identifiés]*

##### Faiblesses
*[Points faibles identifiés]*

##### Évaluation par critère
*[Tableau de notation selon les 5 critères définis en section 2.2]*

### 3.4 Architecture template Air France (proposition)

##### Description

L'architecture template Air France est une solution hybride conçue spécifiquement pour combler le gap identifié lors du benchmarking : aucune solution du marché ne combine configuration déclarative (YAML), stack technique native Air France (Spring Boot + Angular Daisy), self-hosted, et open-source.

**Modèle** : Générateur de code avec configuration déclarative. Un fichier YAML définit l'admin tool, le générateur produit du code source Spring Boot + Angular que les équipes possèdent et peuvent modifier.

**Approche** : Synthèse des meilleures pratiques identifiées lors du benchmarking :
- **Configuration YAML déclarative** (inspiration Retool/Budibase) : Pas de code à écrire pour CRUD basique
- **Génération de code Spring Boot + Angular** (inspiration JHipster) : Code source propriétaire, personnalisable à l'infini
- **Auto-génération API depuis schéma DB** (inspiration Hasura) : Introspection PostgreSQL pour génération automatique
- **Stack native Air France** : Spring Boot backend, Angular + Daisy Design System frontend, PostgreSQL, déploiement Azure/Kubernetes

**Workflow envisagé** :
1. Équipe écrit fichier `admin-config.yaml` définissant tables, champs, permissions, validations
2. Commande `af-admin generate admin-config.yaml` génère le code Spring Boot + Angular
3. Code source généré dans repository Git de l'équipe
4. Équipe peut personnaliser le code (ajout logique métier custom)
5. Build Maven/npm produit artefacts déployables
6. Déploiement Azure Kubernetes via pipeline GitHub Actions

**Positionnement** : Cette solution est évaluée comme 6ème option du benchmarking pour démontrer comment elle répond aux limitations des 5 solutions marché analysées.

##### Principes de conception
*[Comment la solution combine les meilleures pratiques identifiées]*

##### Avantages attendus
*[Bénéfices spécifiques au contexte Air France]*

##### Évaluation par critère
*[Tableau de notation théorique selon les 5 critères, démontrant comment le template comble le gap identifié]*

---

## 4. Analyse comparative

### 4.1 Tableau de synthèse

*[Matrices d'évaluation complètes par critère]*

#### 4.1.1 Sécurité & Mainmise

| Solution | Self-hosted | Open-source | Secrets locaux | Conformité RGPD |
|----------|:-----------:|:-----------:|:--------------:|:---------------:|
| **Retool** | | | | |
| **Forest Admin** | | | | |
| **Budibase** | | | | |
| **Appsmith** | | | | |
| **Django Admin** | | | | |
| **Rails Active Admin** | | | | |
| **Backstage** | | | | |

#### 4.1.2 Configurabilité & Rapidité

| Solution | Config. déclarative | Génération CRUD | Time-to-market | Courbe apprentissage | Documentation |
|----------|:-------------------:|:---------------:|:--------------:|:--------------------:|:-------------:|
| **Retool** | | | | | |
| **Forest Admin** | | | | | |
| **Budibase** | | | | | |
| **Appsmith** | | | | | |
| **Django Admin** | | | | | |
| **Rails Active Admin** | | | | | |
| **Backstage** | | | | | |

#### 4.1.3 Intégration Stack Air France

| Solution | Azure | Kubernetes | Key Vault | Dynatrace | Habile SSO | PostgreSQL | CI/CD GitHub |
|----------|:-----:|:----------:|:---------:|:---------:|:----------:|:----------:|:------------:|
| **Retool** | | | | | | | |
| **Forest Admin** | | | | | | | |
| **Budibase** | | | | | | | |
| **Appsmith** | | | | | | | |
| **Django Admin** | | | | | | | |
| **Rails Active Admin** | | | | | | | |
| **Backstage** | | | | | | | |

#### 4.1.4 Maintenance & Évolutivité

| Solution | Effort maintenance | Stabilité dépendances | Évolutivité fonctionnelle |
|----------|:------------------:|:---------------------:|:-------------------------:|
| **Retool** | | | |
| **Forest Admin** | | | |
| **Budibase** | | | |
| **Appsmith** | | | |
| **Django Admin** | | | |
| **Rails Active Admin** | | | |
| **Backstage** | | | |

#### 4.1.5 Coûts

| Solution | Modèle licence | Coût licences | Infrastructure Azure | Temps formation |
|----------|:--------------:|:-------------:|:--------------------:|:---------------:|
| **Retool** | | | | |
| **Forest Admin** | | | | |
| **Budibase** | | | | |
| **Appsmith** | | | | |
| **Django Admin** | | | | |
| **Rails Active Admin** | | | | |
| **Backstage** | | | | |

**Légende** :
- 🟢 : Oui / Supporté / Faible / Excellent
- 🟡 : Partiellement / Modéré / Acceptable  
- 🔴 : Non / Non supporté / Élevé / Limité

### 4.2 Analyse par critère

#### 4.2.1 Sécurité
*[Analyse détaillée : quelles solutions répondent aux contraintes Air France ?]*

#### 4.2.2 Time-to-market
*[Analyse détaillée : quelle solution est la plus rapide ?]*

#### 4.2.3 Configurabilité
*[Analyse détaillée : qui permet le "fichier config" ?]*

#### 4.2.4 Coût total de possession
*[Analyse détaillée : comparaison des coûts directs et indirects]*

### 4.3 Visualisations comparatives

*[Radar charts, graphiques, diagrammes comparatifs]*

### 4.4 Identification des gaps

*[Analyse des manques identifiés : aucune solution ne combine self-hosted + config simple + déploiement rapide + sécurité secrets entreprise]*

### 4.5 Tendances du marché

*[Évolution des solutions, tendances low-code, importance de la configurabilité]*

---

## 5. Processus actuel Air France

### 5.1 Global Admin Tool (GAT)

#### 5.1.1 Fonctionnement
*[Description du GAT existant]*

#### 5.1.2 Forces
*[Points positifs du système actuel]*

#### 5.1.3 Limites
*[Problèmes identifiés : lourdeur du service desk, délais, rigidité]*

### 5.2 Admin tools custom développés par les équipes

#### 5.2.1 Analyse d'exemples réels
*[Étude de 2-3 admin tools développés en interne]*

#### 5.2.2 Patterns communs
*[Fonctionnalités récurrentes, architectures similaires]*

#### 5.2.3 Duplication des efforts
*[Chiffrage du temps perdu, ressources mobilisées]*

### 5.3 Mesure de performance vs marché

*[Comparaison des processus Air France avec les solutions du marché : temps de développement, flexibilité, coûts]*

### 5.4 Besoins exprimés par les équipes

*[Résultats d'interviews, pain points identifiés]*

---

## 6. Proposition d'architecture template

### 6.1 Principes de conception

#### 6.1.1 Ce qu'on garde des solutions benchmarkées
*[Synthèse des meilleures pratiques identifiées :]*
- *Configurabilité type Retool*
- *Sécurité on-premise type Django*
- *Rapidité de déploiement type Forest Admin*
- *Extensibilité type Backstage*

#### 6.1.2 Combler les gaps identifiés
*[Comment l'architecture proposée répond aux manques du marché]*

### 6.2 Architecture globale

#### 6.2.1 Vue d'ensemble
*[Diagramme d'architecture : Frontend / Backend / Configuration / Databases]*

#### 6.2.2 Stack technique proposée

##### Frontend
*[Technologies : React/Vue, composants UI, templating]*

##### Backend
*[Technologies : Node.js/Python, API REST/GraphQL, ORM]*

##### Configuration
*[Format : YAML/JSON, validation, hot-reload]*

##### Infrastructure
*[Docker, Kubernetes, CI/CD]*

### 6.3 Système de configuration

#### 6.3.1 Structure du fichier de configuration
*[Schéma détaillé du fichier YAML/JSON]*

```yaml
# Template exemple
name: 
description: 

databases:
  - name: 
    connection:
      host: 
      port: 
      database: 
      credentials_ref: 
    
    tables:
      - name: 
        display_name: 
        permissions:
          read: []
          write: []
          delete: []
        
        fields:
          - name: 
            type: 
            display_name: 
            editable: 
            required: 
            validation: 
```

#### 6.3.2 Validation du fichier de configuration
*[Schéma de validation, vérifications automatiques, feedback utilisateur]*

#### 6.3.3 Évolution de la configuration
*[Versioning, migration, hot-reload]*

### 6.4 Gestion de la sécurité

#### 6.4.1 Gestion des secrets
*[Intégration HashiCorp Vault / Kubernetes Secrets / Azure Key Vault]*

#### 6.4.2 Système RBAC
*[Définition des rôles, permissions, groupes, intégration LDAP/AD]*

#### 6.4.3 Audit et traçabilité
*[Logs, audit trails, conformité RGPD]*

### 6.5 Fonctionnalités de base

#### 6.5.1 CRUD operations
*[Création, lecture, mise à jour, suppression des enregistrements]*

#### 6.5.2 Recherche et filtrage
*[Moteur de recherche, filtres dynamiques, exports]*

#### 6.5.3 Validation des données
*[Règles de validation, contraintes métier, feedback utilisateur]*

#### 6.5.4 Bulk operations
*[Import CSV, modifications en masse, API batch]*

### 6.6 Extensibilité

#### 6.6.1 Plugins et extensions
*[Système de plugins, hooks, événements]*

#### 6.6.2 Custom actions
*[Actions métier personnalisées, workflows]*

#### 6.6.3 Intégrations
*[APIs externes, webhooks, notifications]*

### 6.7 Exemple concret d'utilisation

*[Cas d'usage complet : configuration d'un admin tool pour une équipe spécifique avec le template]*

---

## 7. Plan de déploiement et adoption

### 7.1 Roadmap de mise en œuvre

#### Phase 1 : Développement du template
*[Durée, ressources, livrables]*

#### Phase 2 : Pilote avec équipes sélectionnées
*[Sélection de 2-3 équipes, accompagnement, feedback]*

#### Phase 3 : Amélioration itérative
*[Intégration des retours, optimisations]*

#### Phase 4 : Déploiement généralisé
*[Communication, formation, support]*

### 7.2 Stratégie d'adoption

#### 7.2.1 Communication
*[Plan de communication interne, documentation, showcases]*

#### 7.2.2 Formation
*[Modules de formation, tutoriels, support]*

#### 7.2.3 Support et accompagnement
*[Équipe dédiée, channels Slack/Teams, FAQ]*

### 7.3 Métriques de succès

*[KPIs : temps de développement économisé, nombre d'équipes adoptantes, satisfaction utilisateurs, réduction des demandes service desk]*

---

## 8. Limites et perspectives

### 8.1 Limites du benchmarking

*[Solutions non testées, temps de tests limités, contexte spécifique Air France]*

### 8.2 Limites de l'architecture proposée

*[Contraintes techniques, courbe d'apprentissage, maintenance]*

### 8.3 Travaux futurs

#### 8.3.1 Évolutions possibles
*[Génération automatique d'UI complexes, intégration IA, analytics avancés]*

#### 8.3.2 Extensions envisagées
*[Support de bases NoSQL, intégration avec data lakes, GraphQL avancé]*

#### 8.3.3 Recherche et innovation
*[Pistes de recherche académique : DSL optimisés, génération assistée par IA]*

---

## 9. Conclusion

### 9.1 Synthèse des résultats du benchmarking

*[Résumé des solutions analysées, critères clés, gaps identifiés]*

### 9.2 Contribution de l'architecture template

*[Valeur ajoutée : standardisation, accélération, démocratisation]*

### 9.3 Bénéfices attendus pour Air France

*[Gains de temps, réduction des coûts, amélioration de la productivité, satisfaction équipes]*

### 9.4 Perspectives

*[Vision à long terme, évolution de la solution, impact organisationnel]*

---

## 10. Références

### Articles scientifiques

*[1] Auteur, A., Auteur, B. (Année). Titre de l'article. Nom de la revue, volume(numéro), pages.*

### Documentation technique

*[a] Nom de la solution. (Année). Documentation officielle. URL*

### Livres et ouvrages

*[X] Auteur, A. (Année). Titre du livre. Éditeur.*

### Sites web et ressources en ligne

*[i] Source. (Année). Titre de la ressource. URL*

---

## Annexes

### Annexe A : Infrastructure de test POC

#### A.1 Vue d'ensemble

Infrastructure PostgreSQL standardisée simulant un cas d'usage réel Air France pour tester les 6 solutions (Retool, Budibase, Jmix, JHipster, Hasura, Template Air France).

#### A.2 Modèle de données

##### Table `vol` (Critique)

| Colonne | Type | Description | Contraintes |
|---------|------|-------------|-------------|
| `id` | SERIAL | Identifiant unique | PRIMARY KEY |
| `origin` | VARCHAR(3) | Code IATA aéroport départ | NOT NULL, CHECK(length=3) |
| `destination` | VARCHAR(3) | Code IATA aéroport arrivée | NOT NULL, CHECK(length=3) |
| `aller_retour` | BOOLEAN | Aller-retour ou simple | NOT NULL, DEFAULT false |
| `prix` | DECIMAL(10,2) | Prix en euros | NOT NULL, CHECK(>0) |
| `created_at` | TIMESTAMP | Date de création | DEFAULT now() |
| `updated_at` | TIMESTAMP | Date de modification | DEFAULT now() |

##### Table `offre` (Modifiable)

| Colonne | Type | Description | Contraintes |
|---------|------|-------------|-------------|
| `id` | SERIAL | Identifiant unique | PRIMARY KEY |
| `vol_id` | INTEGER | Référence vers vol | FOREIGN KEY, NOT NULL |
| `priorite` | ENUM | Priorité marketing | 'elevee', 'normale', 'basse' |
| `created_at` | TIMESTAMP | Date de création | DEFAULT now() |
| `updated_at` | TIMESTAMP | Date de modification | DEFAULT now() |

##### Vue `v_offres_detail`

Vue combinée offre + vol triée par priorité.

#### A.3 Gestion des permissions

##### Rôle `admin_role` / Utilisateur `admin_user`
- **Permissions** : Accès complet (SELECT, INSERT, UPDATE, DELETE)
- **Credentials** : `admin_user` / `Admin123!`

##### Rôle `user_role` / Utilisateur `dev_user`
- **Permissions** : CRUD complet sur `offre`, lecture seule sur `vol`, lecture sur `v_offres_detail`
- **Credentials** : `dev_user` / `Dev123!`

#### A.4 Données de test

**20 vols** : CDG-LHR (150€), CDG-JFK (650€), CDG-NRT (1200€), CDG-BCN (200€), CDG-ROM (250€), etc.

**12 offres** : 3 priorité élevée (Paris-Londres, Paris-New York, Paris-Tokyo), 5 priorité normale, 4 priorité basse.

#### A.5 Scénario de test

Développeur marketing devant ajuster les priorités d'offres sans accès à la table `vol` critique.

**Opérations autorisées** : Visualiser offres, modifier priorité, créer/supprimer offre, filtrer/trier.

**Restrictions** : Aucune modification de la table `vol`.

#### A.6 Infrastructure Docker

**Démarrage** :
```bash
cd poc-database/
docker compose up -d
```

**Connexion** :
```
postgresql://dev_user:Dev123!@localhost:5432/poc_airfrance
```

**Test permissions** :
```sql
SELECT * FROM offre;
UPDATE offre SET priorite = 'elevee' WHERE id = 5;
UPDATE vol SET prix = 100 WHERE id = 1;
```

Fichiers : `poc-database/init.sql` (script PostgreSQL) et `poc-database/docker-compose.yml` (orchestration Docker).

### Annexe B : Grilles d'évaluation détaillées

*[Grilles complètes utilisées pour scorer chaque solution]*

### Annexe C : Captures d'écran des solutions

*[Screenshots des interfaces des différentes solutions testées]*

### Annexe C : Exemples de fichiers de configuration

*[Exemples complets de configurations pour différents cas d'usage]*

### Annexe D : Code source du POC

*[Lien vers le repository Git ou extraits de code significatifs]*