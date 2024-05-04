package io.github.chubbyhippo.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}

@RestController
class HelloController {
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @Value("${service.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    private final RestTemplate restTemplate;

    HelloController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello")
    String getHello() {
        log.info("Service URL: {}", url);
        return restTemplate.getForObject(url, String.class);
    }
}
