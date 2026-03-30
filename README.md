# Budget Tracker

A small fullstack app to track income and expenses.

<img width="1136" height="1276" alt="app-image" src="https://github.com/user-attachments/assets/7d0e1d43-f3b6-4f7a-a7e4-1b883e7cc07c" />

## What it does

* register / login with JWT
* add income and expense transactions
* filter by type, category and month
* shows total income, expenses and balance

## How to run

Make sure you have Docker installed.

```bash
docker compose up --build
```

Open in browser:

```
http://localhost:5173
```

## Notes

* backend runs on port 8080
* frontend runs on port 5173
* database is PostgreSQL (via Docker)
