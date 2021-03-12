package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Prueba;
import ISPP.G5.INDVELOPERS.repositories.PruebaRepository;

@Service
public class PruebaService {
	
	@Autowired
	private PruebaRepository repository;
	
	public String communicateWithReact() {
		return "Mensaje desde Spring";
	}
	
	public List<Prueba> findAll(){
		return repository.findAll();
	}
	
	public String addPrueba(Prueba prueba) {
		repository.save(prueba);
		return "Added prueba with Id:"+ prueba.getId();
	}

}
