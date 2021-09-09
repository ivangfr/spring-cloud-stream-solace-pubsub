package com.mycompany.producerservice.rest;

import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.event.NewsType;
import com.mycompany.producerservice.producer.NewsEventProducer;
import com.mycompany.producerservice.rest.dto.CreateNewsRequest;
import com.mycompany.producerservice.rest.dto.CreateRandomNewsRequest;
import com.mycompany.producerservice.service.RandomNews;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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

    @Test
    void testPublishNews() {
        CreateNewsRequest request = new CreateNewsRequest(NewsType.SPORT, Country.BR, "city", "title");

        webTestClient.post()
                .uri(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CreateNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(News.class)
                .value(news -> {
                    assertThat(news.getId()).isNotNull();
                    assertThat(news.getType()).isEqualTo(request.getType());
                    assertThat(news.getCountry()).isEqualTo(request.getCountry());
                    assertThat(news.getCity()).isEqualTo(request.getCity());
                    assertThat(news.getTitle()).isEqualTo(request.getTitle());
                });
    }

    @Test
    void testPublishRandomNews() {
        News news = News.of("id", NewsType.SPORT, Country.DE, "city", "title");
        when(randomNews.generate(anyString())).thenReturn(news);

        CreateRandomNewsRequest request = new CreateRandomNewsRequest(5, 100);

        webTestClient.post()
                .uri(BASE_URL+ "/random")
                .accept(MediaType.APPLICATION_NDJSON)
                .body(Mono.just(request), CreateRandomNewsRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBodyList(News.class);

        verify(randomNews, times(5)).generate(anyString());
    }

    private static final String BASE_URL = "/api/news";
}