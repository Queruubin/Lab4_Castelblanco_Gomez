package co.edu.eci.blueprints.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Define el {@link DataSource} de PostgreSQL únicamente cuando el perfil
 * {@code postgres} está activo. Como la auto-configuración de DataSource está
 * excluida en {@code BlueprintsApiApplication}, aquí se construye la conexión a
 * partir de las propiedades {@code spring.datasource.*} (via
 * {@link DataSourceProperties}) para que el repositorio
 * {@code PostgresBlueprintPersistence} pueda operar.
 */
@Configuration
@Profile("postgres")
public class PostgresDataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
