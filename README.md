# Insurance Policies Application

## Overview

This project is a Spring Boot app using Axon Server and PostgreSQL.  
It uses Docker and Docker Compose for local development.

## Prerequisites

- Java 17+ installed
- Docker installed and running
- Docker Compose plugin installed
- Maven Wrapper (`./mvnw`) present

## Makefile Commands

### Build

    make build-app          # Compile and package Spring Boot app
    make build-docker-app   # Build Docker image for the app

### Start Services

    make start-docker-all   # Start PostgreSQL, Axon Server, and app containers
### Stop Services

    make stop-docker-all    # Stop and remove all containers

### Individual Services

    make start-local-database   # Start PostgreSQL container
    make stop-local-database    # Stop PostgreSQL container
    make start-axon-server      # Start Axon Server container
    make stop-axon-server       # Stop Axon Server container
    make start-app              # Start Spring Boot app container
    make stop-app               # Stop and remove Spring Boot app container make restart-app            # Restart Spring Boot app container


## Ports

- PostgreSQL: `localhost:5434`
- Axon Server Dashboard: `localhost:8024`
- Axon Server Client Port: `8124`
- Spring Boot App: `localhost:8080`

## Notes

- The Makefile checks for Java, Docker, and Docker Compose before running.
- Environment variables can be loaded from `.env.local`.
- Docker Compose file used by combined commands is `compose.yml`.

---

For questions or issues, contact [tbib.samir@gmail.com].
