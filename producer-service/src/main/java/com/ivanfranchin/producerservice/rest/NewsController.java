package com.ivanfranchin.producerservice.rest;

import com.ivanfranchin.producerservice.event.News;
import com.ivanfranchin.producerservice.rest.dto.CreateNewsRequest;
import com.ivanfranchin.producerservice.rest.dto.CreateRandomNewsRequest;
import com.ivanfranchin.producerservice.producer.NewsEventProducer;
import com.ivanfranchin.producerservice.service.RandomNews;
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
        News news = new News(getId(), request.type(), request.country(), request.city(), request.title());
        newsEventProducer.send(news);
        return Mono.just(news);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/random", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<News> publishRandomNews(@Valid @RequestBody CreateRandomNewsRequest request) {
        return Flux.range(0, request.number())
                .delayElements(Duration.ofMillis(request.delayInMillis()))
                .map(i -> randomNews.generate(getId()))
                .doOnNext(newsEventProducer::send);
    }

    private String getId() {
        return UUID.randomUUID().toString();
    }
}
