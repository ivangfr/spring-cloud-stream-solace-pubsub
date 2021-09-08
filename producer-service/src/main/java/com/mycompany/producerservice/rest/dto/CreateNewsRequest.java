package com.mycompany.producerservice.rest.dto;

import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.NewsType;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class CreateNewsRequest {

    @NotNull
    NewsType type;

    @NotNull
    Country country;

    @NotBlank
    String city;

    @NotBlank
    String title;
}
