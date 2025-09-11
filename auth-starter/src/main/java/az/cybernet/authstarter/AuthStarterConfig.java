package az.cybernet.authstarter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "az.cybernet.authstarter")
@PropertySource(value = "classpath:auth-application.yml", factory = YamlPropertySourceFactory.class)
public class AuthStarterConfig {
}