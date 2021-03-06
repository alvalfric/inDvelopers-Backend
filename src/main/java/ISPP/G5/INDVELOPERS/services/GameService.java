package ISPP.G5.INDVELOPERS.services;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private ReviewRepository repository;

	public List<Game> findAll() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findAll();
		Collections.reverse(res);
		return res;
	}

	public List<Game> findVerified() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findVerified();
		Collections.reverse(res);
		return res;
	}

	public List<Game> findNotRevised() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findNotRevised();
		Collections.reverse(res);
		return res;
	}

	public String addGame(Game game, Developer developer) {
		Assert.notNull(game);

		game.setCreator(developer);
		game.setDiscount(0.);
		Date fechaCreacion = new Date();
		game.setFechaCreacion(fechaCreacion);
		this.gameRepository.save(game);
		return "Added game with title:" + game.getTitle();
	}

	public List<Game> findByTitle(String title) {
		List<Game> res = new ArrayList<>();
		res = this.gameRepository.findByTitle(title);
		Collections.reverse(res);
		return res;
	}

	public List<Game> findByDeveloper(String developerId) {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findByDeveloper(developerId);
		Collections.reverse(res);
		return res;
	}

	public List<Game> findByMyGames(String developerId) {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findByMyGames(developerId);
		Collections.reverse(res);
		return res;

	}

	public List<Game> gamesByDevelopersFollowed(Developer developer) {

		List<Game> res = new ArrayList<>();
		List<Developer> followed = developer.getFollowing();
		for (Developer d : followed) {
			res.addAll(gameRepository.findByDeveloper(d.getId()));
		}
		return res;

	}

	public List<Game> findByTopSellers() {
		return findVerified().stream().filter(g -> mediaReviews(g) >= 4).collect(Collectors.toList());
	}

	public String updateGame(Game game) {
		Assert.notNull(game);
		this.gameRepository.save(game);
		return "Updated game with title:" + game.getTitle();
	}

	public void deleteGame(String id) {
//		this.cloudStorageService.deleteFile(this.findById(id).getIdCloud());
		this.gameRepository.deleteById(id);
	}

	public Game findById(String id) {
		return this.gameRepository.findById(id).orElse(null);
	}

	public List<Game> findByTitleVerified(String title) {
		List<Game> res = new ArrayList<>();
		res = this.gameRepository.findByTitleVerified(title);
		Collections.reverse(res);
		return res;
	}

	public boolean checkGameTitle(String gameTitle) {
		boolean result = false;

		if (!this.gameRepository.findByTitle(gameTitle).isEmpty()) {
			result = true;
		}

//		for (Game game : this.gameRepository.findAll()) {
//			if (game.getTitle().equals(gameTitle)) {
//				result = true;
//			}
//		}

		return result;
	}

	public Double mediaReviews(Game g) {
		Double res = 0.0;
		List<Review> reviews = this.repository.findAllByGameId(g.getId());
		for (Review r : reviews) {
			res += r.getScore();
		}
		res = res / reviews.size();
		return res;
	}

	public List<Game> findByTitleVerifiedOrCategorie(String input) {
		List<Game> res = this.gameRepository.findByTitleVerified(input);
		for (Game g : this.findVerified()) {
			for (Category c : g.getCategorias()) {
				if (c.getTitle().toLowerCase().contains(input.toLowerCase()) && !res.contains(g)) {
					res.add(g);
				}
			}
		}
		Collections.reverse(res);
		return res;
	}

	public List<Game> findByPrice(Double price) {
//		List<Game> res = this.gameRepository.findByPrice(price);		
		List<Game> res = new ArrayList<Game>();
		List<Game> verifiedGames = this.gameRepository.findVerified();

		for (Game game : verifiedGames) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
			DecimalFormat df2 = (DecimalFormat) nf;
			Double calculatedDiscount = game.getPrice() * game.getDiscount();
			Double discount = Double.valueOf(df2.format(calculatedDiscount));
			Double gamePriceWithDiscount = game.getPrice() - discount;
			if (gamePriceWithDiscount <= price) {
				res.add(game);
			}
		}
		
		Collections.reverse(res);
		return res;
	}

	public List<Game> findAllWithDiscount() {
		List<Game> res = this.gameRepository.findAllWithDiscount();
		Collections.reverse(res);
		return res;
	}

}
