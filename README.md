# Library Management System (Java + MySQL)

**Student:** RA2411003011554  |  Gummudu Venkata Prudhvi Krishna

A console-based Library Management System built using **Java** and **MySQL (JDBC)**.
This project includes a polished console UI (clear menus, input validation, search, and neatly formatted tables)
suitable for submission as an APP subject project.

## ✅ Features
- Add new books with validation
- View all books in a formatted table
- Issue and return books with status checks
- Search books by title or author (partial matches)
- Automatic table creation when connecting to the database
- Clean, user-friendly console UI with pauses and headers

## 🧾 Files
- `src/Book.java` — Book model
- `src/Library.java` — Database operations and business logic (uses JDBC)
- `src/Main.java` — Console UI and application entry point
- `library.sql` — SQL script to create database and table
- `lib/mysql-connector-j.jar` — (placeholder — add the MySQL JDBC connector here)
- `README.md`

## ⚙️ Setup (Windows example)
1. Install Java JDK and MySQL server.
2. Place the MySQL Connector/J `.jar` in the `lib/` folder.
3. Run the SQL in `library.sql` or allow the app to auto-create the table.
4. Update DB credentials in `src/Library.java` if needed (currently set to `root` / `23232323`).
5. Compile and run:

```bash
# compile
javac -cp ".;lib/mysql-connector-j.jar" src/*.java

# run
java -cp ".;lib/mysql-connector-j.jar;src" Main
```

## 📦 Uploading to GitHub
1. Initialize git, add files, commit, and push:

```bash
git init
git add .
git commit -m "Initial commit - Library Management System"
# create repo on GitHub named LibraryManagementSystem_RA2411003011554
git remote add origin https://github.com/<your-username>/LibraryManagementSystem_RA2411003011554.git
git branch -M main
git push -u origin main
```

*Developed by RA2411003011554 — Gummudu Venkata Prudhvi Krishna*
