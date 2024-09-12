#!/bin/bash

COMPOSE_DIR="./docker"

COMPOSE_FILES=(
    "docker-compose.network.yml"
    "docker-compose.company.yml"
    "docker-compose.order.yml"
    "docker-compose.user.yml"
    "docker-compose.gateway.yml"
    "docker-compose.eureka.yml"
    "docker-compose.hub.yml"
)

for compose_file in "${COMPOSE_FILES[@]}"; do
    echo "Building and starting services from $compose_file"
    docker-compose -f "$COMPOSE_DIR/$compose_file" up --build -d
done

echo "All services have been built and started."