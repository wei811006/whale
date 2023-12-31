package help.wei.whale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WhaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhaleApplication.class, args);
	}

}
