package com.ivanfranchin.producerservice.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateRandomNewsRequest(@NotNull @Positive Integer number, @NotNull @Positive Integer delayInMillis) {
}
