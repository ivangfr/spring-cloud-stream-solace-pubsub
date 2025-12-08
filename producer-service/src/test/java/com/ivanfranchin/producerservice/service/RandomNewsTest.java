package com.ivanfranchin.producerservice.service;

import com.ivanfranchin.producerservice.news.RandomNews;
import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.News;
import com.ivanfranchin.producerservice.news.event.NewsType;
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
    void testGenerate() {
        News news = randomNews.generate();

        assertThat(news.id()).isNotNull();
        assertThat(news.type()).isInstanceOf(NewsType.class);
        assertThat(news.country()).isInstanceOf(Country.class);
        assertThat(RandomNews.cities.get(news.country())).contains(news.city());
        assertThat(news.title()).isEqualTo("...");
    }
}