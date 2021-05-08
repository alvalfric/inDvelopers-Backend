package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ISPP.G5.INDVELOPERS.dtos.CommentaryDTO;
import ISPP.G5.INDVELOPERS.dtos.ForumDTO;
import ISPP.G5.INDVELOPERS.models.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.repositories.SpamWordRepository;

@Service
public class SpamWordService {

	
	@Autowired
	private SpamWordRepository repository;
	
	public List<SpamWord> findAllNoAllowed(){
		List<SpamWord> res = repository.findAll();
		return res;
	}
	
	/*
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
	*/
	public Boolean isSpam(String text) {
		boolean isSpam = false;

		List<String> splitted = Arrays.asList(text.split("\\s|\\p{Punct}"));

		splitted = splitted.stream().filter(s -> s != "").collect(Collectors.toList());

		int n = splitted.size();
		int count = 0;

		List<SpamWord> spam = findAllNoAllowed();

		String lowerCaseString = text.toLowerCase().replaceAll("\\s+{2,}", " ");

		for (SpamWord s : spam) {
			s.setWord(s.getWord().trim());
			count += StringUtils.countMatches(lowerCaseString, s.getWord());
			isSpam = count >= 0.2 * n;
			if (isSpam) {
				break;
			}
		}

		return isSpam;
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

	public Boolean CheckForum(ForumDTO forumDTO) {
		Boolean res=false;
		if(isSpam(forumDTO.getTitle())) {
			res = true;
		}
		return res;
	}

	public Boolean CheckCommentary(CommentaryDTO commentaryDTO) {
		Boolean res=false;
		if(isSpam(commentaryDTO.getDescription())) {
			res = true;
		}
		return res;
	}
	
}
