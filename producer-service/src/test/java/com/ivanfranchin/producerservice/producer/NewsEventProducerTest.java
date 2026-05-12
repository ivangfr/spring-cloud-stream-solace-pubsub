package com.ivanfranchin.producerservice.producer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration.getCompleteConfiguration;

import java.util.stream.Stream;

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

import com.ivanfranchin.producerservice.ProducerServiceApplication;
import com.ivanfranchin.producerservice.news.NewsEventProducer;
import com.ivanfranchin.producerservice.news.event.Country;
import com.ivanfranchin.producerservice.news.event.News;
import com.ivanfranchin.producerservice.news.event.NewsType;

import tools.jackson.databind.ObjectMapper;

class NewsEventProducerTest {

  @ParameterizedTest
  @MethodSource("provideTestSendNews")
  void testSendNews(News news, String bindingName) {
    try (ConfigurableApplicationContext context =
        new SpringApplicationBuilder(getCompleteConfiguration(ProducerServiceApplication.class))
            .web(WebApplicationType.NONE)
            .run("--spring.jmx.enabled=false")) {

      NewsEventProducer newsEventProducer = context.getBean(NewsEventProducer.class);
      newsEventProducer.send(news).subscribe();

      ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
      OutputDestination outputDestination = context.getBean(OutputDestination.class);

      Message<byte[]> outputMessage = outputDestination.receive(0, bindingName);
      MessageHeaders headers = outputMessage.getHeaders();
      News payload = objectMapper.readValue(outputMessage.getPayload(), News.class);

      assertThat(headers.get(MessageHeaders.CONTENT_TYPE))
          .isEqualTo(MediaType.APPLICATION_JSON_VALUE);
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
            new News("id", NewsType.HEALTH, Country.BR, "SaoPaulo", "title"),
            "ps/news/HEALTH/BR/SaoPaulo"),
        Arguments.of(
            new News("id", NewsType.ECONOMY, Country.PT, "Porto", "title"),
            "ps/news/ECONOMY/PT/Porto"));
  }
}
