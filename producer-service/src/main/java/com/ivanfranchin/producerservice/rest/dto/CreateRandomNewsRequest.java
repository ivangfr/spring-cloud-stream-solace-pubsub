package com.ivanfranchin.producerservice.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record CreateRandomNewsRequest(@NotNull @Positive Integer number, @NotNull @Positive Integer delayInMillis) {
}
