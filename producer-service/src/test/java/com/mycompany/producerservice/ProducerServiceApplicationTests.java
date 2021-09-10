package com.mycompany.producerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.event.NewsType;
import com.mycompany.producerservice.rest.dto.CreateNewsRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
class ProducerServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideTestPublishNews")
    void testPublishNews(CreateNewsRequest request, String bindingName) throws IOException {
        webTestClient.post()
                .uri(API_NEWS_URL)
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

        Message<byte[]> outputMessage = outputDestination.receive(0, bindingName);
        assertThat(outputMessage).isNotNull();
        assertThat(outputMessage.getHeaders().get(MessageHeaders.CONTENT_TYPE))
                .isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        News news = objectMapper.readValue(outputMessage.getPayload(), News.class);
        assertThat(news).isNotNull();
        assertThat(news.getId()).isNotNull();
        assertThat(news.getType()).isEqualTo(request.getType());
        assertThat(news.getCountry()).isEqualTo(request.getCountry());
        assertThat(news.getCity()).isEqualTo(request.getCity());
        assertThat(news.getTitle()).isEqualTo(request.getTitle());
    }

    private static Stream<Arguments> provideTestPublishNews() {
        return Stream.of(
                Arguments.of(
                        new CreateNewsRequest(NewsType.SPORT, Country.DE, "Berlin", "title"),
                        "ps/news/SPORT/DE/Berlin"),
                Arguments.of(
                        new CreateNewsRequest(NewsType.HEALTH, Country.BR, "Sao Paulo", "title"),
                        "ps/news/HEALTH/BR/Sao Paulo"),
                Arguments.of(
                        new CreateNewsRequest(NewsType.ECONOMY, Country.PT, "Porto", "title"),
                        "ps/news/ECONOMY/PT/Porto")
        );
    }

    private static final String API_NEWS_URL = "/api/news";
}