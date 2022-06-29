package com.mycompany.consumerservice.consumer;

import com.mycompany.consumerservice.event.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class NewsEventConsumer {

    @Bean
    public Consumer<Message<News>> news() {
        return message -> log.info(
                LOG_TEMPLATE, "Received message!", message.getHeaders(), message.getPayload());
    }

    private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";
}
