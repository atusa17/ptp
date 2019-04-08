package edu.msudenver.tsp.website;

import edu.msudenver.tsp.services.RestService;
import edu.msudenver.tsp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "edu.msudenver.tsp")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Autowired
    public UserService userService(final RestService restService) {
        return new UserService(restService);
    }
}
