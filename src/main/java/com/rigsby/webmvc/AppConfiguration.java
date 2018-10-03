package com.rigsby.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/** The application is simple, it only uses Web MVC and a {@linkplain RestTemplate}. */
@EnableWebMvc
public class AppConfiguration {
  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
