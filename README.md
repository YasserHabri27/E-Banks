# 🏦 eBank - Habri & Allali Bank

## 📋 Description
Application de gestion bancaire complète avec Spring Boot 3 et React.

## 🛠 Technologies
- **Backend** : Spring Boot 3, Security JWT, JPA, MySQL
- **Frontend** : React 18, Material-UI, Axios, React Router
- **Base de données** : MySQL 8
- **Sécurité** : JWT, BCrypt, CORS

## 🚀 Installation rapide

### 1. Base de données
```bash
# Se connecter à MySQL et exécuter le script
mysql -u root -p < database/ebank_database.sql
```

### 2. Backend
```bash
cd backend
mvn spring-boot:run
```
⚠️ **Important** : Le backend écoute sur le port `8080`.

### 3. Frontend
```bash
cd backend/ebank-frontend
npm install
npm run dev
```
Accéder à l'application via `http://localhost:5173`.

---

## 📧 Configuration Email (RG_7)

Le service d'email supporte deux modes : **Simulation** (Console) et **Réel** (Gmail SMTP).

### Configuration (`application.properties`)

#### 1. Mode Simulation (Pour la Démo)
Idéal pour la présentation en classe. Les emails sont affichés joliment dans la console du backend.
```properties
app.email.simulation.mode=true
app.email.simulation.console.output=true
```

#### 2. Mode Réel (SMTP)
Pour envoyer de vrais emails.
```properties
app.email.simulation.mode=false
spring.mail.username=yasser.habri.dev2@gmail.com
# Utiliser le mot de passe d'application généré (vireyzlkumpbpfrc)
spring.mail.password=vireyzlkumpbpfrc
```

---

## 👤 Comptes de démonstration

### AGENT GUICHET
- **Email** : `agent1@habriallalibank.ma`
- **Mot de passe** : `Agent123!`

### CLIENT
- **Email** : `yasser.habri@email.ma`
- **Mot de passe** : `Client123!`

---

## ✅ Fonctionnalités implémentées

### UC-1 : Authentification
- Deux rôles : CLIENT et AGENT_GUICHET
- JWT valide 1 heure
- Changement de mot de passe

### UC-2 : Ajout client (Agent)
- Formulaire complet avec validation
- **Envoi d'email automatique avec les identifiants** (RG_7)
- Vérification unicité email/identité

### UC-3 : Création compte (Agent)
- Génération RIB automatique
- Association au client
- Statut "Ouvert" par défaut

### UC-4 : Tableau de bord client
- Affichage solde et RIB
- 10 dernières opérations
- Gestion multi-comptes
- Pagination

### UC-5 : Virement client
- Validation solde suffisant
- Vérification compte non bloqué
- Traçabilité complète

---

## 🧪 Tests

### Test cURL : Créer Client (et déclencher l'email)
```bash
curl -X POST http://localhost:8080/api/agent/clients \
  -H "Authorization: Bearer <VOTRE_TOKEN_AGENT>" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test",
    "prenom": "Email",
    "numeroIdentite": "TEST123",
    "dateNaissance": "1990-01-01",
    "email": "yasser.habri.dev2@gmail.com",
    "adressePostale": "Test address",
    "telephone": "+212600000000"
  }'
```

Le mot de passe sera généré aléatoirement et envoyé par email (ou affiché console).

---

## 👥 Support
**Yasser Habri & Doha Allali**
Projet académique - Gestion de projet
