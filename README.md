## Task:

# To implement API service:
1. Find out the current rate of bitcoin (BTC) in UAH.
2. Sign an email to receive information on changing the course.
3. A request that will be sent to all subscribed users current course


# Steps for applications running
1. Install Docker.
2. Perform following commands in terminal:
docker network create btc-service-network
docker run -d --name mail --network btc-service-network -p 8025:8025 -p 1025:1025 mailhog/mailhog:latest
docker run -d --name app --network btc-service-network -p 8080:8080 vitkolesnyk/btc-service:latest


***API documentation is available after app running via link:
http://localhost:8080/swagger-ui/index.html#/***

# Tests coverage:
Class - 93&
Method - 83%
Line - 80%

## Overview:
Tech stack: Java 17, Spring Boot 3.1.0

Mail server is mocked by Mailhog

Received emails can be checked via link: http://localhost:8025/

External API for BTC-UAH rate receiving - https://rest.coinapi.io

BTC-UAH rate is checking every 10 minutes, in case of rate change all subscribers receive email notification


