package com.mycompany.producerservice.service;

import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.event.NewsType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class RandomNews {

    public static final Map<Country, List<String>> cities = new HashMap<>();
    private static final Random random = new Random();

    static {
        cities.put(Country.BR, List.of("Sao Paulo", "Rio de Janeiro", "Brasilia"));
        cities.put(Country.PT, List.of("Porto", "Lisboa", "Coimbra"));
        cities.put(Country.DE, List.of("Berlin", "München", "Nürnberg"));
    }

    public News generate(String id) {
        NewsType[] newsTypes = NewsType.values();
        NewsType newsType = newsTypes[random.nextInt(newsTypes.length)];

        Country[] countries = Country.values();
        Country country = countries[random.nextInt(countries.length)];

        List<String> countryCities = cities.get(country);
        String city = cities.get(country).get(random.nextInt(countryCities.size()));

        String title = "...";

        return News.of(id, newsType, country, city, title);
    }
}
