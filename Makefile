.PHONY: default help start-local-databases stop-local-databases start-app stop-app start-axon-server stop-axon-server

.PHONY: build-app build-docker-app start-docker-all stop-docker-all

DEFAULT_ENVIRONMENT := production
PROJECT_NAME=$(shell basename "$(PWD)")

## Default Target
default: help

define localEnv
	set -o allexport && . ./.env.local && set +o allexport
endef
# -----------------------------------------
# Database Commands
# -----------------------------------------

## start-local-database: Start local PostgreSQL database using Docker
start-local-database: check-docker check-docker-compose
	@echo "[INFO] Starting local PostgreSQL database."
	@docker compose -f docker-compose/postgres.yml up -d --build || true

## stop-local-database: Stop local PostgreSQL database using Docker
stop-local-database: check-docker check-docker-compose
	@echo "[INFO] Stopping local PostgreSQL database."
	@docker compose -f docker-compose/postgres.yml down || true

# -----------------------------------------
# Axon Server Commands
# -----------------------------------------

## start-axon-server: Start Axon Server using Docker
start-axon-server: check-docker
	@echo "[INFO] Starting Axon Server."
	@docker run --rm --name axon-server \
		-p 8024:8024 -p 8124:8124 \
		-d axoniq/axonserver:latest

## stop-axon-server: Stop Axon Server running in Docker
stop-axon-server: check-docker
	@echo "[INFO] Stopping Axon Server."
	@docker stop axon-server || true

# -----------------------------------------
# Application Commands
# -----------------------------------------

## build-app: Build the Spring Boot application
build-app: check-java
	@echo "[INFO] Compiling and packaging Spring Boot application."
	@$(call localEnv) && ./mvnw clean install -DskipTests
	@$(call localEnv) && ./mvnw clean package -DskipTests -pl presentation

## build-docker-app: Build the Docker image for the Spring Boot application
build-docker-app: build-app check-docker
	@echo "[INFO] Building Docker image for the application."
	@$(call localEnv) && docker build -f ./presentation/Dockerfile -t insurance-policies-app:latest ./presentation

## start-app: Start the Spring Boot application in Docker
start-app: check-docker
	@echo "[INFO] Starting application in Docker."
	@$(call localEnv) && docker run --name ${PROJECT_NAME}-app \
		--env SPRING_PROFILES_ACTIVE=default \
		-p 8080:8080 \
		-d ${PROJECT_NAME}-app:latest

## stop-app: Stop the running Spring Boot application in Docker
stop-app: check-docker
	@echo "[INFO] Stopping application in Docker."
	@docker stop ${PROJECT_NAME}-app || true
	@docker rm ${PROJECT_NAME}-app || true

## restart-app: Stop and Start the Spring Boot application in Docker
restart-app: stop-app start-app
	@echo "[INFO] Application restarted."
# -----------------------------------------
# Combined Commands
# -----------------------------------------
## Start all services (Postgres, Axon Server, App) using Docker Compose
start-docker-all: check-docker check-docker-compose build-docker-app
	@echo "[INFO] Starting PostgreSQL, Axon Server, and Application."
	@docker compose -f compose.yml up  -d

## Stop all services
stop-docker-all: check-docker check-docker-compose
	@echo "[INFO] Stopping all services."
	@docker compose -f compose.yml down
# -----------------------------------------
# Utility Commands
# -----------------------------------------

## check-java: Verify if Java is installed
check-java:
	@if ! java -version > /dev/null 2>&1; then \
		echo "[ERROR] Java not found. Install Java and try again." >&2; \
		exit 1; \
	else \
		echo "[INFO] Java is installed."; \
	fi

## check-docker: Verify if Docker is installed
check-docker:
	@if ! docker -v > /dev/null 2>&1; then \
		echo "[ERROR] Docker not found. Install Docker and try again." >&2; \
		exit 1; \
	else \
		echo "[INFO] Docker is installed."; \
	fi

## check-docker-compose: Verify if Docker Compose is available
check-docker-compose:
	@if ! docker compose version > /dev/null 2>&1; then \
		echo "[ERROR] Docker Compose plugin not found. Install Docker Compose and try again." >&2; \
		exit 1; \
	else \
		echo "[INFO] Docker Compose is available."; \
	fi

## help: Display available commands
help:
	@echo ""
	@echo "Available commands for $(PROJECT_NAME):"
	@grep -E '^## [a-zA-Z0-9_-]+:' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ": "} {print $\$2}'
	@echo ""
