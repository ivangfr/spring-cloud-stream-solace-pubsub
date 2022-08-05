package com.ivanfranchin.producerservice.service;

import com.ivanfranchin.producerservice.event.Country;
import com.ivanfranchin.producerservice.event.News;
import com.ivanfranchin.producerservice.event.NewsType;
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

        assertThat(news.id()).isEqualTo("id");
        assertThat(news.type()).isInstanceOf(NewsType.class);
        assertThat(news.country()).isInstanceOf(Country.class);
        assertThat(RandomNews.cities.get(news.country())).contains(news.city());
        assertThat(news.title()).isEqualTo("...");
    }
}