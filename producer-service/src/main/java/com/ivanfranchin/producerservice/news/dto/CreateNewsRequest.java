package com.ivanfranchin.producerservice.news.dto;

import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.NewsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNewsRequest(
        @NotNull NewsType type,
        @NotNull Country country,
        @NotBlank String city,
        @NotBlank String title) {
}
