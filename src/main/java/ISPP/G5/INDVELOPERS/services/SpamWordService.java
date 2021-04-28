package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ISPP.G5.INDVELOPERS.models.SpamWord;
import ISPP.G5.INDVELOPERS.repositories.SpamWordRepository;

@Service
public class SpamWordService {

	
	@Autowired
	private SpamWordRepository repository;
	
	public List<SpamWord> findAllNotAllowed(){
		List<SpamWord> words = repository.findAll();
		List<SpamWord> res = new ArrayList<SpamWord>();
		for(SpamWord s: words) {
			if(!s.getIsAllowed()) {
				res.add(s);
			}
		}
		return res;
	}
	
	public List<SpamWord> findAllAllowed(){
		List<SpamWord> words = repository.findAll();
		List<SpamWord> res = new ArrayList<SpamWord>();
		for(SpamWord s: words) {
			if(s.getIsAllowed()) {
				res.add(s);
			}
		}
		return res;
	}
	
	
	public Boolean isSpam(String text) {
		Boolean res = false;
		String texto = text.toLowerCase().trim().replace("\s+", " ");
		int spamCount = 0;
		for (SpamWord s : findAllAllowed()) {
			if (texto.contains(s.getWord())) {
				spamCount = spamCount + 1;
			}
		}
		Float actualThreshold = (float) spamCount / text.length();
		if(actualThreshold > 0.2) {
			res = true;
		}
		for (SpamWord s : findAllNotAllowed()) {
			if (texto.contains(s.getWord())) {
				res = true;
				break;
			}
		}
		return res;
	}
}
