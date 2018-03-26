package scat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Import;

// TODO N+1 problem in cities
// TODO N+1 problem in schools
// TODO postman collections
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@Import(ScatConfig.class)
public class ScatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScatApplication.class, args);
	}

}
