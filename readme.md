# ♟️ Chess API - Projet Spring Boot avec IA

Une API complète pour la gestion d'échecs avec intelligence artificielle intégrée via Mistral AI.

## 🚀 **Technologies**

| **Technologie** | **Version** | **Description** |
|-----------------|-------------|-----------------|
| **Java** | 17+ | Langage principal |
| **Spring Boot** | 3.5.0 | Framework principal |
| **Spring Security** | 6.x | Authentification JWT |
| **Spring Data JPA** | 3.x | Accès aux données |
| **PostgreSQL** | 15+ | Base de données |
| **Mistral AI** | API | Intelligence artificielle |
| **Swagger/OpenAPI** | 3.0 | Documentation |
| **Railway** | - | Déploiement cloud |
| **Maven** | 3.8+ | Gestion des dépendances |

## 🏗️ **Architecture**

```
src/
├── main/java/com/chesslearning/chess_api/
│   ├── config/          # Configuration (JWT, Mistral, WebClient)
│   ├── controller/      # Contrôleurs REST
│   ├── dto/            # Objets de transfert de données
│   ├── entity/         # Entités JPA
│   ├── repository/     # Repositories Spring Data
│   ├── service/        # Logique métier
│   └── security/       # Configuration sécurité
└── resources/
    ├── application.properties
    └── static/
```

## 🚀 **Installation & Démarrage**

### **Prérequis**
- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Clé API Mistral AI ([console.mistral.ai](https://console.mistral.ai))

### **1. Cloner le projet**
```bash
git clone https://github.com/GabrielMorand/chess-api.git
cd chess-api
```

### **2. Configuration locale**
Créer un fichier `application-local.properties` :
```properties
# Base de données locale
spring.datasource.url=jdbc:postgresql://localhost:5432/chess_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# Mistral AI
mistral.api.key=your_mistral_api_key_here
mistral.api.url=https://api.mistral.ai/v1
mistral.model=mistral-small
```

### **3. Lancer l'application**
```bash
mvn spring-boot:run
```

### **4. Accéder à l'application**
- **API Locale** : http://localhost:8080
- **API Production** : https://chess-api.up.railway.app
- **Swagger UI** : /swagger-ui.html
- **API Docs** : /v3/api-docs

## 📋 **Endpoints API**

### 🔐 **Authentification**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/auth/register` | Créer un compte |
| `POST` | `/api/auth/login` | Se connecter |
| `POST` | `/api/auth/refresh` | Renouveler le token |

### 👤 **Utilisateurs**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/users/me` | Profil utilisateur |
| `PUT` | `/api/users/me` | Modifier le profil |
| `GET` | `/api/users/{id}/stats` | Statistiques joueur |

### ♟️ **Parties**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/games` | Créer une partie |
| `GET` | `/api/games` | Lister les parties |
| `GET` | `/api/games/{id}` | Détails d'une partie |
| `PUT` | `/api/games/{id}` | Mettre à jour une partie |

### 🎯 **Coups**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/moves` | Jouer un coup |
| `GET` | `/api/moves/game/{gameId}` | Coups d'une partie |

### 🤖 **Intelligence Artificielle**
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/ai/chat` | Chat avec l'IA échecs |
| `POST` | `/api/ai/analyze` | Analyser une partie |
| `POST` | `/api/ai/suggest-move` | Suggérer un coup |
| `POST` | `/api/ai/explain-move` | Expliquer un coup |
| `POST` | `/api/ai/quiz` | Générer un quiz tactique |
| `POST` | `/api/ai/improvement-tips` | Conseils d'amélioration |
| `GET` | `/api/ai/health` | État du service IA |

## 🧪 **Collection Postman**

### **Variables d'environnement Postman :**
```json
{
  "baseUrl": "https://chess-api.up.railway.app",
  "localUrl": "http://localhost:8080",
  "token": "{{authToken}}"
}
```

### **1. Authentification**

#### **Inscription**
```json
POST {{baseUrl}}/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

#### **Connexion**
```json
POST {{baseUrl}}/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}

// Script Post-Response Postman :
pm.test("Login successful", function () {
    pm.response.to.have.status(200);
    const responseJson = pm.response.json();
    pm.environment.set("authToken", responseJson.token);
});
```

### **2. Tests IA Mistral**

#### **Health Check IA**
```json
GET {{baseUrl}}/api/ai/health
Authorization: Bearer {{token}}
```

#### **Chat avec l'IA**
```json
POST {{baseUrl}}/api/ai/chat
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "message": "Explique-moi la défense française aux échecs"
}
```

#### **Quiz Tactique**
```json
POST {{baseUrl}}/api/ai/quiz
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "difficulty": "débutant"
}
```

#### **Conseils d'Amélioration**
```json
POST {{baseUrl}}/api/ai/improvement-tips
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "stats": "Parties jouées: 50, Victoires: 20, Défaites: 25, Nulles: 5. ELO: 1200. Problèmes fréquents: perte de matériel en milieu de partie."
}
```

#### **Explication de Coup**
```json
POST {{baseUrl}}/api/ai/explain-move
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "move": "e4",
  "position": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
}
```

### **3. Gestion des Parties**

#### **Créer une Partie**
```json
POST {{baseUrl}}/api/games
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "playerBlackId": 2,
  "timeControl": "BLITZ"
}

// Script Post-Response :
pm.test("Game created", function () {
    pm.response.to.have.status(201);
    const responseJson = pm.response.json();
    pm.environment.set("gameId", responseJson.id);
});
```

#### **Analyser une Partie**
```json
POST {{baseUrl}}/api/ai/analyze
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "gameId": {{gameId}}
}
```

#### **Suggérer un Coup**
```json
POST {{baseUrl}}/api/ai/suggest-move
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "gameId": {{gameId}},
  "position": "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1"
}
```

### **4. Tests de Coups**

#### **Jouer un Coup**
```json
POST {{baseUrl}}/api/moves
Content-Type: application/json
Authorization: Bearer {{token}}

{
  "gameId": {{gameId}},
  "moveNotation": "e4",
  "fromSquare": "e2",
  "toSquare": "e4"
}
```

## 🔧 **Variables d'Environnement**

### **Pour Railway (Production) :**
```bash
DATABASE_URL=your_postgresql_url
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret
MISTRAL_API_KEY=your_mistral_api_key
```

### **Pour développement local :**
```bash
export MISTRAL_API_KEY=your_mistral_api_key
export JWT_SECRET=your_jwt_secret
```

## 📊 **Tests avec curl**

```bash
# Health check
curl -X GET "https://chess-api.up.railway.app/api/ai/health"

# Login et récupération du token
TOKEN=$(curl -X POST "https://chess-api.up.railway.app/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}' | jq -r '.token')

# Chat IA
curl -X POST "https://chess-api.up.railway.app/api/ai/chat" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"message":"Que penses-tu de la défense sicilienne?"}'

# Quiz tactique
curl -X POST "https://chess-api.up.railway.app/api/ai/quiz" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"difficulty":"intermédiaire"}'
```

## 🎯 **Ordre de Tests Recommandé**

1. **Health Check** - Vérifier que l'API fonctionne
2. **Register/Login** - Créer un compte et récupérer le token
3. **AI Health** - Vérifier que Mistral AI fonctionne
4. **AI Chat** - Tester la communication avec l'IA
5. **Quiz** - Tester la génération de quiz
6. **Create Game** - Créer une partie test
7. **AI Analysis** - Analyser la partie créée

## 🚀 **Déploiement**

L'application est automatiquement déployée sur Railway à chaque push sur la branche main.

**URL de production :** https://chess-api.up.railway.app

## 📝 **Contribution**

1. Fork le projet
2. Créer une branche (`git checkout -b feature/nouvelle-fonctionnalite`)
3. Commit les changements (`git commit -am 'Ajouter nouvelle fonctionnalité'`)
4. Push vers la branche (`git push origin feature/nouvelle-fonctionnalite`)
5. Créer une Pull Request

## 📄 **Licence**

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

---

**Créé avec ❤️ et ♟️ par Gabriel Morand**