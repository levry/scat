package scat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

// TODO batch address
// TODO batch school
@SpringBootApplication
@Import(ScatConfig.class)
public class ScatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScatApplication.class, args);
	}

}
