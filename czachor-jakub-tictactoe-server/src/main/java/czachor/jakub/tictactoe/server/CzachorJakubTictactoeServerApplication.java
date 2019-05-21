package czachor.jakub.tictactoe.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CzachorJakubTictactoeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CzachorJakubTictactoeServerApplication.class, args);
	}

}
