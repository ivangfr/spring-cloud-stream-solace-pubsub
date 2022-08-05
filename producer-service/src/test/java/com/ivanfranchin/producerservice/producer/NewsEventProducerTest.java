package com.ivanfranchin.producerservice.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanfranchin.producerservice.ProducerServiceApplication;
import com.ivanfranchin.producerservice.event.Country;
import com.ivanfranchin.producerservice.event.News;
import com.ivanfranchin.producerservice.event.NewsType;
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
            assertThat(payload.id()).isEqualTo(news.id());
            assertThat(payload.type()).isEqualTo(news.type());
            assertThat(payload.country()).isEqualTo(news.country());
            assertThat(payload.city()).isEqualTo(news.city());
            assertThat(payload.title()).isEqualTo(news.title());
        }
    }

    private static Stream<Arguments> provideTestSendNews() {
        return Stream.of(
                Arguments.of(
                        new News("id", NewsType.SPORT, Country.DE, "Berlin", "title"),
                        "ps/news/SPORT/DE/Berlin"),
                Arguments.of(
                        new News("id", NewsType.HEALTH, Country.BR, "Sao Paulo", "title"),
                        "ps/news/HEALTH/BR/Sao Paulo"),
                Arguments.of(
                        new News("id", NewsType.ECONOMY, Country.PT, "Porto", "title"),
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