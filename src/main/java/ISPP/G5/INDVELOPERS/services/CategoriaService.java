package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public List<Category> findAll(){
		return repository.findAll();
	}
	
	public Category findById(String id) {
		Optional<Category> categoria = repository.findById(id);
		if (categoria.get() != null) {
			return categoria.get();
		} else {
			throw new IllegalArgumentException("This category doesn't exist.");
		}
	}
	
	public Category findByTitle(String title) {
		Category categoria = null;
		List<Category> categorias = repository.findAll();
		for(Category c: categorias) {
			if(c.getTitle().contentEquals(title)) {
				categoria = c;
			}
		}
		return categoria;
	}
	
	public String addCategory(Category c) {
		List<Category> categorias = repository.findAll();
//		if(!categorias.contains(c)) {
//			repository.save(c);
//			return "Successfully added with id: " + c.getId();
//		}else {
//			throw new IllegalArgumentException("This category already exists.");
//		}
		repository.save(c);
		return "Successfully added with id: " + c.getId();
	}
	
	public String deleteCategory(Category c) {
		List<Category> categorias = repository.findAll();
		if(categorias.contains(c)) {
			repository.delete(c);
			return "Successfully deleted with id: " + c.getId();
		}else {
			throw new IllegalArgumentException("This category doesn't exists.");
		}
	}

}
