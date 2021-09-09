package com.mycompany.producerservice.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.producerservice.ProducerServiceApplication;
import com.mycompany.producerservice.event.Country;
import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.event.NewsType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration.getCompleteConfiguration;

class NewsEventProducerTest {

    @ParameterizedTest
    @MethodSource("provideTestSendNews")
    void testSendNews(News news, String bindingName) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                getCompleteConfiguration(
                        ProducerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false")) {

            NewsEventProducer newsEventProducer = context.getBean(NewsEventProducer.class);
            newsEventProducer.send(news);

            ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
            OutputDestination outputDestination = context.getBean(OutputDestination.class);

            Message<byte[]> outputMessage = outputDestination.receive(0, bindingName);
            MessageHeaders headers = outputMessage.getHeaders();
            News payload = deserialize(objectMapper, outputMessage.getPayload(), News.class);

            assertThat(headers.get(MessageHeaders.CONTENT_TYPE)).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            assertThat(payload).isNotNull();
            assertThat(payload.getId()).isEqualTo(news.getId());
            assertThat(payload.getType()).isEqualTo(news.getType());
            assertThat(payload.getCountry()).isEqualTo(news.getCountry());
            assertThat(payload.getCity()).isEqualTo(news.getCity());
            assertThat(payload.getTitle()).isEqualTo(news.getTitle());
        }
    }

    private static Stream<Arguments> provideTestSendNews() {
        return Stream.of(
                Arguments.of(
                        News.of("id", NewsType.SPORT, Country.DE, "Berlin", "title"),
                        "ps/news/SPORT/DE/Berlin"),
                Arguments.of(
                        News.of("id", NewsType.HEALTH, Country.BR, "Sao Paulo", "title"),
                        "ps/news/HEALTH/BR/Sao Paulo"),
                Arguments.of(
                        News.of("id", NewsType.ECONOMY, Country.PT, "Porto", "title"),
                        "ps/news/ECONOMY/PT/Porto")
        );
    }

    private <T> T deserialize(ObjectMapper objectMapper, byte[] bytes, Class<T> clazz) {
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            return null;
        }
    }
}