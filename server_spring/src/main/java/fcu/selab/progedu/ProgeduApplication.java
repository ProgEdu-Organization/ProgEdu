package fcu.selab.progedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoAutoConfiguration.class})
public class ProgeduApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgeduApplication.class, args);
	}

}
