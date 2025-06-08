package br.com.viniciusmassari.desafio.modules.instructor.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.utils.TestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthInstructorControllerTest {

        private MockMvc mvc;

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private InstructorRepository instructorRepository;

        @Before
        public void setup() {
                mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();
        }

        @DisplayName("Should be able to auth an instructor")
        @Test
        public void should_auth_an_instructor() throws Exception {
                String email = "test@example.com";
                String password = "123456";
                String encodedPassword = this.passwordEncoder.encode(password);
                InstructorEntity instructorEntity = InstructorEntity.builder().name("NAME_TEST").email(email)
                                .password(encodedPassword).build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(email).password(password)
                                .build();

                this.instructorRepository.saveAndFlush(instructorEntity);

                var response = mvc.perform(
                                MockMvcRequestBuilders.post("/instructor/auth/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                authInstructorDTO)))
                                .andReturn();
                ObjectMapper mapper = new ObjectMapper();
                var res = response.getResponse().getContentAsString();
                var responseDTO = mapper.readValue(res,
                                AuthInstructorResponseDTO.class);

                assertInstanceOf(AuthInstructorResponseDTO.class, responseDTO);
        }

}
