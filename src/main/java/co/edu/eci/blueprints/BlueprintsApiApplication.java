package co.edu.eci.blueprints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BlueprintsApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlueprintsApiApplication.class, args);
    }
}
