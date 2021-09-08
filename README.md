# spring-cloud-stream-solace-pubsub

The goal of this project is to play with [`Solace PubSub+`](https://www.solace.dev/). For it, we will implement a producer and consumer of `news` & `alert` events.

## Applications

- ### producer-service

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that exposes a REST API to submit `news` & `alert` events.

  Endpoints
  ```
  POST /api/news/cnn {"title": "..."}
  POST /api/news/dw {"titel": "..."}
  POST /api/news/rai {"titolo": "..."}
  POST /api/alert/earthquake {"richterScale": "...", "epicenterLat": "...", "epicenterLon": "..."}
  POST /api/alert/weather {"message": "..."}
  ```

- ### consumer-service

  `Spring Boot` application that consumes the `news` & `alert` events published by `producer-service`.

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start environment

- Open a terminal and inside `spring-cloud-stream-solace-pubsub` root folder run
  ```
  docker-compose up -d
  ```

- Wait until all containers are Up (healthy). You can check their status by running
  ```
  docker-compose ps

## Running Applications with Maven

- **producer-service**

    - In a terminal, make sure you are in `spring-cloud-stream-solace-pubsub` root folder
    - Run the command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects producer-service
      ```

- **consumer-service**

    - Open a new terminal and navigate to `spring-cloud-stream-solace-pubsub` root folder
    - Run the command below to start the application
      ```
      ./mvnw clean spring-boot:run --projects consumer-service
      ```

## Useful Links

- **Solace**
  
  `Solace` can be accessed at http://localhost:8080

## Shutdown

- To stop applications, go to the terminals where they are running and press `Ctrl+C`
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-solace-pubsub` root folder, run the following command
  ```
  docker-compose down -v
  ```

## References

- https://tutorials.solace.dev/spring/spring-cloud-stream/