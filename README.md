# KanovskyDev Portfolio Platform

A multi-purpose platform featuring:
1. **Professional Resume Showcase** - Display experience and projects  
2. **Crypto Tracker** - Real-time portfolio management  
3. **Resume Builder** - Template-based resume creation tool 

## ✨ Key Features

### Resume Showcase
- Professional experience timeline  
- Education background  
- Project highlights with live demos  

### Crypto Tracker
- Real-time price data (CoinMarketCap API)  
- Portfolio value tracking  
- Historical performance charts  

### Resume Builder  
- Multiple professional templates  
- PDF export functionality  
- Customizable sections  

## 🛠 Tech Stack  

| Component       | Technologies                          |
|-----------------|---------------------------------------|
| **Frontend**    | Vue.js, TypeScript, TailwindCSS, Nginx |
| **Backend**     | Spring Boot, Kotlin, JWT              |
| **Database**    | MySQL                                 |
| **Infra**       | Docker, Docker Compose                |

---

## 🚀 Launch the Application

### Prerequisites
- Docker Desktop ([Install](https://www.docker.com/products/docker-desktop/))
- Node.js v18+ (for frontend dev)
- JDK 17 (for backend dev)

### Quick Start
```bash
# 1. Clone repository
git clone https://github.com/HonzaKanovsky/KanovskyDev.git
cd KanovskyDev

# 2. Edit with your actual values (fill the coin market api)

# 3. Start all services
docker-compose up --build