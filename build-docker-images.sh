#!/usr/bin/env bash

DOCKER_IMAGE_PREFIX="ivanfranchin"
APP_VERSION="1.0.0"

PRODUCER_SERVICE_APP_NAME="producer-service"
CONSUMER_SERVICE_APP_NAME="consumer-service"
PRODUCER_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${PRODUCER_SERVICE_APP_NAME}:${APP_VERSION}"
CONSUMER_SERVICE_DOCKER_IMAGE_NAME="${DOCKER_IMAGE_PREFIX}/${CONSUMER_SERVICE_APP_NAME}:${APP_VERSION}"

SKIP_TESTS="true"

./mvnw clean spring-boot:build-image \
  --projects "$PRODUCER_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dspring-boot.build-image.imageName="$PRODUCER_SERVICE_DOCKER_IMAGE_NAME"

./mvnw clean spring-boot:build-image \
  --projects "$CONSUMER_SERVICE_APP_NAME" \
  -DskipTests="$SKIP_TESTS" \
  -Dspring-boot.build-image.imageName="$CONSUMER_SERVICE_DOCKER_IMAGE_NAME"