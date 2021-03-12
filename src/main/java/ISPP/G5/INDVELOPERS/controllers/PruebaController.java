package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Prueba;
import ISPP.G5.INDVELOPERS.services.PruebaService;

@CrossOrigin("*")
@RestController
@RequestMapping("/Prueba")
public class PruebaController {

	@Autowired
	private PruebaService service;
	
	@GetMapping("/communicate")
	public String communicateWithReact() {
		return service.communicateWithReact();
	}
	@GetMapping("/findAll")
	public List<Prueba> findAll() {
		return service.findAll();
	}
	@PostMapping("/add")
	public String addPrueba(@RequestBody Prueba prueba) {
		return service.addPrueba(prueba);
	}
}
