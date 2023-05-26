package com.ivanfranchin.producerservice.rest.dto;

import com.ivanfranchin.producerservice.event.Country;
import com.ivanfranchin.producerservice.event.NewsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNewsRequest(@NotNull NewsType type, @NotNull Country country, @NotBlank String city,
                                @NotBlank String title) {
}
