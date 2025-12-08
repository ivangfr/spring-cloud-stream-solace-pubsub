package com.ivanfranchin.producerservice.news.event;

import com.ivanfranchin.producerservice.news.dto.CreateNewsRequest;

import java.util.UUID;

public record News(String id, NewsType type, Country country, String city, String title) {

    public News(NewsType type, Country country, String city, String title) {
        this(randomUUID(), type, country, city, title);
    }

    public static News fromCreateNewsRequest(CreateNewsRequest request) {
        return new News(randomUUID(), request.type(), request.country(), request.city(), request.title());
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
