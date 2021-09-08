package com.mycompany.producerservice.producer;

import com.mycompany.producerservice.event.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class NewsProducer {

    @Bean
    public Supplier<News> news() {
        return () -> {
            News news = News.of("News about Berlin ...");
            log.info("Emitting " + news);
            return news;
        };
    }
}
