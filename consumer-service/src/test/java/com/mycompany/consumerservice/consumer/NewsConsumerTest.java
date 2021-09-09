package com.mycompany.consumerservice.consumer;

import com.mycompany.consumerservice.ConsumerServiceApplication;
import com.mycompany.consumerservice.event.News;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class NewsConsumerTest {

    @Test
    void testNewsSubscribingAllNewsFromBrazil(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false", "--spring.cloud.stream.bindings.news-in-0.destination=ps/news/*/BR>")) {

            News news = new News("id", "type", "country", "city", "title");
            Message<News> alertEventMessage = MessageBuilder.withPayload(news).build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, "ps/news/*/BR>");

            assertThat(output).contains("Received message!");
            assertThat(output).contains("PAYLOAD: News(id=id, type=type, country=country, city=city, title=title)");
        }
    }

    @Test
    void testNewsSubscribingAllEconomyNews(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false", "--spring.cloud.stream.bindings.news-in-0.destination=ps/news/ECONOMY/>")) {

            News news = new News("id", "type", "country", "city", "title");
            Message<News> alertEventMessage = MessageBuilder.withPayload(news).build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, "ps/news/ECONOMY/>");

            assertThat(output).contains("Received message!");
            assertThat(output).contains("PAYLOAD: News(id=id, type=type, country=country, city=city, title=title)");
        }
    }

    @Test
    void testNewsSubscribingAllNewsFromPorto(CapturedOutput output) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                TestChannelBinderConfiguration.getCompleteConfiguration(
                        ConsumerServiceApplication.class))
                .web(WebApplicationType.NONE)
                .run("--spring.jmx.enabled=false", "--spring.cloud.stream.bindings.news-in-0.destination=ps/news/*/*/Porto")) {

            News news = new News("id", "type", "country", "city", "title");
            Message<News> alertEventMessage = MessageBuilder.withPayload(news).build();

            InputDestination inputDestination = context.getBean(InputDestination.class);
            inputDestination.send(alertEventMessage, "ps/news/*/*/Porto");

            assertThat(output).contains("Received message!");
            assertThat(output).contains("PAYLOAD: News(id=id, type=type, country=country, city=city, title=title)");
        }
    }
}