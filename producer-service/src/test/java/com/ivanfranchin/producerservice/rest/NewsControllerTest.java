package com.ivanfranchin.producerservice.rest;

import com.ivanfranchin.producerservice.event.Country;
import com.ivanfranchin.producerservice.event.News;
import com.ivanfranchin.producerservice.rest.dto.CreateNewsRequest;
import com.ivanfranchin.producerservice.rest.dto.CreateRandomNewsRequest;
import com.ivanfranchin.producerservice.event.NewsType;
import com.ivanfranchin.producerservice.producer.NewsEventProducer;
import com.ivanfranchin.producerservice.service.RandomNews;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private NewsEventProducer newsEventProducer;

    @MockBean
    private RandomNews randomNews;

    @ParameterizedTest
    @MethodSource("provideTestPublishNews")
    void testPublishNews(CreateNewsRequest request) {
        webTestClient.post()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(News.class)
                .value(news -> {
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
                Arguments.of(new CreateNewsRequest(NewsType.HEALTH, Country.BR, "Sao Paulo", "title")),
                Arguments.of(new CreateNewsRequest(NewsType.ECONOMY, Country.PT, "Porto", "title"))
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestPublishRandomNews")
    void testPublishRandomNews(CreateRandomNewsRequest request, int times) {
        News news = new News("id", NewsType.SPORT, Country.DE, "city", "title");
        when(randomNews.generate(anyString())).thenReturn(news);

        webTestClient.post()
                .uri(BASE_URL + "/random")
                .accept(MediaType.APPLICATION_NDJSON)
                .body(Mono.just(request), CreateRandomNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(News.class);

        verify(randomNews, times(times)).generate(anyString());
    }

    private static Stream<Arguments> provideTestPublishRandomNews() {
        return Stream.of(
                Arguments.of(new CreateRandomNewsRequest(1, 1000), 1),
                Arguments.of(new CreateRandomNewsRequest(5, 100), 5),
                Arguments.of(new CreateRandomNewsRequest(10, 10), 10)
        );
    }

    private static final String BASE_URL = "/api/news";
}