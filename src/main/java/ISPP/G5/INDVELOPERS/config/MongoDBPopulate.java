package ISPP.G5.INDVELOPERS.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

@Configuration
public class MongoDBPopulate {

	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

	@Bean
	CommandLineRunner commandLineRunner(UserEntityRepository userEntityRepository,
			DeveloperRepository developerRepository, GameRepository gameRepository, PublicationRepository publicationRepository) {
		return strings -> {

			userEntityRepository.deleteAll();
			gameRepository.deleteAll();
			publicationRepository.deleteAll();
			

			/*
			 * ================= USERS =================
			 */

//			UserEntity master = new UserEntity("master", passwordEncoder.encode("master"), "https://dummyimage.com/300",
//					Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()), true);
//
//			userEntityRepository.save(master);
//
//			Developer master2 = new Developer("master2", passwordEncoder.encode("master2"),
//					"https://dummyimage.com/300", null, null,
//					Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()), null, null, true);
//
//			developerRepository.save(master2);
			/*
			 * Game game1 = new Game("25 caminos oscuros",
			 * "Es un juego en el que elijas el camino que elijas pierdes",
			 * "No tiene grandes requisitos, 20 gigas de ram", 25.65, "25.icloud.", true);
			 * 
			 * Game game2 = new Game("Payaso que salta",
			 * "No intentes que el payaso se quede quieto, siempre salta",
			 * "Con tener ordenador ya te tira", 21.43, "no tiene", true);
			 * 
			 * gameRepository.save(game1); gameRepository.save(game2);
			 */
		};

	}
}