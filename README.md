# Sparrow Air 2 - PostgreSQL Database Guide

This guide provides instructions on how to connect to the PostgreSQL database running in Docker and view the database tables.

## Database Configuration

The PostgreSQL database is configured with the following parameters:

- **Database Name**: mydatabase
- **Username**: myuser
- **Password**: secret
- **Port**: 5432 (mapped from container to host)

## Connecting to the PostgreSQL Container

### 1. Check Running Containers

First, ensure that the Docker containers are running:

```bash
docker ps
```

Look for a container running the `postgres:latest` image.

### 2. Connect to the PostgreSQL Container

You can connect to the PostgreSQL container using the following command:

```bash
docker exec -it <container_id> bash
```

Replace `<container_id>` with the actual container ID from the `docker ps` command. Alternatively, you can use the container name:

```bash
docker exec -it sparrow-air-2-postgres-1 bash
```

## Logging into PostgreSQL

Once you're inside the container, you can log into PostgreSQL using the `psql` command-line tool:

```bash
psql -U myuser -d mydatabase
```

When prompted, enter the password: `secret`

## Viewing Database Tables

After logging into PostgreSQL, you can use the following commands to explore the database:

### List All Tables

```sql
\dt
```

This will display all tables in the current database schema.

### View Table Structure

To view the structure of a specific table:

```sql
\d+ table_name
```

Replace `table_name` with one of the following tables:
- airlines
- airports
- aircraft_types
- flights
- passengers
- bookings
- seats
- booking_segments

### Query Table Data

You can run SQL queries to view the data in the tables:

```sql
SELECT * FROM table_name;
```

### Common SQL Commands

Here are some useful SQL commands to explore the database:

```sql
-- List all tables
\dt

-- View detailed information about a table
\d+ table_name

-- Count records in a table
SELECT COUNT(*) FROM table_name;

-- View all data in a table
SELECT * FROM table_name;

-- Exit PostgreSQL
\q
```

## Exiting

To exit the PostgreSQL prompt, type:

```sql
\q
```

To exit the container bash shell, type:

```bash
exit
```

## Database Schema

The database contains the following tables:
1. airlines
2. airports
3. aircraft_types
4. flights
5. passengers
6. bookings
7. seats
8. booking_segments

These tables are created and managed using Liquibase migrations defined in the application.


