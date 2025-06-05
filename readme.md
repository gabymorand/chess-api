# ♟️ Chess API - Projet Spring Boot

<div align="center">

![Chess API](https://img.shields.io/badge/Chess-API-blue?style=for-the-badge&logo=chess.com)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen?style=for-the-badge&logo=spring)
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![Railway](https://img.shields.io/badge/Railway-Deployed-purple?style=for-the-badge&logo=railway)

**API REST complète pour la gestion d'un système d'échecs avec IA intégrée**

[🌐 **Demo Live**](https://chess-api.up.railway.app/) | [📚 **Documentation**](https://chess-api.up.railway.app/swagger-ui.html) | [🤖 **IA Mistral**](https://chess-api.up.railway.app/api/ai/health)

</div>

---

## 🎯 **Aperçu du Projet**

Cette API REST moderne permet de gérer un système complet d'échecs incluant :
- 👥 **Gestion des utilisateurs** avec authentification JWT
- ♟️ **Parties d'échecs** complètes avec historique des coups
- 🏆 **Tournois** et système de classement
- 🤖 **Intelligence Artificielle** intégrée (Mistral AI)
- 📊 **Statistiques** et analyses de parties

## ✨ **Fonctionnalités Principales**

### 🔐 **Authentification & Sécurité**
- JWT Token avec refresh
- Rôles utilisateur (USER/ADMIN)
- Sécurisation par endpoints
- BCrypt password hashing

### ♟️ **Gestion des Échecs**
- Création et gestion de parties
- Enregistrement des coups en notation algébrique
- Système de commentaires sur les parties
- Génération automatique de PGN

### 🏆 **Système Compétitif**
- Tournois avec inscriptions
- Classement ELO automatique
- Statistiques détaillées par joueur
- Leaderboards dynamiques

### 🤖 **IA Mistral Intégrée**
- Chat intelligent sur les échecs
- Analyse automatique de parties
- Suggestions de coups optimaux
- Quiz tactiques personnalisés
- Conseils d'amélioration

## 🛠️ **Technologies Utilisées**

| Technologie | Version | Usage |
|-------------|---------|-------|
| **Spring Boot** | 3.5.0 | Framework principal |
| **Spring Security** | 6.x | Authentification JWT |
| **Spring Data JPA** | 3.x | Accès aux données |
| **PostgreSQL** | 15+ | Base de données |
| **Mistral AI** | API | Intelligence artificielle |
| **Swagger/OpenAPI** | 3.0 | Documentation |
| **Railway** | - | Déploiement cloud |
| **Maven** | 3.8+ | Gestion des dépendances |

## 🚀 **Installation & Démarrage**

### **Prérequis**
- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Clé API Mistral AI

### **1. Cloner le projet**
```bash
git clone https://github.com/GabrielMorand/chess-api.git
cd chess-api
```

### **2. Configuration**
Créer un fichier `application-local.properties` :
```properties
# Base de données locale
spring.datasource.url=jdbc:postgresql://localhost:5432/chess_db
spring.datasource.username=postgres
spring.datasource.password=votre_password

# JWT
jwt.secret=votre_jwt_secret_key
jwt.expiration=86400000

# Mistral AI
mistral.api.key=votre_cle_mistral
mistral.api.url=https://api.mistral.ai/v1
```

### **3. Lancer l'application**
```bash
mvn spring-boot:run
```

### **4. Accéder à l'application**
- **Interface** : http://localhost:8080
- **Swagger** : http://localhost:8080/swagger-ui.html
- **API Docs** : http://localhost:8080/v3/api-docs

## 📋 **Structure des Endpoints**

### 🔐 **Authentification**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription utilisateur |
| POST | `/api/auth/login` | Connexion et récupération JWT |

### 👥 **Utilisateurs**
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/users` | Liste des utilisateurs | Public |
| GET | `/api/users/{id}` | Détails utilisateur | Public |
| PUT | `/api/users/{id}/role` | Modifier rôle | Admin |

### ♟️ **Parties d'Échecs**
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/games` | Liste des parties | Public |
| POST | `/api/games` | Créer partie | User |
| GET | `/api/games/{id}` | Détails partie | Public |
| PUT | `/api/games/{id}` | Modifier partie | User |

### 🎯 **Coups & Moves**
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/moves/game/{gameId}` | Coups d'une partie | Public |
| POST | `/api/moves` | Enregistrer coup | User |

### 🏆 **Tournois**
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/tournaments` | Liste tournois | Public |
| POST | `/api/tournaments` | Créer tournoi | User |
| GET | `/api/tournaments/upcoming` | Tournois à venir | Public |

### 🤖 **Intelligence Artificielle**
| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| POST | `/api/ai/chat` | Chat avec l'IA | User |
| GET | `/api/ai/analyze/game/{id}` | Analyser partie | User |
| POST | `/api/ai/suggest/move/{id}` | Suggérer coup | User |
| GET | `/api/ai/quiz` | Quiz tactique | User |

## 🧪 **Exemples d'Utilisation**

### **Inscription d'un utilisateur**
```bash
curl -X POST "https://chess-api.up.railway.app/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "joueur1",
    "email": "joueur1@email.com",
    "password": "motdepasse123"
  }'
```

### **Chat avec l'IA**
```bash
curl -X POST "https://chess-api.up.railway.app/api/ai/chat" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explique-moi l'ouverture espagnole"
  }'
```

## 📊 **Modèle de Données**

### **Entités Principales**
- **User** : Utilisateurs avec rôles et statistiques
- **Game** : Parties avec joueurs et résultats
- **Move** : Coups individuels en notation algébrique
- **Tournament** : Tournois avec participants
- **Comment** : Commentaires sur les parties
- **Ranking** : Classements et statistiques ELO

## 🔧 **Configuration Avancée**

### **Variables d'Environnement**
```bash
# Base de données
DATABASE_URL=postgresql://...
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=...

# JWT
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000

# Mistral AI
MISTRAL_API_KEY=your_mistral_key
MISTRAL_API_URL=https://api.mistral.ai/v1
```

## 📈 **Performance & Monitoring**

- **Health Check** : `/actuator/health`
- **Métriques** : `/actuator/metrics`
- **Connection Pool** : HikariCP optimisé
- **Logging** : Logback avec niveaux configurables

## 🚀 **Déploiement**

### **Railway (Production)**
1. Connecter le repository GitHub
2. Configurer les variables d'environnement
3. Railway détecte automatiquement Spring Boot
4. Déploiement automatique sur push

### **Docker (Optionnel)**
```dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/chess-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 👨‍💻 **Développeur**

**Gabriel Morand**
- 📧 Email : gabriel.morand@example.com
- 🎓 Projet étudiant - API REST avancée
- 🗓️ Développé en 2025

## 📝 **Licence**

Ce projet est développé dans un cadre éducatif.

---

<div align="center">

**🏆 Projet terminé avec succès ! 🏆**

Toutes les fonctionnalités demandées ont été implémentées :
✅ 6+ Resources | ✅ CRUD complet | ✅ JWT Auth | ✅ PostgreSQL | ✅ Swagger | ✅ IA Mistral | ✅ Railway Deploy

[🌐 **Voir la démo live**](https://chess-api.up.railway.app/)

</div>