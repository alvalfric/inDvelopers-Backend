package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.Security.JwtTokenProvider;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperDashboardService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	public Integer publicationFindAll(){
		return publicationRepository.findAll().size();
	}
	
	public Integer reviewFindAll(){
		return reviewRepository.findAll().size();
	}
	
	public Integer gameFindAll() {
		return gameRepository.findAll().size();
	}

}
