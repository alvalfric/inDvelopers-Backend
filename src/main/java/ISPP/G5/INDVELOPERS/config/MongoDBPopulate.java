package ISPP.G5.INDVELOPERS.config;

import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class MongoDBPopulate {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Bean
    CommandLineRunner commandLineRunner(
            UserEntityRepository userEntityRepository,
            GameRepository gameRepository) {
        return strings -> {

            userEntityRepository.deleteAll();
            gameRepository.deleteAll();

            /*
                ================= USERS =================
             */

            UserEntity master = new UserEntity("master",
                    passwordEncoder.encode("master"),
                    "https://dummyimage.com/300",
                    Stream.of(UserRole.USER, UserRole.ADMIN).collect(Collectors.toSet()),
                    true);

            userEntityRepository.save(master);

            Game game1 = new Game("25 caminos oscuros",
                    "Es un juego en el que elijas el camino que elijas pierdes",
                    "No tiene grandes requisitos, 20 gigas de ram",
                    25.65,
                    "25.icloud.",
                    true);

            Game game2 = new Game("Payaso que salta",
                    "No intentes que el payaso se quede quieto, siempre salta",
                    "Con tener ordenador ya te tira",
                    21.43,
                    "no tiene",
                    true);

            gameRepository.save(game1);
            gameRepository.save(game2);
        };

    }
}