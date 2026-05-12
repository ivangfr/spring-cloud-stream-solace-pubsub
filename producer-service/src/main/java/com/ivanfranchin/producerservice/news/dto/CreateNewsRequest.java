package com.ivanfranchin.producerservice.news.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.NewsType;

public record CreateNewsRequest(
    @NotNull NewsType type,
    @NotNull Country country,
    @NotBlank String city,
    @NotBlank String title) {}
