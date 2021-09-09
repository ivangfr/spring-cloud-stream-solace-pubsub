package com.mycompany.producerservice.rest.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
public class CreateRandomNewsRequest {

    @NotNull
    @Positive
    Integer number;

    @NotNull
    @Positive
    Integer delayInMillis;
}
