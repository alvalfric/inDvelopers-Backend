package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.SpamWord;
import ISPP.G5.INDVELOPERS.repositories.SpamWordRepository;

@Service
public class SpamWordService {

	
	@Autowired
	private SpamWordRepository repository;
	
	public List<SpamWord> findAllNoAllowed(){
		List<SpamWord> res = repository.findAll();
		return res;
	}
	
	
	public Boolean isSpam(String text) {
		Boolean res = false;
		String texto = text.toLowerCase().trim().replace("\\s+", " ");
		int spamCount = 0;
		for (SpamWord s : findAllNoAllowed()) {
			if (texto.contains(s.getWord())) {
				spamCount = spamCount + 1;
			}
		}
		Float actualThreshold = (float) spamCount / text.length();
		if(actualThreshold > 0.2) {
			res = true;
		}
		return res;
	}
	public Boolean CheckGame(Game game) {
		Boolean res=false;
		if(isSpam(game.getTitle())) {
			res=true;
		}else if(isSpam(game.getDescription())) {
			res=true;
			
		}else if(isSpam(game.getRequirements())) {
			res=true;
		}
		return res;
	}
	public Boolean CheckReview(Review review) {
		Boolean res=false;
		if(isSpam(review.getText())) {
			res=true;
		}
		return res;
	}
	public Boolean CheckPublication(Publication publication) {
		Boolean res=false;
		if(isSpam(publication.getText())) {
			res=true;
		}
		return res;
	}
	public Boolean CheckDeveloper(Developer developer) {
		Boolean res=false;
		if(isSpam(developer.getDescription())) {
			res=true;
		}else if(isSpam(developer.getTechnologies())) {
			res=true;
		}else if(isSpam(developer.getUsername())) {
			res=true;
		}
		return res;
	}
	public Boolean CheckGetDeveloperDTO(GetDeveloperDTO developerDTO) {
		Boolean res=false;
		if(isSpam(developerDTO.getDescription())) {
			res=true;
		}else if(isSpam(developerDTO.getTechnologies())) {
			res=true;
		}else if(isSpam(developerDTO.getUsername())) {
			res=true;
		}
		return res;
	}
	
}
