import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Vadim Sharomov
 */
@SpringBootApplication
@EnableJpaRepositories("repository")
@EntityScan("entity")
@ComponentScan({"controller","services","dao","config"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}