-- DROP TABLES IF EXISTS
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS patron CASCADE;
DROP TABLE IF EXISTS borrowing_log CASCADE;
DROP TABLE IF EXISTS borrowing_record CASCADE;

-- CREATE TABLES
CREATE TABLE book (
  id SERIAL PRIMARY KEY,
  title VARCHAR(45) NOT NULL,
  author VARCHAR(45) NOT NULL,
  publication_year DATE,
  isbn VARCHAR(45) NOT NULL UNIQUE
);

CREATE TABLE patron (
  id SERIAL PRIMARY KEY,
  name VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL UNIQUE,
  mobile VARCHAR(45) NOT NULL
);

CREATE TABLE borrowing_log (
  book_id INT NOT NULL,
  patron_id INT NOT NULL,
  UNIQUE (book_id),
  FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (patron_id) REFERENCES patron (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE borrowing_record (
  book_id INT NOT NULL,
  patron_id INT NOT NULL,
  borrow_date DATE NOT NULL,
  return_date DATE,
  PRIMARY KEY (book_id, patron_id, borrow_date),
  FOREIGN KEY (book_id) REFERENCES book (id) ON DELETE CASCADE,
  FOREIGN KEY (patron_id) REFERENCES patron (id) ON DELETE CASCADE
);
CREATE INDEX borrowing_record_book_idx ON borrowing_record (book_id);
CREATE INDEX borrowing_record_book_return_date_idx ON borrowing_record (book_id, return_date);

