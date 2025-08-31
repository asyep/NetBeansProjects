drop database finova_desktop;
create database finova_desktop;
use finova_desktop;

CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100)
);
CREATE TABLE account (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_type VARCHAR(50),
    balance DECIMAL(10, 2),
    liabilities DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE income_source (
    source_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    source_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE income (
    income_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    income_date DATE,
    income_source VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);

CREATE TABLE expense_category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
	user_id INT,
    category_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE expense (
	expense_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    expense_date DATE,
    expense_category VARCHAR(100),
    remark VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (account_id) REFERENCES account(account_id)
);


delimiter //
create trigger beforeAccountdelete
before delete on account
for each row

begin
	if OLD.account_id is not null THEN
		delete from expense where account_id = OLD.account_id;
		delete from income where account_id = OLD.account_id;
        delete from transaction where account_id = OLD.account_id;
	END IF;
end;
//

delimiter ;



CREATE TABLE transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    type varchar(10),
    amount decimal(10,2),
	statement VARCHAR(255),
    time TIMESTAMP,
    foreign key (account_id) references account(account_id)
);


DELIMITER //

CREATE TRIGGER afterIncomeInsert
AFTER INSERT ON income
FOR EACH ROW
BEGIN
    INSERT INTO transaction(account_id, type, amount,statement, time) VALUES
        (NEW.account_id, 'Income',NEW.amount,CONCAT('Income recorded: ', NEW.income_source, ' - Amount: ', NEW.amount), CURRENT_TIMESTAMP);
END;
//

CREATE TRIGGER afterExpenseInsert
AFTER INSERT ON expense
FOR EACH ROW
BEGIN
    INSERT INTO transaction(account_id, type, amount, statement, time) VALUES
        (NEW.account_id, 'Expense',NEW.amount, CONCAT('Expense recorded: ', NEW.expense_category, ' - Amount: ', NEW.amount), CURRENT_TIMESTAMP);
END;

//

DELIMITER ;

CREATE TABLE budget (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    expense_category VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE target_amount (
    target_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

-- Populate the user table with the admin user
-- (Assuming this is run on a fresh database or the user doesn't exist yet)
INSERT INTO user (name, username, password)
SELECT * FROM (SELECT 'Administrator' AS name, 'admin' AS username, '$2a$12$5Q8gN6nxXGSDs3CrLHDv.O2vHSZmcOIbOqqYlrEaKowQ7Br62nVTO' AS password) AS tmp
WHERE NOT EXISTS (SELECT username FROM user WHERE username = 'admin');

-- Populate account table for user_id = 1
-- (Assuming user_id = 1 corresponds to 'admin')
INSERT INTO account (user_id, account_type, balance, liabilities)
SELECT 1, 'Savings Account', 5000.00, 200.00
WHERE NOT EXISTS (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account');
INSERT INTO account (user_id, account_type, balance, liabilities)
SELECT 1, 'Checking Account', 1500.00, 50.00
WHERE NOT EXISTS (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account');
INSERT INTO account (user_id, account_type, balance, liabilities)
SELECT 1, 'Investment Portfolio', 12000.00, 0.00
WHERE NOT EXISTS (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Investment Portfolio');

-- Populate income_source table for user_id = 1
INSERT INTO income_source (user_id, source_name)
SELECT 1, 'Monthly Salary'
WHERE NOT EXISTS (SELECT source_id FROM income_source WHERE user_id = 1 AND source_name = 'Monthly Salary');
INSERT INTO income_source (user_id, source_name)
SELECT 1, 'Freelance Web Design'
WHERE NOT EXISTS (SELECT source_id FROM income_source WHERE user_id = 1 AND source_name = 'Freelance Web Design');
INSERT INTO income_source (user_id, source_name)
SELECT 1, 'Stock Dividends'
WHERE NOT EXISTS (SELECT source_id FROM income_source WHERE user_id = 1 AND source_name = 'Stock Dividends');
INSERT INTO income_source (user_id, source_name)
SELECT 1, 'Consulting Gig'
WHERE NOT EXISTS (SELECT source_id FROM income_source WHERE user_id = 1 AND source_name = 'Consulting Gig');


-- Populate expense_category table for user_id = 1
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Groceries'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Groceries');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Utilities (Electricity, Water)'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Utilities (Electricity, Water)');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Rent/Mortgage'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Rent/Mortgage');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Transportation (Fuel, Public Transit)'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Transportation (Fuel, Public Transit)');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Dining Out'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Dining Out');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Entertainment'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Entertainment');
-- NEW Expense Categories
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Healthcare (Pharmacy, Doctor)'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Healthcare (Pharmacy, Doctor)');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Shopping (Clothes, Goods)'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Shopping (Clothes, Goods)');
INSERT INTO expense_category (user_id, category_name)
SELECT 1, 'Education (Courses, Books)'
WHERE NOT EXISTS (SELECT category_id FROM expense_category WHERE user_id = 1 AND category_name = 'Education (Courses, Books)');


-- PREVIOUSLY ADDED DATA (June/July for context)
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-06-01', 'Monthly Salary', 3500.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-05', 'Freelance Web Design', 750.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Investment Portfolio' LIMIT 1), '2025-06-10', 'Stock Dividends', 120.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-07-01', 'Monthly Salary', 3500.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-02', 'Groceries', 'Weekly supermarket run', 120.50),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-03', 'Utilities (Electricity, Water)', 'Monthly electricity bill', 85.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-06-05', 'Rent/Mortgage', 'Monthly rent payment', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-07', 'Transportation (Fuel, Public Transit)', 'Fuel for car', 60.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-12', 'Dining Out', 'Dinner with friends', 75.20),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-10', 'Entertainment', 'Movie tickets', 30.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-06-11', 'Groceries', 'Mid-month grocery top-up', 55.75);

-- NEW DATA: Income and Expenses from January to May 2025

-- JANUARY 2025
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-01-01', 'Monthly Salary', 3400.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-15', 'Freelance Web Design', 650.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-01-05', 'Rent/Mortgage', 'Monthly rent', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-06', 'Utilities (Electricity, Water)', 'Jan Utilities', 90.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-10', 'Groceries', 'Weekly Groceries', 110.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-18', 'Transportation (Fuel, Public Transit)', 'Monthly pass', 70.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-25', 'Dining Out', 'Weekend Dinner', 60.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-01-28', 'Shopping (Clothes, Goods)', 'Winter Coat', 150.00);

-- FEBRUARY 2025
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-02-01', 'Monthly Salary', 3400.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-02-14', 'Freelance Web Design', 700.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-02-05', 'Rent/Mortgage', 'Monthly rent', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-02-07', 'Utilities (Electricity, Water)', 'Feb Utilities', 88.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-02-12', 'Groceries', 'Bi-Weekly Groceries', 95.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-02-20', 'Healthcare (Pharmacy, Doctor)', 'Doctors Visit Co-pay', 40.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-02-26', 'Entertainment', 'Concert Ticket', 80.00);

-- MARCH 2025
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-03-01', 'Monthly Salary', 3450.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-16', 'Freelance Web Design', 680.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Investment Portfolio' LIMIT 1), '2025-03-20', 'Stock Dividends', 130.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-03-05', 'Rent/Mortgage', 'Monthly rent', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-08', 'Utilities (Electricity, Water)', 'Mar Utilities', 92.50),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-11', 'Groceries', 'Weekly Groceries', 115.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-19', 'Transportation (Fuel, Public Transit)', 'Fuel top-up', 55.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-24', 'Dining Out', 'Birthday Dinner', 90.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-03-29', 'Education (Courses, Books)', 'Online Course Subscription', 49.99);


-- APRIL 2025
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-04-01', 'Monthly Salary', 3450.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-14', 'Consulting Gig', 900.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-04-05', 'Rent/Mortgage', 'Monthly rent', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-06', 'Utilities (Electricity, Water)', 'Apr Utilities', 85.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-10', 'Groceries', 'Stocking up pantry', 130.25),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-17', 'Shopping (Clothes, Goods)', 'New shoes', 75.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-25', 'Entertainment', 'Weekend trip contribution', 150.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-04-28', 'Groceries', 'Quick grocery run', 45.50);

-- MAY 2025
INSERT INTO income (user_id, account_id, income_date, income_source, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-05-01', 'Monthly Salary', 3500.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-15', 'Freelance Web Design', 720.00);

INSERT INTO expense (user_id, account_id, expense_date, expense_category, remark, amount) VALUES
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Savings Account' LIMIT 1), '2025-05-05', 'Rent/Mortgage', 'Monthly rent', 1200.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-07', 'Utilities (Electricity, Water)', 'May Utilities', 93.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-11', 'Groceries', 'Farmers market haul', 85.60),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-18', 'Transportation (Fuel, Public Transit)', 'Gas for car', 65.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-22', 'Healthcare (Pharmacy, Doctor)', 'Prescription refill', 30.00),
(1, (SELECT account_id FROM account WHERE user_id = 1 AND account_type = 'Checking Account' LIMIT 1), '2025-05-28', 'Dining Out', 'Client Lunch', 50.00);

-- Populate budget table for user_id = 1 (can be adjusted or expanded based on new categories)
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Groceries', 450.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Groceries');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Utilities (Electricity, Water)', 150.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Utilities (Electricity, Water)');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Rent/Mortgage', 1200.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Rent/Mortgage');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Transportation (Fuel, Public Transit)', 200.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Transportation (Fuel, Public Transit)');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Dining Out', 250.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Dining Out');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Entertainment', 150.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Entertainment');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Healthcare (Pharmacy, Doctor)', 100.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Healthcare (Pharmacy, Doctor)');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Shopping (Clothes, Goods)', 200.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Shopping (Clothes, Goods)');
INSERT INTO budget (user_id, expense_category, amount)
SELECT 1, 'Education (Courses, Books)', 75.00
WHERE NOT EXISTS (SELECT budget_id FROM budget WHERE user_id = 1 AND expense_category = 'Education (Courses, Books)');


-- Populate target_amount table for user_id = 1
INSERT INTO target_amount (user_id, amount)
SELECT 1, 10000.00
WHERE NOT EXISTS (SELECT target_id FROM target_amount WHERE user_id = 1 AND amount = 10000.00);

-- The 'transaction' table will be populated automatically by triggers.
-- You can verify with:
-- SELECT * FROM transaction t JOIN account a ON t.account_id = a.account_id WHERE a.user_id = 1 ORDER BY t.time;
-- SELECT * FROM user;
-- SELECT * FROM account WHERE user_id = 1;
