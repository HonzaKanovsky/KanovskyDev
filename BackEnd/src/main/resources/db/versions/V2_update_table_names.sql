-- Step 1: Modify Columns to Support 8 Decimal Digits
ALTER TABLE PortfolioEntry MODIFY COLUMN amount DOUBLE(18, 8) NOT NULL;
ALTER TABLE HistorisationCryptoPrice MODIFY COLUMN price DOUBLE(18, 8) NOT NULL;
ALTER TABLE Crypto MODIFY COLUMN price DOUBLE(18, 8) NOT NULL;

-- Step 2: Rename Tables to snake_case
RENAME TABLE PortfolioEntry TO portfolio_entry;
RENAME TABLE HistorisationCryptoPrice TO historisation_crypto_price;
