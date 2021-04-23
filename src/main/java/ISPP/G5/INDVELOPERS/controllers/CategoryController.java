package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.services.CategoriaService;

@RestController
@CrossOrigin("*")
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoriaService service;

	
	@GetMapping("/findAll")
	public ResponseEntity<List<Category>> getCategories() {
		try {
			return ResponseEntity.ok(service.findAll());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<Category> getCategoryById(@PathVariable final String id) {
		try {
			return ResponseEntity.ok(service.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	
	@PostMapping("/add")
	public ResponseEntity<String> addCategory(@RequestBody final Category categoria) {
		try {
			return new ResponseEntity<>(service.addCategory(categoria), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteCategory(@PathVariable final String id) {
		try {
			Category categoria = service.findById(id);
			return new ResponseEntity<>(service.deleteCategory(categoria), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
}
