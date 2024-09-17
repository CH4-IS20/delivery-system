#!/bin/bash

# 빌드된 스프링부트 기반으로 Docker 컨테이너 일괄 실행
# 네트워크 생성
echo "Creating networks..."
docker network create external-network
docker network create internal-network --internal
docker network create monitoring-network
COMPOSE_DIR="./docker"

COMPOSE_FILES=(
    "docker-compose.eureka.yml"
    "docker-compose.gateway.yml"
    "docker-compose.cache.yml"
    "docker-compose.company.yml"
    "docker-compose.order.yml"
    "docker-compose.user.yml"
    "docker-compose.hub.yml"
)

# Docker Compose 파일을 순차적으로 빌드하고 실행
for compose_file in "${COMPOSE_FILES[@]}"; do
    echo "Building and starting services from $compose_file"
    if ! docker-compose -f "$COMPOSE_DIR/$compose_file" up --build -d; then
        echo "Failed to build or start services from $compose_file. Exiting..."
        exit 1
    fi
done

echo "All services have been built and started."