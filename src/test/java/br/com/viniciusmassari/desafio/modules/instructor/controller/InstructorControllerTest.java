package br.com.viniciusmassari.desafio.modules.instructor.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class InstructorControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @DisplayName("Should be able to create a instructor")
    @Test
    public void should_create_a_instructor() throws Exception {
        CreateInstructorDTO createInstructorDTO = CreateInstructorDTO.builder().name("TEST_NAME")
                .email("email@test.com").password("123456").build();
        mvc.perform(
                MockMvcRequestBuilders.post("/instructor/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.objectToJSON(createInstructorDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

}
