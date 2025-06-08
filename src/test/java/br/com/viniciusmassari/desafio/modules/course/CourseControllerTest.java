package br.com.viniciusmassari.desafio.modules.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

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
                var res = response.getResponse().getContentAsString();
                var responseDTO = mapper.readValue(res, AuthInstructorResponseDTO.class);
                String token = responseDTO.token();

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
                                .instructorEntity(createdInstructor)
                                .build();

                this.courseRepository.saveAndFlush(courseEntity);

                var result = mvc.perform(MockMvcRequestBuilders.get("/courses/show/?page=0&perPage=10"))
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
                                .instructorEntity(createdInstructor)
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
                String token = responseDTO.token();

                UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder().category("NEW CATEGORY").name("NEW NAME")
                                .build();

                mvc.perform(
                                MockMvcRequestBuilders.put("/courses/" + courseEntity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(TestUtils.objectToJSON(
                                                                updateCourseDTO))
                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
        }

        @DisplayName("Should be able change course status")
        @Test
        public void should_change_course_status() throws Exception {
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

                String token = responseDTO.token();

                CourseEntity courseEntity = CourseEntity.builder().instructorEntity(instructorEntity)
                                .instructorEntity(instructorEntity).Active(CourseActive.ACTIVE).category("Category")
                                .description("description").name("name").build();

                courseRepository.saveAndFlush(courseEntity);

                mvc.perform(
                                MockMvcRequestBuilders.patch("/courses/" + courseEntity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)

                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());

                Optional<CourseEntity> courseWithNewStatus = courseRepository.findById(courseEntity.getId());

                assertEquals(courseWithNewStatus.get().getActive(), CourseActive.NOT);

        }

        @DisplayName("Should be able to delete course")
        @Test
        public void should_delete_course() throws Exception {
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

                String token = responseDTO.token();

                CourseEntity courseEntity = CourseEntity.builder().instructorEntity(instructorEntity)
                                .instructorEntity(instructorEntity).Active(CourseActive.ACTIVE).category("Category")
                                .description("description").name("name").build();

                courseRepository.saveAndFlush(courseEntity);

                mvc.perform(
                                MockMvcRequestBuilders.delete("/courses/" + courseEntity.getId())
                                                .contentType(MediaType.APPLICATION_JSON)

                                                .header("Authorization", "Bearer " + token))
                                .andExpect(MockMvcResultMatchers.status().isNoContent());

                Optional<CourseEntity> course = courseRepository.findById(courseEntity.getId());

                assertEquals(course.isPresent(), false);

        }

}
