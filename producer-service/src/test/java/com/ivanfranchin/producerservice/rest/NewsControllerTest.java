package com.ivanfranchin.producerservice.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ivanfranchin.producerservice.news.NewsController;
import com.ivanfranchin.producerservice.news.NewsEventProducer;
import com.ivanfranchin.producerservice.news.RandomNews;
import com.ivanfranchin.producerservice.news.dto.CreateNewsRequest;
import com.ivanfranchin.producerservice.news.dto.CreateRandomNewsRequest;
import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.News;
import com.ivanfranchin.producerservice.news.event.NewsType;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockitoBean private NewsEventProducer newsEventProducer;

  @MockitoBean private RandomNews randomNews;

  @ParameterizedTest
  @MethodSource("provideTestPublishNews")
  void testPublishNews(CreateNewsRequest request) {
    Mono<News> newsMono =
        Mono.just(new News("id", request.type(), request.country(), request.city(), "title"));
    when(newsEventProducer.send(any(News.class))).thenReturn(newsMono);

    webTestClient
        .post()
        .uri(BASE_URL)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(request), CreateNewsRequest.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(News.class)
        .value(
            news -> {
              assertThat(news.id()).isNotNull();
              assertThat(news.type()).isEqualTo(request.type());
              assertThat(news.country()).isEqualTo(request.country());
              assertThat(news.city()).isEqualTo(request.city());
              assertThat(news.title()).isEqualTo(request.title());
            });
  }

  private static Stream<Arguments> provideTestPublishNews() {
    return Stream.of(
        Arguments.of(new CreateNewsRequest(NewsType.SPORT, Country.DE, "Berlin", "title")),
        Arguments.of(new CreateNewsRequest(NewsType.HEALTH, Country.BR, "SaoPaulo", "title")),
        Arguments.of(new CreateNewsRequest(NewsType.ECONOMY, Country.PT, "Porto", "title")));
  }

  @ParameterizedTest
  @MethodSource("provideTestPublishRandomNews")
  void testPublishRandomNews(CreateRandomNewsRequest request, int times) {
    News news = new News("id", NewsType.SPORT, Country.DE, "city", "title");

    when(randomNews.generate()).thenReturn(news);
    when(newsEventProducer.send(any(News.class))).thenReturn(Mono.just(news));

    webTestClient
        .post()
        .uri(BASE_URL + "/random")
        .accept(MediaType.APPLICATION_NDJSON)
        .body(Mono.just(request), CreateRandomNewsRequest.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBodyList(News.class);

    verify(randomNews, times(times)).generate();
  }

  private static Stream<Arguments> provideTestPublishRandomNews() {
    return Stream.of(
        Arguments.of(new CreateRandomNewsRequest(1, 1000), 1),
        Arguments.of(new CreateRandomNewsRequest(5, 100), 5),
        Arguments.of(new CreateRandomNewsRequest(10, 10), 10));
  }

  private static final String BASE_URL = "/api/news";
}
