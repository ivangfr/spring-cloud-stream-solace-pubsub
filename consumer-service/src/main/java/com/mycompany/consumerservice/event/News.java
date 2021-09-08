package com.mycompany.consumerservice.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class News {
    private String id;
    private String type;
    private String country;
    private String city;
    private String title;
}
