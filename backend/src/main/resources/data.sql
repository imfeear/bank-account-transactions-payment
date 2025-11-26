-- CREATING TABLE Status
CREATE TABLE IF NOT EXISTS status (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL,
    code SMALLINT NOT NULL UNIQUE
    );

-- CREATING TABLE Transaction Type
CREATE TABLE IF NOT EXISTS transaction_type (
                                                id BIGSERIAL PRIMARY KEY,
                                                transaction_type VARCHAR(20) NOT NULL,
    code INT
    );

-- TRANSACTIONS
INSERT INTO status (name, code)
SELECT 'Complete', 1 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 1);
INSERT INTO status (name, code)
SELECT 'Pending', 2 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 2);
INSERT INTO status (name, code)
SELECT 'Canceled', 3 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 3);

-- TRANSACTIONS TYPE
INSERT INTO transaction_type (code, transaction_type)
SELECT 1, 'Deposit' WHERE NOT EXISTS (SELECT 1 FROM transaction_type WHERE code = 1);
INSERT INTO transaction_type (code, transaction_type)
SELECT 2, 'Transference' WHERE NOT EXISTS (SELECT 1 FROM transaction_type WHERE code = 2);
INSERT INTO transaction_type (code, transaction_type)
SELECT 3, 'Payment' WHERE NOT EXISTS (SELECT 1 FROM transaction_type WHERE code = 3);

-- CREDIT CARD
INSERT INTO status (name, code)
SELECT 'Blocked', 40 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 40);
INSERT INTO status (name, code)
SELECT 'Inactive', 41 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 41);
INSERT INTO status (name, code)
SELECT 'Active', 42 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 42);
INSERT INTO status (name, code)
SELECT 'Canceled', 43 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 43);
INSERT INTO status (name, code)
SELECT 'Expired', 44 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 44);
INSERT INTO status (name, code)
SELECT 'Paid', 45 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 45);
INSERT INTO status (name, code)
SELECT 'Closed', 46 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 46);
INSERT INTO status (name, code)
SELECT 'Approved', 47 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 47);
INSERT INTO status (name, code)
SELECT 'Open', 48 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 48);


-- LOAN STATUS
INSERT INTO status (name, code)
SELECT 'Completed', 1 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 1);
INSERT INTO status (name, code)
SELECT 'Credit successfully evaluated', 20 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 20);
INSERT INTO status (name, code)
SELECT 'Error in credit evaluation', 21 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 21);
INSERT INTO status (name, code)
SELECT 'Invalid evaluation', 22 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 22);
INSERT INTO status (name, code)
SELECT 'In process', 25 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 25);
INSERT INTO status (name, code)
SELECT 'Active', 26 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 26);
INSERT INTO status (name, code)
SELECT 'Finalized', 27 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 27);
INSERT INTO status (name, code)
SELECT 'Canceled', 28 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 28);
INSERT INTO status (name, code)
SELECT 'Overdue', 29 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 29);
INSERT INTO status (name, code)
SELECT 'Processing failure', 30 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 30);
INSERT INTO status (name, code)
SELECT 'Paid', 32 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 32);
INSERT INTO status (name, code)
SELECT 'Outstanding', 33 WHERE NOT EXISTS (SELECT 1 FROM status WHERE code = 33);
