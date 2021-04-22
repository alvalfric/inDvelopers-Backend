package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Categoria;
import ISPP.G5.INDVELOPERS.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public List<Categoria> findAll(){
		return repository.findAll();
	}
	
	public Categoria findById(String id) {
		Optional<Categoria> categoria = repository.findById(id);
		if (categoria.get() != null) {
			return categoria.get();
		} else {
			throw new IllegalArgumentException("This category doesn't exist.");
		}
	}
	
	public Categoria findByTitle(String title) {
		Categoria categoria = null;
		List<Categoria> categorias = repository.findAll();
		for(Categoria c: categorias) {
			if(c.getTitle().contentEquals(title)) {
				categoria = c;
			}
		}
		return categoria;
	}
	
	public String addCategory(Categoria c) {
		List<Categoria> categorias = repository.findAll();
		if(!categorias.contains(c)) {
			repository.save(c);
			return "Successfully added with id: " + c.getId();
		}else {
			throw new IllegalArgumentException("This category already exists.");
		}
		
	}
	
	public String deleteCategory(Categoria c) {
		List<Categoria> categorias = repository.findAll();
		if(categorias.contains(c)) {
			repository.delete(c);
			return "Successfully deleted with id: " + c.getId();
		}else {
			throw new IllegalArgumentException("This category doesn't exists.");
		}
	}

}
