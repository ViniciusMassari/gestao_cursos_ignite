package br.com.viniciusmassari.desafio.modules.instructor.controller;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8083)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthRateLimitTest {

        @Test
        public void should_return_too_many_requests_status() {
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email("fernando@email.com")
                                .password("123456").build();
                String fakeJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTY5MDAwMDAwMCwiZXhwIjoxNjkwMDAzNjAwfQ.wF0z8U_9k2_oXQ0Q5bZ2fFgJ5mdV3SLcFTKXoXYc1Ck";
                stubFor(post(urlEqualTo("/instructor/auth"))
                                .withHeader("Content-Type", equalTo("application/json"))
                                .withRequestBody(equalToJson(
                                                "{\"email\":\"fernando@email.com\",\"password\":\"123456\"}"))
                                .inScenario("RateLimitTest") // Define um cenário para o rate-limit
                                .whenScenarioStateIs("Started") // Começa no estado inicial
                                .willReturn(aResponse()
                                                .withBody(fakeJwt)
                                                .withStatus(201))
                                .willSetStateTo("LimitReached"));

                stubFor(post(urlEqualTo("/instructor/auth"))
                                .inScenario("RateLimitTest")
                                .whenScenarioStateIs("LimitReached")
                                .willReturn(aResponse()
                                                .withStatus(429)
                                                .withBody("Too Many Requests")));

                RestTemplate template = new RestTemplate();
                // 🔹 Enviando 5 requisições válidas
                for (int i = 0; i < 6; i++) {

                        try {
                                template.postForObject("http://localhost:8083/instructor/auth", authInstructorDTO,
                                                String.class);

                        } catch (HttpClientErrorException e) {
                                if (i == 6) {

                                        assertEquals(429, e.getStatusCode());

                                }
                        }

                }

        }
}
