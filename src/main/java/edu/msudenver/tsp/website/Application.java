package edu.msudenver.tsp.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*
    @Bean
    @Autowired
    public UserService userService(@Autowired final RestService restService) {
        return new UserService(restService);
    }
    */
}
