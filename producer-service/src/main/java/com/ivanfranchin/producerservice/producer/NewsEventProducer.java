package com.ivanfranchin.producerservice.producer;

import com.ivanfranchin.producerservice.event.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsEventProducer {

    private final StreamBridge streamBridge;

    public Mono<News> send(News news) {
        return Mono.just(news)
                .doOnNext(news1 -> {
                    String topic = getTopic(news1);
                    streamBridge.send(topic, news1);
                    log.info("Sent '{}' to topic '{}'", news1, topic);
                });
    }

    private String getTopic(News news) {
        return String.format("ps/news/%s/%s/%s", news.type(), news.country(), news.city());
    }
}
