package com.mycompany.producerservice.service;

import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.event.NewsType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(RandomNews.class)
class RandomNewsTest {

    @Autowired
    private RandomNews randomNews;

    @Test
    void name() {
        News news = randomNews.generate("id");

        assertThat(news.getId()).isEqualTo("id");
        assertThat(news.getType()).isInstanceOf(NewsType.class);
        assertThat(news.getCountry()).isInstanceOf(Country.class);
        assertThat(RandomNews.cities.get(news.getCountry())).contains(news.getCity());
        assertThat(news.getTitle()).isEqualTo("...");
    }
}