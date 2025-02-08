package fr.it.exalt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fr.it.exalt")
public class JackpotApplication {

    public static void main(String[] args) {
        SpringApplication.run(JackpotApplication.class, args);
    }

}
