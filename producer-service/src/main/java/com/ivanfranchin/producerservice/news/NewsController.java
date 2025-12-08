package com.ivanfranchin.producerservice.news;

import com.ivanfranchin.producerservice.news.dto.CreateNewsRequest;
import com.ivanfranchin.producerservice.news.dto.CreateRandomNewsRequest;
import com.ivanfranchin.producerservice.news.event.News;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsEventProducer newsEventProducer;
    private final RandomNews randomNews;

    public NewsController(NewsEventProducer newsEventProducer, RandomNews randomNews) {
        this.newsEventProducer = newsEventProducer;
        this.randomNews = randomNews;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<News> publishNews(@Valid @RequestBody CreateNewsRequest request) {
        return newsEventProducer.send(News.fromCreateNewsRequest(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/random", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<News> publishRandomNews(@Valid @RequestBody CreateRandomNewsRequest request) {
        return Flux.range(0, request.number())
                .delayElements(Duration.ofMillis(request.delayInMillis()))
                .map(i -> randomNews.generate())
                .doOnNext(news -> newsEventProducer.send(news).subscribe());
    }
}
