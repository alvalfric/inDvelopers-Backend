
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.services.CategoriaService;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerUnitTests {

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private CategoriaService	service;

	Category					category	= new Category("action");


	@Test
	@DisplayName("Find All Categories")
	@WithMockUser(value = "spring")
	void findAllTest() throws Exception {
		when(service.findAll()).thenReturn(new ArrayList<Category>());
		mockMvc.perform(get("/categories/findAll")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Find Category by Id")
	@WithMockUser(value = "spring")
	void findByIdTest() throws Exception {
		when(service.findById(any(String.class))).thenReturn(category);
		mockMvc.perform(get("/categories/findById/id")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Add Category")
	@WithMockUser(value = "spring")
	void addCategoryTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(category);

		when(service.addCategory(any(Category.class))).thenReturn("OK");
		mockMvc.perform(post("/categories/add").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Delete Category")
	@WithMockUser(value = "spring")
	void deleteCategoryTest() throws Exception {
		when(service.findById(any(String.class))).thenReturn(category);
		when(service.deleteCategory(any(Category.class))).thenReturn("OK");
		mockMvc.perform(delete("/categories/delete/id")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Bad Requests")
	@WithMockUser(value = "spring")
	void badRequestTest() throws Exception {
		when(service.findById(any(String.class))).thenThrow(IllegalArgumentException.class);
		when(service.addCategory(any(Category.class))).thenThrow(IllegalArgumentException.class);
		when(service.findAll()).thenThrow(IllegalArgumentException.class);

		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(category);

		mockMvc.perform(get("/categories/findAll")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/categories/findById/id")).andExpect(status().isBadRequest());
		mockMvc.perform(delete("/categories/delete/id")).andExpect(status().isBadRequest());
		mockMvc.perform(post("/categories/add").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());

	}

}
