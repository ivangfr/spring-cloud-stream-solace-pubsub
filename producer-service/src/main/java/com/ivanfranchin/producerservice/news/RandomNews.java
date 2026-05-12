package com.ivanfranchin.producerservice.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.News;
import com.ivanfranchin.producerservice.news.event.NewsType;

@Component
public class RandomNews {

  private static final Map<Country, List<String>> cities = new HashMap<>();
  private static final Random random = new Random();

  public static Map<Country, List<String>> getCities() {
    return cities;
  }

  static {
    cities.put(Country.BR, List.of("SaoPaulo", "RioDeJaneiro", "Brasilia"));
    cities.put(Country.PT, List.of("Porto", "Lisboa", "Coimbra"));
    cities.put(Country.DE, List.of("Berlin", "München", "Nürnberg"));
  }

  public News generate() {
    NewsType[] newsTypes = NewsType.values();
    NewsType newsType = newsTypes[random.nextInt(newsTypes.length)];

    Country[] countries = Country.values();
    Country country = countries[random.nextInt(countries.length)];

    List<String> countryCities = cities.get(country);
    String city = cities.get(country).get(random.nextInt(countryCities.size()));

    String title = "...";

    return new News(newsType, country, city, title);
  }
}
