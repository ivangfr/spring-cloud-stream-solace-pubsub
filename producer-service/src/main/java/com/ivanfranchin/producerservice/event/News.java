package com.ivanfranchin.producerservice.event;

public record News(String id, NewsType type, Country country, String city, String title) {
}
