package br.com.viniciusmassari.desafio.modules.instructor.controller;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
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
                mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();
        }

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @DisplayName("Should be able to create a instructor")
        @Test
        public void should_create_a_instructor() throws Exception {
                CreateInstructorDTO createInstructorDTO = CreateInstructorDTO.builder().name("TEST_NAME")
                                .email("email@test.com").password("123456").build();
                mvc.perform(
                                MockMvcRequestBuilders.post("/instructor/create")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(createInstructorDTO)))
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @DisplayName("Should delete an instructor account")
        @Test
        public void should_delete_an_instructor_account() throws Exception {
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
                AuthInstructorResponseDTO responseDTO = mapper.readValue(response.getResponse().getContentAsString(),
                                AuthInstructorResponseDTO.class);

                String token = responseDTO.token();

                mvc.perform(
                                MockMvcRequestBuilders.delete("/instructor/delete/" + instructorEntity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)

                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

}
