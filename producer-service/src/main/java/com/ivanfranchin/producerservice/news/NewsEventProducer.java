package com.ivanfranchin.producerservice.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.ivanfranchin.producerservice.news.event.News;

import reactor.core.publisher.Mono;

@Component
public class NewsEventProducer {

  private static final Logger log = LoggerFactory.getLogger(NewsEventProducer.class);

  private final StreamBridge streamBridge;

  public NewsEventProducer(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public Mono<News> send(News news) {
    return Mono.just(news)
        .doOnNext(
            news1 -> {
              String topic = getTopic(news1);
              streamBridge.send(topic, news1);
              log.info("Sent '{}' to topic '{}'", news1, topic);
            });
  }

  private String getTopic(News news) {
    return String.format("ps/news/%s/%s/%s", news.type(), news.country(), news.city());
  }
}
