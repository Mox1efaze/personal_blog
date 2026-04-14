version: '3.8'

services:
  perblog-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: perblog-app
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SERVER_PORT=8080
    volumes:
      - perblog-data:/app/data
    networks:
      - perblog-network
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

  # 如果需要使用 MySQL，取消下面的注释
  # perblog-db:
  #   image: mysql:8.0
  #   container_name: perblog-db
  #   ports:
  #     - "3306:3306"
  #   environment:
  #     MYSQL_ROOT_PASSWORD: rootpassword
  #     MYSQL_DATABASE: perblog
  #     MYSQL_USER: perblog
  #     MYSQL_PASSWORD: perblog123
  #   volumes:
  #     - mysql-data:/var/lib/mysql
  #     - ./src/main/resources/schema_mysql.sql:/docker-entrypoint-initdb.d/schema.sql
  #   networks:
  #     - perblog-network
  #   restart: unless-stopped
  #   healthcheck:
  #     test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
  #     interval: 10s
  #     timeout: 5s
  #     retries: 5

volumes:
  perblog-data:
    driver: local
  # mysql-data:
  #   driver: local

networks:
  perblog-network:
    driver: bridge
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S perblog && adduser -S perblog -G perblog

COPY --from=build /app/target/*.jar app.jar

RUN chown -R perblog:perblog /app

USER perblog

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
