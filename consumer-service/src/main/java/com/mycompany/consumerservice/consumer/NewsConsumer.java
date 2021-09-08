package com.mycompany.consumerservice.consumer;

import com.mycompany.consumerservice.event.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class NewsConsumer {

    @Bean
    public Consumer<News> news() {
        return news -> log.info("Received {}", news);
    }
}
