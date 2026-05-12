package com.ivanfranchin.producerservice.config;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.webflux.error.DefaultErrorAttributes;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;

@Configuration
public class ErrorAttributesConfig {

  @Bean
  ErrorAttributes errorAttributes() {
    return new DefaultErrorAttributes() {
      @Override
      public Map<String, Object> getErrorAttributes(
          ServerRequest request, ErrorAttributeOptions options) {
        return super.getErrorAttributes(
            request, options.including(Include.MESSAGE, Include.BINDING_ERRORS));
      }
    };
  }
}
