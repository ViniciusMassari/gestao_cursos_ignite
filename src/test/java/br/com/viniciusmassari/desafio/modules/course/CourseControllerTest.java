package br.com.viniciusmassari.desafio.modules.course;

import static org.junit.Assert.assertTrue;

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

import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.UpdateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.utils.TestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CourseControllerTest {
        private MockMvc mvc;

        @Autowired
        private WebApplicationContext context;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private CourseRepository courseRepository;

        @Before
        public void setup() {
                mvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                                .build();
        }

        @DisplayName("Should be able to create a new course")
        @Test
        public void should_create_a_course() throws Exception {
                String email = "test@example.com";
                String password = "123456";
                String encodedPassword = this.passwordEncoder.encode(password);
                InstructorEntity instructorEntity = InstructorEntity.builder().name("NAME_TEST").email(email)
                                .password(encodedPassword).build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(email).password(password)
                                .build();

                this.instructorRepository.saveAndFlush(instructorEntity);

                // auth instructor
                var response = mvc.perform(
                                MockMvcRequestBuilders.post("/instructor/auth/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                authInstructorDTO)))
                                .andReturn();
                ObjectMapper mapper = new ObjectMapper();
                var responseDTO = mapper.readValue(response.getResponse().getContentAsString(),
                                AuthInstructorResponseDTO.class);
                String token = responseDTO.getToken();

                CreateCourseDTO createCourseDTO = CreateCourseDTO.builder().category("CATEGORY")
                                .description("DESCRIPTION")
                                .name("NAME").build();

                mvc.perform(
                                MockMvcRequestBuilders.post("/courses/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                createCourseDTO))
                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isCreated());
        }

        @DisplayName("Should return a list with one course")
        @Test
        public void should_return_an_list_with_one_course() throws Exception {

                InstructorEntity instructorEntity = InstructorEntity.builder().name("NAME")
                                .email("instructor@email.com").password("12345678").build();
                var createdInstructor = instructorRepository.saveAndFlush(instructorEntity);
                CourseEntity courseEntity = CourseEntity.builder().name("COURSE").description("COURSE DESCRIPTION TEST")
                                .category("Category").Active(CourseActive.ACTIVE)
                                .instructorId(createdInstructor.getId()).instructorEntity(createdInstructor)
                                .build();

                this.courseRepository.saveAndFlush(courseEntity);

                var result = mvc.perform(MockMvcRequestBuilders.get("/courses/show/"))
                                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

                assertTrue(!result.getResponse().getContentAsString().isEmpty());

        }

        @DisplayName("Should update a course")
        @Test
        public void should_update_a_course() throws Exception {
                String email = "test@example.com";
                String password = "123456";
                String encodedPassword = this.passwordEncoder.encode(password);
                InstructorEntity instructorEntity = InstructorEntity.builder().name("NAME_TEST").email(email)
                                .password(encodedPassword).build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(email).password(password)
                                .build();

                var createdInstructor = this.instructorRepository.saveAndFlush(instructorEntity);

                CourseEntity courseEntity = CourseEntity.builder().name("COURSE").description("COURSE DESCRIPTION TEST")
                                .category("Category").Active(CourseActive.ACTIVE)
                                .instructorId(createdInstructor.getId()).instructorEntity(createdInstructor)
                                .build();

                this.courseRepository.saveAndFlush(courseEntity);

                // auth instructor
                var response = mvc.perform(
                                MockMvcRequestBuilders.post("/instructor/auth/")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                authInstructorDTO)))
                                .andReturn();
                ObjectMapper mapper = new ObjectMapper();
                var responseDTO = mapper.readValue(response.getResponse().getContentAsString(),
                                AuthInstructorResponseDTO.class);
                String token = responseDTO.getToken();

                UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder().category("NEW CATEGORY").name("NEW NAME")
                                .build();

                mvc.perform(
                                MockMvcRequestBuilders.put("/courses/" + courseEntity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                updateCourseDTO))
                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isOk());
        }

}
