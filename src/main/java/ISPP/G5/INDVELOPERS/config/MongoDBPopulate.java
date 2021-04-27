package ISPP.G5.INDVELOPERS.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ISPP.G5.INDVELOPERS.models.Category;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Order;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.paypal.OrderRepository;
import ISPP.G5.INDVELOPERS.repositories.CategoriaRepository;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.DeveloperSubscriptionRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;
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
            DeveloperSubscriptionRepository developerSubscriptionRepository, OrderRepository orderRepository, CategoriaRepository categoriaRepository,
            IncidentRepository incidentRepository) {
        return strings -> {
        	userEntityRepository.deleteAll();
            developerRepository.deleteAll();
            gameRepository.deleteAll();
            reviewRepository.deleteAll();
            ownedGameRepository.deleteAll();
            publicationRepository.deleteAll();
            developerSubscriptionRepository.deleteAll();
            orderRepository.deleteAll();
            categoriaRepository.deleteAll();
            incidentRepository.deleteAll();
			
            /*
                ================= USERS =================
             */
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            UserEntity master = new UserEntity("master",
                    passwordEncoder.encode("master123"),
                    "master@indvelopers.com",
                    Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    true);

            userEntityRepository.save(master);
            
            Developer master2 = new Developer("master2",
                    passwordEncoder.encode("master212"),
                    "master2@indvelopers.com",
                    null, Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    null, null, true,formatter.parse("1999-05-03"), new ArrayList<Developer>());

            developerRepository.save(master2);
            
            Developer alvaro = new Developer("alvaro",
                    passwordEncoder.encode("alvaro123"),
                    "alvaro@gmail.com",
                     null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());

            developerRepository.save(alvaro);
            
            Developer John = new Developer("JohnDoe1",
                    passwordEncoder.encode("JohnDoe1"),
                    "sb-n43pka5914239@personal.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, false, formatter.parse("1994-03-06"), new ArrayList<Developer>());

            developerRepository.save(John);
            
            Developer dummyDeveloper = new Developer("dummyDeveloper",
                    passwordEncoder.encode("dummyDeveloper"),
                    "sb-m439ui5916012@business.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1998-03-06"), new ArrayList<Developer>());

            developerRepository.save(dummyDeveloper);
            
            Developer dummyDeveloper2 = new Developer("dummyDeveloper2",
                    passwordEncoder.encode("dummyDeveloper2"),
                    "dummyDeveloper2@gmail.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, false, formatter.parse("1997-07-14"), new ArrayList<Developer>());

            developerRepository.save(dummyDeveloper2);
            
            Developer fernando = new Developer("fernando",
                    passwordEncoder.encode("fernando"),
                    "fernando@gmail.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-12"), new ArrayList<Developer>());

            developerRepository.save(fernando);

            Developer asdf = new Developer("asdf",
                    passwordEncoder.encode("asdfasdf"),
                    "asdf@asdf.com",
                    null, Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1991-03-06"), new ArrayList<Developer>());

            developerRepository.save(asdf);

            Developer asdfasdf = new Developer("asdfasdf",
                    passwordEncoder.encode("asdfasdf"),
                    "asdf@asdf.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, false, formatter.parse("1990-03-06"), new ArrayList<Developer>());

            developerRepository.save(asdfasdf);
            
            /* Credenciales de los profesores */
            
            Developer carlos = new Developer("carlosmuller",
                    passwordEncoder.encode("carlosmuller"),
                    "cmuller@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());

            Developer bedilia = new Developer("bediliaestrada",
                    passwordEncoder.encode("bediliaestrada"),
                    "iestrada@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer pablo = new Developer("pablofernandez",
                    passwordEncoder.encode("pablofernandez"),
                    "pablofm@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer cristina = new Developer("cristinacabanillas",
                    passwordEncoder.encode("cristinacabanillas"),
                    "cristinacabanillas@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer antonio = new Developer("antonioruiz",
                    passwordEncoder.encode("antonioruiz"),
                    "aruiz@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer rafael = new Developer("rafaelfresno",
                    passwordEncoder.encode("rafaelfresno"),
                    "rfrenos@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            developerRepository.save(carlos);
            developerRepository.save(bedilia);
            developerRepository.save(pablo);
            developerRepository.save(cristina);
            developerRepository.save(antonio);
            developerRepository.save(rafael);

            /*
            ================= GAMES =================
             */
         
            Category categoria1 = new Category("Accion");
            categoriaRepository.save(categoria1);
            Category categoria2 = new Category("Arcade");
            categoriaRepository.save(categoria2);
            Category categoria3 = new Category("Deportivo");
            categoriaRepository.save(categoria3);
            Category categoria4 = new Category("Estrategia");
            categoriaRepository.save(categoria4);
            Category categoria5 = new Category("Simulacion");
            categoriaRepository.save(categoria5);
            
            List<Category> categorias = new ArrayList<Category>();
            categorias.add(categoria1);
            categorias.add(categoria5);
            
            Date fecha = new Date();
            
            Game game1 = new Game("25 caminos oscuros",
                    "Es un juego en el que elijas el camino que elijas pierdes",
                    "No tiene grandes requisitos, 20 gigas de ram",
                    25.65,
                    "1618508350667_blob",
                    true, 
                    dummyDeveloper,
                    null, categorias, fecha, 18);
            gameRepository.save(game1);
            
            
            
            Game game2 = new Game("Payaso que salta",
                    "No intentes que el payaso se quede quieto, siempre salta",
                    "Con tener ordenador ya te tira",
                    21.43,
                    "1618508350667_blob",
                    true, 
                    dummyDeveloper,
                    null, categorias, fecha, 18);
            
            gameRepository.save(game2);
            
           
            Game game3 = new Game("Almas oscuras",
                    "Juego super complicado que no podras pasarte",
                    "Requiere de una grafica de ultima generacion",
                    39.99,
                    "1618508350667_blob",
                    true, 
                    dummyDeveloper,
                    null, categorias, fecha, 18);
            
            gameRepository.save(game3);

            /*
            ================= REVIEWS =================
            */
          
          	Review r1 = new Review("text", 2., false, game1, master2);
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
            
            /*
            ================= FOLLOWERS =================
             */
            
            alvaro.getFollowing().add(fernando);
            alvaro.getFollowing().add(dummyDeveloper);
            developerRepository.save(alvaro);
            
            dummyDeveloper.getFollowing().add(fernando);
            developerRepository.save(dummyDeveloper);
        };

    }
}

