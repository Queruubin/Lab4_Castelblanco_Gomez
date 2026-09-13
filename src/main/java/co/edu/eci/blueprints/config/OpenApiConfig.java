package co.edu.eci.blueprints.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
          .info(new Info().title("ARSW BluePrints API")
            .version("2.0")
            .description("Blueprints Laboratory (Java 21 / Spring Boot 3.3.x) — Parte 2: Seguridad con JWT (OAuth 2.0)"))
          .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
          .components(new Components().addSecuritySchemes("bearer-jwt",
            new SecurityScheme()
              .name("bearer-jwt")
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
              .bearerFormat("JWT")));
    }
}
