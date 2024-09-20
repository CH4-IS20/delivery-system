#!/bin/bash

# 프로젝트 디렉터리 배열
PROJECTS=( "company" "eureka" "gateway" "hub" "order" "user" "policy")

# 각 프로젝트 디렉터리로 이동하여 gradlew clean build -x test 실행
for project in "${PROJECTS[@]}"; do
    echo "Building $project project..."
    # 프로젝트 디렉터리로 이동
    cd $project

    # gradlew clean build -x test 실행
    if ./gradlew clean build -x test; then
        echo "$project build succeeded."
    else
        echo "$project build failed. Exiting..."
        exit 1
    fi

    # 다시 루트 디렉터리로 이동
    cd ..
done

echo "All projects have been built successfully."
