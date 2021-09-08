package com.mycompany.producerservice.producer;

import com.mycompany.producerservice.event.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsEventProducer {

    private final StreamBridge streamBridge;

    public void send(News news) {
        String topic = getTopic(news);
        streamBridge.send(topic, news);
        log.info("Sent '{}' to topic '{}'", news, topic);
    }

    private String getTopic(News news) {
        return String.format("ps/news/%s/%s/%s", news.getType(), news.getCountry(), news.getCity());
    }
}
