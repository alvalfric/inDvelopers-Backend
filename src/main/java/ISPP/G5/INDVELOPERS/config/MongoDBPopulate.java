package ISPP.G5.INDVELOPERS.config;

import java.time.LocalDate;
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
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.DeveloperSubscriptionRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

@Configuration
public class MongoDBPopulate<E> {


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Bean
    CommandLineRunner commandLineRunner(
            UserEntityRepository userEntityRepository, DeveloperRepository developerRepository,
            GameRepository gameRepository, ReviewRepository reviewRepository, OwnedGameRepository ownedGameRepository, PublicationRepository publicationRepository,
            DeveloperSubscriptionRepository developerSubscriptionRepository) {
        return strings -> {
        	userEntityRepository.deleteAll();
            developerRepository.deleteAll();
            gameRepository.deleteAll();
            reviewRepository.deleteAll();
            ownedGameRepository.deleteAll();
            publicationRepository.deleteAll();
            developerSubscriptionRepository.deleteAll();

            /*
                ================= USERS =================
             */

            UserEntity master = new UserEntity("master",
                    passwordEncoder.encode("master123"),
                    "https://dummyimage.com/300",
                    Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    true);

            userEntityRepository.save(master);

            Developer master2 = new Developer("master2",
                    passwordEncoder.encode("master212"),
                    "https://dummyimage.com/300",
                    null, null, Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(master2);
            
            Developer alvaro = new Developer("alvaro",
                    passwordEncoder.encode("alvaro123"),
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
                    null, null, false);

            developerRepository.save(dummyDeveloper2);
            
            Developer fernando = new Developer("fernando",
                    passwordEncoder.encode("fernando"),
                    "fernando@gmail.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(fernando);

            Developer asdf = new Developer("asdf",
                    passwordEncoder.encode("asdfasdf"),
                    "asdf@asdf.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            developerRepository.save(asdf);

            Developer asdfasdf = new Developer("asdfasdf",
                    passwordEncoder.encode("asdfasdf"),
                    "asdf@asdf.com",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, false);

            developerRepository.save(asdfasdf);
            
            /* Credenciales de los profesores */
            
            Developer carlos = new Developer("carlosmuller",
                    passwordEncoder.encode("carlosmuller"),
                    "cmuller@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);

            Developer bedilia = new Developer("bediliaestrada",
                    passwordEncoder.encode("bediliaestrada"),
                    "iestrada@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);
            
            Developer pablo = new Developer("pablofernandez",
                    passwordEncoder.encode("pablofernandez"),
                    "pablofm@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);
            
            Developer cristina = new Developer("cristinacabanillas",
                    passwordEncoder.encode("cristinacabanillas"),
                    "cristinacabanillas@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);
            
            Developer antonio = new Developer("antonioruiz",
                    passwordEncoder.encode("antonioruiz"),
                    "aruiz@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);
            
            Developer rafael = new Developer("rafaelfresno",
                    passwordEncoder.encode("rafaelfresno"),
                    "rfrenos@us.es",
                    null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true);
            
            developerRepository.save(carlos);
            developerRepository.save(bedilia);
            developerRepository.save(pablo);
            developerRepository.save(cristina);
            developerRepository.save(antonio);
            developerRepository.save(rafael);

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
            
            /*
            ================= SUBSCRIPTIONS =================
             */
            
            DeveloperSubscription devSub1 = new DeveloperSubscription(master2,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub1);
            
            DeveloperSubscription devSub2 = new DeveloperSubscription(dummyDeveloper,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub2);
            
            DeveloperSubscription devSub3 = new DeveloperSubscription(dummyDeveloper2,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub3);
            
            DeveloperSubscription devSub4 = new DeveloperSubscription(alvaro,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub4);
            
            /* Subscripciones de los profesores */
            
            DeveloperSubscription subCarlos = new DeveloperSubscription(carlos,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            DeveloperSubscription subBedilia = new DeveloperSubscription(bedilia,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            DeveloperSubscription subPablo = new DeveloperSubscription(pablo,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            DeveloperSubscription subCristina = new DeveloperSubscription(cristina,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            DeveloperSubscription subAntonio = new DeveloperSubscription(antonio,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            DeveloperSubscription subRafael = new DeveloperSubscription(rafael,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            
            developerSubscriptionRepository.save(subCarlos);
            developerSubscriptionRepository.save(subBedilia);
            developerSubscriptionRepository.save(subPablo);
            developerSubscriptionRepository.save(subCristina);
            developerSubscriptionRepository.save(subAntonio);
            developerSubscriptionRepository.save(subRafael);
        };

    }
}

