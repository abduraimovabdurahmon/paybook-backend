-- Generating 1000 random Income records
INSERT INTO income (id, user_id, description, amount, currency, created_at)
SELECT
    gen_random_uuid(),
    '6b8c4f48-44d3-4aeb-a5fe-e4ade6458deb',
    'Income from ' || (ARRAY['Salary', 'Freelance', 'Investment', 'Gift', 'Business', 'Rental', 'Bonus', 'Side Hustle'])[floor(random() * 8 + 1)],
    round((random() * 10000000 + 10000)::numeric, 2),
    'UZS',
    timestamp '2024-01-01' + random() * (timestamp '2025-07-14' - timestamp '2024-01-01')
FROM generate_series(1, 1000);

-- Generating 1000 random Expense records
INSERT INTO expense (id, user_id, description, amount, currency, created_at)
SELECT
    gen_random_uuid(),
    '6b8c4f48-44d3-4aeb-a5fe-e4ade6458deb',
    'Expense for ' || (ARRAY['Groceries', 'Utilities', 'Rent', 'Transport', 'Entertainment', 'Dining', 'Shopping', 'Bills'])[floor(random() * 8 + 1)],
    round((random() * 5000000 + 5000)::numeric, 2),
    'UZS',
    timestamp '2024-01-01' + random() * (timestamp '2025-07-14' - timestamp '2024-01-01')
FROM generate_series(1, 1000);

-- Generating 1000 random Debt records (500 BORROW, 500 LEND)
INSERT INTO debt (id, user_id, type, description, amount, currency, is_paid, due_date, created_at)
SELECT
    gen_random_uuid(),
    '6b8c4f48-44d3-4aeb-a5fe-e4ade6458deb',
    CASE WHEN g <= 500 THEN 'BORROW' ELSE 'LEND' END,
    CASE WHEN g <= 500
             THEN 'Borrowed for ' || (ARRAY['Personal', 'Business', 'Emergency', 'Project', 'Travel'])[floor(random() * 5 + 1)]
         ELSE 'Lent for ' || (ARRAY['Friend', 'Family', 'Business', 'Project', 'Support'])[floor(random() * 5 + 1)]
        END,
    round((random() * 20000000 + 50000)::numeric, 2),
    'UZS',
    (random() > 0.7),
    timestamp '2024-01-01' + random() * (timestamp '2025-12-31' - timestamp '2024-01-01'),
    timestamp '2024-01-01' + random() * (timestamp '2025-07-14' - timestamp '2024-01-01')
FROM generate_series(1, 1000) g;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


INSERT INTO category (id, keyword, title, type, icon, bg_color, created_at) VALUES
                                                                                (uuid_generate_v4(), 'food', 'Food & Dining', 'EXPENSE', 'food-icon', '#FF5733', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'salary', 'Monthly Salary', 'INCOME', 'salary-icon', '#00FF00', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'transport', 'Transportation', 'EXPENSE', 'transport-icon', '#3366FF', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'rent', 'Rent', 'EXPENSE', 'home-icon', '#FF3366', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'utilities', 'Utilities', 'EXPENSE', 'utilities-icon', '#FFFF33', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'freelance', 'Freelance Work', 'INCOME', 'freelance-icon', '#33FFFF', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'debt', 'Debt Repayment', 'DEBT', 'debt-icon', '#9933FF', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'entertainment', 'Entertainment', 'EXPENSE', 'entertainment-icon', '#FF9900', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'investment', 'Investment Income', 'INCOME', 'investment-icon', '#00CC99', '2025-07-15 15:15:00'),
                                                                                (uuid_generate_v4(), 'shopping', 'Shopping', 'EXPENSE', 'shopping-icon', '#CC33FF', '2025-07-15 15:15:00');