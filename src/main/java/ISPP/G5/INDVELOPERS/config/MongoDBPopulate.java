package ISPP.G5.INDVELOPERS.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.SpamWord;
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
import ISPP.G5.INDVELOPERS.repositories.SpamWordRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

@Configuration
public class MongoDBPopulate<E> {


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Bean
    CommandLineRunner commandLineRunner(
            UserEntityRepository userEntityRepository, DeveloperRepository developerRepository,
            GameRepository gameRepository, ReviewRepository reviewRepository, OwnedGameRepository ownedGameRepository, PublicationRepository publicationRepository,
            DeveloperSubscriptionRepository developerSubscriptionRepository, OrderRepository orderRepository, CategoriaRepository categoriaRepository,
            IncidentRepository incidentRepository, SpamWordRepository spamWordRepository) {
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
            spamWordRepository.deleteAll();
			
            /*
                ================= USERS =================
             */
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            
            Developer master = new Developer("master",
                    passwordEncoder.encode("n8KVHrpYrTDj"),
                    "sb-n43pka5914239@personal.example.com",
                     null, Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    "Administrador para inDvelopers", "Java", true, formatter.parse("1996-01-12"), new ArrayList<Developer>());

            developerRepository.save(master);
               
            new Developer("username", "password", "email", "image", Stream.of(UserRole.USER).collect(Collectors.toSet()),
            		"description", "technologies", true, formatter.parse("2000-01-01"), new ArrayList<Developer>());
            
            Developer alvaro = new Developer("alvaro",
                    passwordEncoder.encode("Y54Swgosb7av"),
                    "sb-n43pka5914239@personal.example.com",
                     null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Desarrollador para inDvelopers", "Java", true, formatter.parse("1996-01-12"), new ArrayList<Developer>());

            developerRepository.save(alvaro);
                        
            Developer fernando = new Developer("fernando",
                    passwordEncoder.encode("YroS67EgOjRn"),
                    "sb-n43pka5914239@personal.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    null, null, true, formatter.parse("1997-03-12"), new ArrayList<Developer>());

            developerRepository.save(fernando);
            
            Developer alvaqjim = new Developer("alvaqjim",
                    passwordEncoder.encode("MhPQpKKb1Ieh"),
                    "sb-uiz525914316@personal.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Soy un desarrollador de videojuegos", "Unreal Engine, Maya y Photoshop", true, formatter.parse("1997-03-12"), new ArrayList<Developer>());

            developerRepository.save(alvaqjim);
            
            /* Credenciales de los profesores */
            
            Developer carlos = new Developer("carlosmuller",
                    passwordEncoder.encode("PEfKVcDp3Dux"),
                    "sb-1yi9g5948590@business.example.com",
                    null,
                    Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());

            Developer bedilia = new Developer("bediliaestrada",
                    passwordEncoder.encode("cfjZx47YknwD"),
                    "sb-043o1g5959188@business.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer pablo = new Developer("pablofernandez",
                    passwordEncoder.encode("Csx3fhGyWzjn"),
                    "sb-yuwre5959198@business.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer cristina = new Developer("cristinacabanillas",
                    passwordEncoder.encode("3ixfVZizLbpN"),
                    "sb-szvcz5959202@business.example.com@us.es",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer antonio = new Developer("antonioruiz",
                    passwordEncoder.encode("GmQUfLFTyWsw"),
                    "sb-ckd7u5959194@business.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            Developer rafael = new Developer("rafaelfresno",
                    passwordEncoder.encode("mlUXLwFa00cx"),
                    "sb-q27xx5959180@business.example.com",
                    null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                    "Description", "Technologies", true, formatter.parse("1997-03-06"), new ArrayList<Developer>());
            
            developerRepository.save(carlos);
            developerRepository.save(bedilia);
            developerRepository.save(pablo);
            developerRepository.save(cristina);
            developerRepository.save(antonio);
            developerRepository.save(rafael);

            /*
            ================= CATEGORIES =================
             */
         
            List<Category> categories = new ArrayList<Category>();
            Category actionCategory = new Category("Action");
            Category adventureCategory = new Category("Adventure");
            Category casualCategory = new Category("Casual");
            Category simulatorCategory = new Category("Simulator");
            Category strategyCategory = new Category("Strategy");
            Category rolCategory = new Category("Rol");
            Category singlePlayerCategory = new Category("Single Player");
            Category freeToPlayCategory = new Category("Free to Play");
            Category twoDimensionCategory = new Category("2D");
            Category violentCategory = new Category("Gore");
            Category sportsCategory = new Category("Sports");
            Category multiplayerCategory = new Category("Multiplayer");
            Category puzzlesCategory = new Category("Puzzles");
            Category racingCategory = new Category("Racing");
            Category fantasyCategory = new Category("Fantasy");
            Category threeDimensionCategory = new Category("3D");
            Category nudityCategory = new Category("Nudity");
            Category animeCategory = new Category("Anime");
            Category pixelCategory = new Category("Pixel");
            Category firstPersonCategory = new Category("First Person");
            Category funCategory = new Category("Fun");
            Category scienceFictionCategory = new Category("Science Fiction");
            Category explorationCategory = new Category("Exploration");
            Category shooterCategory = new Category("Shooter");
            Category arcadeCategory = new Category("Arcade");
            Category scaryCategory = new Category("Scary");
            Category retroCategory = new Category("Retro");
            Category survivalCategory = new Category("Survival");
            Category platformerCategory = new Category("Platformer");
            Category warCategory = new Category("War");
            Category hardCategory = new Category("Hard");

            categories.add(actionCategory);
            categories.add(adventureCategory);
            categories.add(casualCategory);
            categories.add(simulatorCategory);
            categories.add(strategyCategory);
            categories.add(rolCategory);
            categories.add(singlePlayerCategory);
            categories.add(freeToPlayCategory);
            categories.add(twoDimensionCategory);            
            categories.add(violentCategory);
            categories.add(sportsCategory);
            categories.add(multiplayerCategory);
            categories.add(puzzlesCategory);
            categories.add(racingCategory);
            categories.add(fantasyCategory);
            categories.add(threeDimensionCategory);
            categories.add(nudityCategory);
            categories.add(animeCategory);
            categories.add(pixelCategory);
            categories.add(firstPersonCategory);
            categories.add(funCategory);
            categories.add(scienceFictionCategory);
            categories.add(explorationCategory);
            categories.add(shooterCategory);
            categories.add(arcadeCategory);
            categories.add(scaryCategory);
            categories.add(retroCategory);
            categories.add(survivalCategory);
            categories.add(platformerCategory);
            categories.add(warCategory);
            categories.add(hardCategory);
            
            categoriaRepository.saveAll(categories);
            
            /*
            ================= GAMES =================
             */
            
            Set<Category> iceSecretCategories = new HashSet<Category>();
            iceSecretCategories.add(funCategory);
            iceSecretCategories.add(arcadeCategory);

            Date fecha = new Date();
                        
            Game iceSecret = new Game("Ice' secret",
                    "Ice was a necessity but can't be found",
                    "Dual Core and 4GB of Ram",
                    9.99,
                    "1619011155211_blob",
                    true,
                    alvaqjim,
                    null, 
                    iceSecretCategories, fecha, 12,0.);
            gameRepository.save(iceSecret);
            
            Set<Category> aLovelyAfternoonCategories = new HashSet<Category>();
            aLovelyAfternoonCategories.add(funCategory);
            aLovelyAfternoonCategories.add(arcadeCategory);

            Game aLovelyAfternoon = new Game("A lovely afternoon",
                    "Two friends looking having a blast together",
                    "Dual Core and 4GB of Ram",
                    2.0,
                    "1619011221431_blob",
                    true,
                    alvaqjim,
                    null, 
                    aLovelyAfternoonCategories, fecha, 18,0.);
            gameRepository.save(aLovelyAfternoon);

            /*
            ================= REVIEWS =================
            */
          
          	Review r1 = new Review("Loved the story but found it hard to play", 4., false, iceSecret, alvaro);
			      reviewRepository.save(r1);
          
            /*
            ================= OWNED-GAMES =================
             */
            
            List<Game> gamesOfDeveloperAlvaro = new ArrayList<Game>();
            gamesOfDeveloperAlvaro.add(iceSecret);
            OwnedGame ownedGame1 = new OwnedGame(alvaro, gamesOfDeveloperAlvaro);
            
            ownedGameRepository.save(ownedGame1);
            
            /*
            ================= SUBSCRIPTIONS =================
             */
            
            DeveloperSubscription devSub1 = new DeveloperSubscription(master,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub1);
                        
            DeveloperSubscription devSub2 = new DeveloperSubscription(alvaro,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub2);
            
            DeveloperSubscription devSub3 = new DeveloperSubscription(fernando,LocalDate.of(2000, 01, 01),LocalDate.of(2999, 12, 31));
            developerSubscriptionRepository.save(devSub3);
            
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
            alvaro.getFollowing().add(alvaqjim);
            developerRepository.save(alvaro);
            
            fernando.getFollowing().add(alvaqjim);
            developerRepository.save(fernando);
            
            /*
            ================= Spam Words =================
             */
            
            spamWordRepository.save(new SpamWord("anal"));
            spamWordRepository.save(new SpamWord("anus"));
            spamWordRepository.save(new SpamWord("arse"));
            spamWordRepository.save(new SpamWord("ass"));
            spamWordRepository.save(new SpamWord("ass fuck"));
            spamWordRepository.save(new SpamWord("ass hole"));
            spamWordRepository.save(new SpamWord("assfucker"));
            spamWordRepository.save(new SpamWord("asshole"));
            spamWordRepository.save(new SpamWord("assshole"));
            spamWordRepository.save(new SpamWord("bastard"));
            spamWordRepository.save(new SpamWord("bitch"));
            spamWordRepository.save(new SpamWord("black cock"));
            spamWordRepository.save(new SpamWord("bloody hell"));
            spamWordRepository.save(new SpamWord("boong"));
            spamWordRepository.save(new SpamWord("cock"));
            spamWordRepository.save(new SpamWord("cockfucker"));
            spamWordRepository.save(new SpamWord("cocksuck"));
            spamWordRepository.save(new SpamWord("cocksucker"));
            spamWordRepository.save(new SpamWord("coon"));
            spamWordRepository.save(new SpamWord("coonnass"));
            spamWordRepository.save(new SpamWord("crap"));
            spamWordRepository.save(new SpamWord("cunt"));
            spamWordRepository.save(new SpamWord("cyberfuck"));
            spamWordRepository.save(new SpamWord("damn"));
            spamWordRepository.save(new SpamWord("darn"));
            spamWordRepository.save(new SpamWord("dick"));
            spamWordRepository.save(new SpamWord("dirty"));
            spamWordRepository.save(new SpamWord("douche"));
            spamWordRepository.save(new SpamWord("dummy"));
            spamWordRepository.save(new SpamWord("erect"));
            spamWordRepository.save(new SpamWord("erection"));
            spamWordRepository.save(new SpamWord("erotic"));
            spamWordRepository.save(new SpamWord("escort"));
            spamWordRepository.save(new SpamWord("fag"));
            spamWordRepository.save(new SpamWord("faggot"));
            spamWordRepository.save(new SpamWord("fuck"));
            spamWordRepository.save(new SpamWord("fuck off"));
            spamWordRepository.save(new SpamWord("fuck you"));
            spamWordRepository.save(new SpamWord("fuckass"));
            spamWordRepository.save(new SpamWord("fuckhole"));
            spamWordRepository.save(new SpamWord("god damn"));
            spamWordRepository.save(new SpamWord("gook"));
            spamWordRepository.save(new SpamWord("god damn"));
            spamWordRepository.save(new SpamWord("hard core"));
            spamWordRepository.save(new SpamWord("hardcore"));
            spamWordRepository.save(new SpamWord("homoerotic"));
            spamWordRepository.save(new SpamWord("hore"));
            spamWordRepository.save(new SpamWord("lesbian"));
            spamWordRepository.save(new SpamWord("lesbians"));
            spamWordRepository.save(new SpamWord("mother fucker"));
            spamWordRepository.save(new SpamWord("motherfuck"));
            spamWordRepository.save(new SpamWord("motherfucker"));
            spamWordRepository.save(new SpamWord("negro"));
            spamWordRepository.save(new SpamWord("nigger"));
            spamWordRepository.save(new SpamWord("niger"));
            spamWordRepository.save(new SpamWord("orgasim"));
            spamWordRepository.save(new SpamWord("orgasm"));
            spamWordRepository.save(new SpamWord("orgasm"));
            spamWordRepository.save(new SpamWord("penis"));
            spamWordRepository.save(new SpamWord("penisfucker"));
            spamWordRepository.save(new SpamWord("piss"));
            spamWordRepository.save(new SpamWord("piss off"));
            spamWordRepository.save(new SpamWord("porn"));
            spamWordRepository.save(new SpamWord("porno"));
            spamWordRepository.save(new SpamWord("pornography"));
            spamWordRepository.save(new SpamWord("pussy"));
            spamWordRepository.save(new SpamWord("retard"));
            spamWordRepository.save(new SpamWord("sadist"));
            spamWordRepository.save(new SpamWord("sex"));
            spamWordRepository.save(new SpamWord("sexy"));
            spamWordRepository.save(new SpamWord("shit"));
            spamWordRepository.save(new SpamWord("slut"));
            spamWordRepository.save(new SpamWord("son of a bitch"));
            spamWordRepository.save(new SpamWord("suck"));
            spamWordRepository.save(new SpamWord("tits"));
            spamWordRepository.save(new SpamWord("viagra"));
            spamWordRepository.save(new SpamWord("whore"));
            spamWordRepository.save(new SpamWord("xxx"));
            
        };

    }
}

