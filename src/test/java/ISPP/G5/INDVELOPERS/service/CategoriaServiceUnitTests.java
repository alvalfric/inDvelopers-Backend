
package ISPP.G5.INDVELOPERS.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.repositories.CategoriaRepository;
import ISPP.G5.INDVELOPERS.services.CategoriaService;

@SpringBootTest
public class CategoriaServiceUnitTests {

	@InjectMocks
	CategoriaService	service;

	@Mock
	CategoriaRepository	repository;


	@Test
	@DisplayName("Finding all")
	void testFindAll() {
		when(repository.findAll()).thenReturn(new ArrayList<Category>());

		assertTrue(service.findAll().isEmpty());
	}

	@Test
	@DisplayName("Finding by id")
	void testFindById() {
		when(repository.findById(any(String.class))).thenReturn(Optional.of(new Category()));

		assertNotNull(service.findById("prueba"));
	}

	@Test
	@DisplayName("Finding by title")
	void testFindByTitle() {
		List<Category> l = new ArrayList<Category>();
		Category c = new Category("Action");
		l.add(c);
		when(repository.findByTitle(any(String.class))).thenReturn(l);

		assertNotNull(service.findByTitle("Action"));
	}

	@Test
	@DisplayName("Adding Category")
	void testAddCategory() {
		when(repository.findAll()).thenReturn(new ArrayList<Category>());
		when(repository.save(any(Category.class))).thenReturn(new Category());

		assertNotNull(service.addCategory(new Category()));
	}

	@Test
	@DisplayName("Deleting Category")
	void testDeleteCategory() {
		List<Category> l = new ArrayList<Category>();
		Category c = new Category("Action");
		l.add(c);
		when(repository.findAll()).thenReturn(l);

		assertNotNull(service.deleteCategory(c));
	}
}
