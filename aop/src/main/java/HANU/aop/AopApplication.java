package HANU.aop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(CacutorService cacutorService) {
        return runner -> {
            double result = cacutorService.add(5, 10);
            System.out.println("Result: " + result);
        };
    }


}
