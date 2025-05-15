# Web application that simulates a cryptocurrency trading platform
This project simulates a web platform that provides a listing of the top 20 crypto currencies at the current moment of use. Users can buy and sell those currencies.


## Tech stack

### Frontend
- **React**: JavaScript library for building the user interface  
- **Vite**: Next-generation build tool for fast frontend development  
- **TypeScript**: JavaScript superset that adds static typing  
- **TailwindCSS**: Utility-first CSS framework for styling

### Backend
- **Spring Boot**: Java framework for building the backend API 
- **Spring JDBC**: Simplifies data access and persistence using plain JDBC and Spring Data  
- **TypeScript**: JavaScript superset that adds static typing  
- **TailwindCSS**: Utility-first CSS framework for styling

### Database
- **PostgreSQL (Dockerized)**: A PostgreSQL database instance is hosted in a Docker container

## Communication
The backend extracts real-time data about the crypto currencies throughout a web socket handshake with Kraken V2 WebSocket API (Ticker (Level 1). A communication is established between the FE and BE again by web sockets.
