package com.mycompany.producerservice.rest;

import com.mycompany.producerservice.event.News;
import com.mycompany.producerservice.producer.NewsEventProducer;
import com.mycompany.producerservice.rest.dto.CreateNewsRequest;
import com.mycompany.producerservice.rest.dto.CreateRandomNewsRequest;
import com.mycompany.producerservice.service.RandomNews;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;
    private final RandomNews randomNews;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<News> publishNews(@Valid @RequestBody CreateNewsRequest request) {
        News news = News.of(getId(), request.getType(), request.getCountry(), request.getCity(), request.getTitle());
        newsEventProducer.send(news);
        return Mono.just(news);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/random", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<News> publishRandomNews(@Valid @RequestBody CreateRandomNewsRequest request) {
        return Flux.range(0, request.getNumber())
                .delayElements(Duration.ofMillis(request.getDelayInMillis()))
                .map(i -> randomNews.generate(getId()))
                .doOnNext(newsEventProducer::send);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
