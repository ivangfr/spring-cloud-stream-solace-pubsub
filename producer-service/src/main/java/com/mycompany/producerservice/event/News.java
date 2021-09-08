package com.mycompany.producerservice.event;

import lombok.Value;

@Value(staticConstructor = "of")
public class News {
    String title;
}
