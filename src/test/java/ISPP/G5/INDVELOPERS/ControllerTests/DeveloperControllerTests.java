package ISPP.G5.INDVELOPERS.ControllerTests;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
class DeveloperControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private DeveloperService developerService;
	
	Developer developer1;
	Developer developer2;
	
	@BeforeEach
	void init() {
		developer1 = new Developer("developer1", "password", null, null, null, null, null, null, null);
		developer2 = new Developer("developer2", "password", null, null, null, null, null, null, null);
	}
	
	@Test
	@DisplayName("Show developer list")
	@WithMockUser(value="spring")
	void showStudentsListTest() {
		//arrange
		when(this.developerService.getAll()).thenReturn(Lists.list(developer1, developer2));
		try {
			//act
			mockMvc.perform(get("/developers"))
			//assert
			.andExpect(status().isOk())
//			.andExpect(view().name("developers/developerList")) Lo tienen que poner los de Front-End
			.andExpect(model().attribute("developer", hasItem(
                    allOf(
                            hasProperty("username", is(developer1.getUsername())),
                            hasProperty("email", is(developer1.getEmail()))
                    )
            )));
		} catch (Exception e) {
			System.err.println("Error testing controller: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
//	@Test
//	@DisplayName("Init creation form")
//	@WithMockUser(value="spring")
//	void initCreationFormTest() {
//		//arrange
//		try {
//			//act
//			mockMvc.perform(get("/students/new"))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("students/createOrUpdateStudentForm"))
//			.andExpect(model().attributeExists("student"));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("Process creation form")
//	@WithMockUser(value="spring")
//	void processCreationFormTest() {
//		//arrange
//		try {
//			//act
//			mockMvc.perform(post("/students/new", student)
//				.param("firstName", "Will")
//				.param("lastName", "Smith")
//				.param("name", "Will Smith")
//				.param("email", "will@smith.com")
//				.param("username", "willy")
//				.param("password", "smilly")
//				.with(csrf()))
//			//assert
//			.andExpect(status().is3xxRedirection())
//			.andExpect(view().name("redirect:/students/" + "null"));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("List my subjects")
//	@WithMockUser(value="spring")
//	void listMySubjectsTest() {
//		//arrange
//		student.setId(111);
//		when(this.studentService.findStudentByUsername(any())).thenReturn(student);
//		try {
//			//act
//			mockMvc.perform(get("/subjects/mySubjects/{studentId}", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("students/mySubjects"))
//			.andExpect(model().attribute("student", hasProperty("name", is(student.getName()))));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("Show my rated teachers")
//	@WithMockUser(value="spring")
//	void showMyRatedTeachersTest() {
//		//arrange
//		student.setId(111);
//		Teacher teacher = new Teacher("profesor", null, null, null, null, null);
//		when(studentService.findStudentById(student.getId())).thenReturn(student);
//		when(teacherService.findTeacherByStudentId(student.getId())).thenReturn(Lists.list(teacher));
//		when(this.studentService.findStudentByUsername(any())).thenReturn(student);
//		try {
//			//act
//			mockMvc.perform(get("/students/{studentId}/showRatedTeachers", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("teachers/myRatedTeachersList"))
//			.andExpect(model().attribute("teachers", hasItem(
//                    hasProperty("name", is(teacher.getName()))
//					)));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("Teacher to rate")
//	@WithMockUser(value="spring")
//	void teacherToRateTest() {
//		//arrange
//		student.setId(123);
//		Teacher teacher = new Teacher("profesor", null, null, null, null, null);
//		when(teacherService.teachersToRate(student.getId())).thenReturn(Lists.list(teacher));
//		when(this.studentService.findStudentByUsername(any())).thenReturn(student);
//		try {
//			//act
//			mockMvc.perform(get("/students/{studentId}/teacherToRate", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("teachers/teacherToRate"))
//			.andExpect(model().attribute("teachers", hasItem(
//                    hasProperty("name", is(teacher.getName()))
//					)));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	//negative
//	
//	@Test
//	@DisplayName("Process creation form empty name")
//	@WithMockUser(value="spring")
//	void processCreationFormNegativeTest() {
//		//arrange
//		Student student22 = new Student();
//		try {
//			//act
//			mockMvc.perform(post("/students/new", student22)
//				.param("firstName", "Will")
//				.param("lastName", "Smith")
//				.param("name", "")
//				.param("email", "will@smith.com")
//				.param("username", "willy")
//				.param("password", "smilly")
//				.with(csrf()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("students/createOrUpdateStudentForm"));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("List my subjects bad studentId")
//	@WithMockUser(value="spring")
//	void listMySubjectsNegativeTest() {
//		//arrange
//		student.setId(111);
//		when(this.studentService.findStudentByUsername(any())).thenReturn(null);
//		when(this.studentService.findStudentByUsername(any())).thenReturn(student);
//		try {
//			//act
//			mockMvc.perform(get("/subjects/mySubjects/{studentId}", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("students/mySubjects"))
//			.andExpect(model().attribute("subjects", is(new ArrayList<>())));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("Show my rated teachers bad id")
//	@WithMockUser(value="spring")
//	void showMyRatedTeachersNegativeTest() {
//		//arrange
//		student.setId(111);
//		Teacher teacher = new Teacher("profesor", null, null, null, null, null);
//		when(studentService.findStudentById(student.getId())).thenReturn(null);
//		when(teacherService.findTeacherByStudentId(student.getId())).thenReturn(Lists.list(teacher));
//		try {
//			//act
//			mockMvc.perform(get("/students/{studentId}/showRatedTeachers", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("exception"));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@DisplayName("Teacher to rate bad student id")
//	@WithMockUser(value="spring")
//	void teacherToRateNegativeTest() {
//		//arrange
//		student.setId(123);
//		when(teacherService.teachersToRate(student.getId())).thenReturn(Lists.list());
//		when(this.studentService.findStudentByUsername(any())).thenReturn(student);
//		try {
//			//act
//			mockMvc.perform(get("/students/{studentId}/teacherToRate", student.getId()))
//			//assert
//			.andExpect(status().isOk())
//			.andExpect(view().name("teachers/teacherToRate"))
//			.andExpect(model().attribute("teachers", is(new ArrayList<Teacher>())));
//		} catch (Exception e) {
//			System.err.println("Error testing controller: "+e.getMessage());
//			e.printStackTrace();
//		}
//	}

}
