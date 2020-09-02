-- USERS

-- the below password is saved using BcryptUtil.bcryptHash(..) method
-- encoded password is: passw0rd
INSERT INTO users (id, username, password, role) VALUES (1, 'TU001', '$2a$10$GYPRO6RJh81MgGq9ytg9peFYkkr2DRlLexrxVy4Sm/LVtTEdTAeDW', 'TECHNICAL_USER');

-- BOOKS

INSERT INTO books (id, isbn, title, description, booklanguage, image_url, unit_cost, pages, publication_date)
VALUES (1, '13-978-0984782857', 'Cracking the Coding Interview', 'Cracking the Coding Interview, 6th Edition is here to help you through this process, teaching you what you need to know and enabling you to perform at your very best. I''ve coached and interviewed hundreds of software engineers. The result is this book.
', 'ENGLISH', 'http://www.crackingthecodinginterview.com', 26.99, 687, '2015-07-01');
INSERT INTO books (id, isbn, title, description, booklanguage, image_url, unit_cost, pages, publication_date)
VALUES (2, '13-978-1593279288', 'Python Crash Course, 2nd Edition', 'Python Crash Course is the world''s best-selling guide to the Python programming language. This fast-paced, thorough introduction to programming with Python will have you writing programs, solving problems, and making things that work in no time.', 'PORTUGUESE', 'https://nostarch.com/pythoncrashcourse2e', 22.99, 544, '2019-05-03');
INSERT INTO books (id, isbn, title, description, booklanguage, image_url, unit_cost, pages, publication_date)
VALUES (3, '13-978-0135957059', 'The Pragmatic Programmer', 'The Pragmatic Programmer is one of those rare tech books you’ll read, re-read, and read again over the years. Whether you’re new to the field or an experienced practitioner, you’ll come away with fresh insights each and every time.', 'ITALIAN', 'https://pragprog.com', 31.99, 352, '2019-09-23');