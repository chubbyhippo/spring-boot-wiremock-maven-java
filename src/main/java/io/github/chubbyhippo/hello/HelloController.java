package io.github.chubbyhippo.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
