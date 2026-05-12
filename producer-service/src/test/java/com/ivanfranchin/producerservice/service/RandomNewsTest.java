package com.ivanfranchin.producerservice.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.ivanfranchin.producerservice.news.RandomNews;
import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.News;
import com.ivanfranchin.producerservice.news.event.NewsType;

class RandomNewsTest {

  private final RandomNews randomNews = new RandomNews();

  @Test
  void testGenerate() {
    News news = randomNews.generate();

    assertThat(news.id()).isNotNull();
    assertThat(news.type()).isInstanceOf(NewsType.class);
    assertThat(news.country()).isInstanceOf(Country.class);
    assertThat(RandomNews.getCities().get(news.country())).contains(news.city());
    assertThat(news.title()).isEqualTo("...");
  }
}
