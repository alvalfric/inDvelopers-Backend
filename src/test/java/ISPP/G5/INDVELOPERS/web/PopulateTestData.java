package ISPP.G5.INDVELOPERS.web;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;

public class PopulateTestData {

    static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
	
	public static Developer dummyDeveloper(String developerId) {
		Developer dummyDeveloper = new Developer("dummyDeveloper",
                passwordEncoder.encode("dummyDeveloper"),
                "dummyDeveloper@gmail.com",
                null, null, Stream.of(UserRole.USER).collect(Collectors.toSet()),
                null, null, true);
		
		dummyDeveloper.setId(developerId);
		
		return dummyDeveloper;
	}
	
	public static Game dummyGame(Developer dummyDeveloper, String gameID) {
        Game dummyGame = new Game("25 caminos oscuros",
                "Es un juego en el que elijas el camino que elijas pierdes",
                "No tiene grandes requisitos, 20 gigas de ram",
                25.65,
                "25.icloud.",
                true, 
                dummyDeveloper);
        
        dummyGame.setId(gameID);
        
        return dummyGame;
	}
	
}
