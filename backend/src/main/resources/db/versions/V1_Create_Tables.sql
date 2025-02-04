-- Step 1: Create the Database
CREATE DATABASE portfolio_tracker;

-- Use the database
USE portfolio_tracker;

-- Step 2: Create the Entities as Tables

-- Table: User
CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE
);

-- Table: Crypto
CREATE TABLE Crypto (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        symbol VARCHAR(10) NOT NULL UNIQUE,
                        name VARCHAR(100) NOT NULL,
                        price DECIMAL(18, 2) NOT NULL,
                        market_cap BIGINT
);

-- Table: PortfolioEntry
CREATE TABLE PortfolioEntry (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                userID BIGINT NOT NULL,
                                cryptoID BIGINT NOT NULL,
                                amount DECIMAL(18, 4) NOT NULL,
                                timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (userID) REFERENCES User(id) ON DELETE CASCADE,
                                FOREIGN KEY (cryptoID) REFERENCES Crypto(id) ON DELETE CASCADE
);

-- Table: HistorisationCryptoPrice
CREATE TABLE HistorisationCryptoPrice (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          cryptoID BIGINT NOT NULL,
                                          timestamp DATETIME NOT NULL,
                                          price DECIMAL(18, 4) NOT NULL,
                                          FOREIGN KEY (cryptoID) REFERENCES Crypto(id) ON DELETE CASCADE
);

-- Step 3: Optional Indexes for Performance
-- Index for PortfolioEntry by userID
CREATE INDEX idx_userID ON PortfolioEntry(userID);

-- Index for PortfolioEntry by cryptoID
CREATE INDEX idx_cryptoID ON PortfolioEntry(cryptoID);

-- Composite Index for HistorisationCryptoPrice
CREATE INDEX idx_cryptoID_timestamp ON HistorisationCryptoPrice(cryptoID, timestamp);
