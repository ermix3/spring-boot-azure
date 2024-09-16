-- Create the uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insert initial customers
INSERT INTO customer (name, email)
VALUES ('Hassan', 'hassan@gmail.com'),
       ('Imane', 'imane@gmail.com'),
       ('Mohamed', 'mohamed@gmail.com');

-- Insert initial bank accounts and capture the IDs
WITH inserted_accounts AS (
    INSERT INTO bank_account (id, balance, created_at, status, customer_id, over_draft, interest_rate)
        VALUES (UUID_generate_v4(), random() * 90000, NOW(), 'CREATED', 1, 8000, 3.2),
               (UUID_generate_v4(), random() * 120000, NOW(), 'CREATED', 1, 4000, 5.5),
               (UUID_generate_v4(), random() * 90000, NOW(), 'CREATED', 2, 6000, 2.2),
               (UUID_generate_v4(), random() * 120000, NOW(), 'CREATED', 2, 2000, 9.5),
               (UUID_generate_v4(), random() * 90000, NOW(), 'CREATED', 3, 9000, 3.3),
               (UUID_generate_v4(), random() * 120000, NOW(), 'CREATED', 3, 1000, 15.5)
        RETURNING id)

-- Insert initial operations using the captured IDs
INSERT
INTO account_operation (operation_date, amount, type, bank_account_id, description)
SELECT NOW(), 1000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts
UNION ALL
SELECT NOW(), 2000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts
UNION ALL
SELECT NOW(), 3000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts
UNION ALL
SELECT NOW(), 4000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts
UNION ALL
SELECT NOW(), 5000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts
UNION ALL
SELECT NOW(), 6000, 'DEPOSIT', id, 'Initial deposit'
FROM inserted_accounts;