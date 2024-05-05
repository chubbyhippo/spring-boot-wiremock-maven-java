package io.github.chubbyhippo.hello;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class HelloApplicationTests {

    @RegisterExtension
    private final static WireMockExtension wm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort()
                    .dynamicHttpsPort())
            .build();

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("should get hello using setter injection")
    void shouldGetHelloUsingSetterInjection(@Autowired HelloController controller) {
        wm.stubFor(get("/hello").willReturn(aResponse().withBody("hello")));
        controller.setUrl(wm.getRuntimeInfo().getHttpBaseUrl() + "/hello");

        webTestClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("hello");
    }

    @Test
    @DisplayName("should get hello using reflection")
    void shouldGetHelloUsingReflection(@Autowired HelloController controller) throws NoSuchFieldException, IllegalAccessException {
        wm.stubFor(get("/hello").willReturn(aResponse().withBody("hello")));
        controller.setUrl(wm.getRuntimeInfo().getHttpBaseUrl() + "/hello");

        var url = controller.getClass().getDeclaredField("url");
        ReflectionUtils.makeAccessible(url);
        url.set(controller, wm.getRuntimeInfo().getHttpBaseUrl() + "/hello");

        webTestClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("hello");
    }
}
