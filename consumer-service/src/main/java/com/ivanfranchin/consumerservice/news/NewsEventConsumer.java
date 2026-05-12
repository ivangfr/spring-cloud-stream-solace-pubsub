package com.ivanfranchin.consumerservice.news;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class NewsEventConsumer {

  private static final Logger log = LoggerFactory.getLogger(NewsEventConsumer.class);
  private static final String LOG_TEMPLATE = "{}\n---\nHEADERS: {}\n...\nPAYLOAD: {}\n---";

  @Bean
  Consumer<Message<News>> news() {
    return message ->
        log.info(LOG_TEMPLATE, "Received message!", message.getHeaders(), message.getPayload());
  }
}
