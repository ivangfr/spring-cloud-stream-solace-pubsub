package com.mycompany.producerservice.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class News {

    String id;
    NewsType type;
    Country country;
    String city;
    String title;
}
