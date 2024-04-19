For this project to compile java 21 is needed.

In order to start the project execute: run
1. ./gradlew clean build
2. docker compose build
3. docker compose up joke-service


Service interaction curl:
curl --location --request GET 'http://localhost:8080/jokes?count=1'
