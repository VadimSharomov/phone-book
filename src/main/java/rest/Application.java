package rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Vadim Sharomov
 */
@SpringBootApplication
@ComponentScan({"rest","services","dao","interfaces"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}