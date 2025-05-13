CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    balance DOUBLE PRECISION
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    crypto_symbol VARCHAR(10),
    crypto_name VARCHAR(50),
    quantity DOUBLE PRECISION,
    price DOUBLE PRECISION,
    total DOUBLE PRECISION,
    type VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE holdings (
      id SERIAL PRIMARY KEY,
      user_id INTEGER REFERENCES users(id),
      crypto_symbol VARCHAR(10),
      quantity DOUBLE PRECISION
);