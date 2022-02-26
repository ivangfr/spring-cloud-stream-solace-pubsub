# spring-cloud-stream-solace-pubsub

The goal of this project is to play with [`Solace PubSub+`](https://www.solace.dev/). For it, we will implement a producer and consumer of different types of `news` about many countries and cities.

## Applications

- ### producer-service

  [`Spring Boot`](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) application that exposes a REST API to submit `news` events. It published news to the following destination with format: `ps/news/{type}/{country}/{city}`

  Endpoints
  ```
  POST /api/news {"type": [SPORT|ECONOMY|HEALTH], "country": "...", "city": "...", "title": "..."}
  POST /api/news/random {"number": ..., "delay": ...}
  ```

- ### consumer-service

  `Spring Boot` application that consumes the `news` events published by `producer-service`.

## Prerequisites

- [`Java 11+`](https://www.oracle.com/java/technologies/downloads/#java11)
- [`Docker`](https://www.docker.com/)
- [`Docker-Compose`](https://docs.docker.com/compose/install/)

## Start Environment

- Open a terminal and inside `spring-cloud-stream-solace-pubsub` root folder run
  ```
  docker-compose up -d
  ```

- Wait for `solace` Docker container to be up and running. To check it, run
  ```
  docker-compose ps
  ```

## Running Applications with Maven

  - **producer-service**

    - In a terminal, make sure you are in `spring-cloud-stream-solace-pubsub` root folder
    - Run the commands below to start the application
      ```
      ./mvnw clean spring-boot:run --projects producer-service
      ```

  - **consumer-service-1**

    - It subscribes to all news from `Brazil`
    - Open a new terminal and navigate to `spring-cloud-stream-solace-pubsub` root folder
    - Run the commands below to start the application
      ```
      export NEWS_SUBSCRIPTION="ps/news/*/BR/>"
      ./mvnw clean spring-boot:run --projects consumer-service
      ```

  - **consumer-service-2** 

    - It subscribes to all news related to `HEALTH`
    - Open a new terminal and navigate to `spring-cloud-stream-solace-pubsub` root folder
    - Run the commands below to start the application
      ```
      export SERVER_PORT=9082
      export NEWS_SUBSCRIPTION="ps/news/HEALTH/>"
      ./mvnw spring-boot:run --projects consumer-service
      ```

## Running Applications as Docker containers

- ### Build Docker Images

  - In a terminal, make sure you are inside `spring-cloud-stream-solace-pubsub` root folder
  - Run the following script to build the Docker images
    - JVM
      ```
      ./docker-build.sh
      ```
    - Native (it's not implemented yet)
      ```
      ./docker-build.sh native
      ```

- ### Environment Variables

  - **producer-service**

    | Environment Variable | Description                                                                      |
    |----------------------|----------------------------------------------------------------------------------|
    | `SOLACE_HOST`        | Specify host of the `Solace PubSub+` message broker to use (default `localhost`) |
    | `SOLACE_PORT`        | Specify port of the `Solace PubSub+` message broker to use (default `55555`)     |

  - **consumer-service**

    | Environment Variable | Description                                                                      |
    |----------------------|----------------------------------------------------------------------------------|
    | `SOLACE_HOST`        | Specify host of the `Solace PubSub+` message broker to use (default `localhost`) |
    | `SOLACE_PORT`        | Specify port of the `Solace PubSub+` message broker to use (default `55555`)     |

- ### Run Docker Containers

  - **producer-service**

    Run the following command in a terminal
    ```
    docker run --rm --name producer-service \
      -p 9080:9080 \
      -e SOLACE_HOST=solace \
      --network=spring-cloud-stream-solace-pubsub_default \
      ivanfranchin/producer-service:1.0.0
    ```

  - **consumer-service-1**

    - It subscribes to all news from `Brazil`
    - Open a new terminal and run the following command
      ```
      docker run --rm --name consumer-service-1 \
        -p 9081:9081 \
        -e SOLACE_HOST=solace \
        -e NEWS_SUBSCRIPTION="ps/news/*/BR/>" \
        --network=spring-cloud-stream-solace-pubsub_default \
        ivanfranchin/consumer-service:1.0.0
      ```

  - **consumer-service-2**

    - It subscribes to all news related to `HEALTH`
    - Open a new terminal and run the following command
      ```
      docker run --rm --name consumer-service-2 \
        -p 9082:9081 \
        -e SOLACE_HOST=solace \
        -e NEWS_SUBSCRIPTION="ps/news/HEALTH/>" \
        --network=spring-cloud-stream-solace-pubsub_default \
        ivanfranchin/consumer-service:1.0.0
      ```

## Playing around

In a terminal, submit the following POST requests to `producer-service` and check its logs and `consumer-service` logs

> **Note:** [HTTPie](https://httpie.org/) is being used in the calls bellow

- Sending `news` one by one
  
  - Just `consumer-service-1` should consume
    ```
    http :9080/api/news type="SPORT" country="BR" city="Sao Paulo" title="..."
    ```

  - Just `consumer-service-2` should consume
    ```
    http :9080/api/news type="HEALTH" country="PT" city="Porto" title="..."
    ```

  - Both `consumer-service-1` or `consumer-service-2` should not consume
    ```
    http :9080/api/news type="ECONOMY" country="DE" city="Berlin" title="..."
    ```

  - Both `consumer-service-1` and `consumer-service-2` should consume
    ```
    http :9080/api/news type="HEALTH" country="BR" city="Berlin" title="..."
    ```

- Sending a number of `news` randomly with a specified delay in seconds
  ```
  http :9080/api/news/random number=10 delayInMillis=1000 --stream
  ```

## Useful Links

- **Solace**
  
  `Solace` can be accessed at http://localhost:8080 and enter `admin` to both username and password

## Shutdown

- To stop applications, go to the terminals where they are running and press `Ctrl+C`
- To stop and remove docker-compose containers, network and volumes, go to a terminal and, inside `spring-cloud-stream-solace-pubsub` root folder, run the following command
  ```
  docker-compose down -v
  ```

## Running Test Cases

In a terminal, make sure you are inside `spring-cloud-stream-solace-pubsub` root folder

- **producer-service**
  ```
  ./mvnw clean test --projects producer-service
  ```

- **consumer-service**
  ```
  ./mvnw clean test --projects consumer-service
  ```

## Cleanup

To remove the Docker images created by this project, go to a terminal and, inside `spring-cloud-stream-solace-pubsub` root folder, run the following script
```
./remove-docker-images.sh
```

## References

- https://tutorials.solace.dev/spring/spring-cloud-stream/
