#!/bin/sh

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL

    CREATE USER "insurance-policies" WITH PASSWORD 'insurance-policies';
    CREATE DATABASE "insurance-policies";
    GRANT ALL PRIVILEGES ON DATABASE "insurance-policies" TO "insurance-policies";

    \c insurance-policies
    GRANT CREATE ON SCHEMA public TO "insurance-policies";

EOSQL