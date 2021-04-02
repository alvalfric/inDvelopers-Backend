package ISPP.G5.INDVELOPERS.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

@Configuration
public class MongoDBPopulate<E> {


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Bean
    CommandLineRunner commandLineRunner(
            UserEntityRepository userEntityRepository, DeveloperRepository developerRepository,
            GameRepository gameRepository, ReviewRepository reviewRepository, OwnedGameRepository ownedGameRepository, PublicationRepository publicationRepository) {
        return strings -> {
        	userEntityRepository.deleteAll();
            developerRepository.deleteAll();
            gameRepository.deleteAll();
            reviewRepository.deleteAll();
            ownedGameRepository.deleteAll();
          publicationRepository.deleteAll();

            /*
                ================= USERS =================
             */

            UserEntity master = new UserEntity("master",
                    passwordEncoder.encode("master"),
                    "https://dummyimage.com/300",
                    Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    true);

            userEntityRepository.save(master);

            Developer master2 = new Developer("master2",
                    passwordEncoder.encode("master2"),
                    "https://dummyimage.com/300",
                    null, null, Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(master2);
            
            Developer alvaro = new Developer("alvaro",
                    passwordEncoder.encode("alvaro"),
                    "alvaro@gmail.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(alvaro);
            
            Developer dummyDeveloper = new Developer("dummyDeveloper",
                    passwordEncoder.encode("dummyDeveloper"),
                    "dummyDeveloper@gmail.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(dummyDeveloper);
            
            Developer dummyDeveloper2 = new Developer("dummyDeveloper2",
                    passwordEncoder.encode("dummyDeveloper2"),
                    "dummyDeveloper2@gmail.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(dummyDeveloper2);
            
            /*
            ================= GAMES =================
             */
            
            Game game1 = new Game("25 caminos oscuros",
                    "Es un juego en el que elijas el camino que elijas pierdes",
                    "No tiene grandes requisitos, 20 gigas de ram",
                    25.65,
                    "25.icloud.",
                    true, 
                    dummyDeveloper);
            
            gameRepository.save(game1);
            
            Game game2 = new Game("Payaso que salta",
                    "No intentes que el payaso se quede quieto, siempre salta",
                    "Con tener ordenador ya te tira",
                    21.43,
                    "no tiene",
                    true, 
                    dummyDeveloper);
            
            gameRepository.save(game2);
            
            Game game3 = new Game("Almas oscuras",
                    "Juego super complicado que no podras pasarte",
                    "Requiere de una grafica de ultima generacion",
                    39.99,
                    "no tiene",
                    true, 
                    dummyDeveloper);
            
            gameRepository.save(game3);

            /*
            ================= REVIEWS =================
            */
          
          	Review r1 = new Review("text", 2., game1, master2);
			      reviewRepository.save(r1);
          
            /*
            ================= OWNED-GAMES =================
             */
            
            List<Game> gamesOfDeveloperAlvaro = new ArrayList<Game>();
            gamesOfDeveloperAlvaro.add(game1);
            OwnedGame ownedGame1 = new OwnedGame(alvaro, gamesOfDeveloperAlvaro);
            
            ownedGameRepository.save(ownedGame1);
        };

    }
}

