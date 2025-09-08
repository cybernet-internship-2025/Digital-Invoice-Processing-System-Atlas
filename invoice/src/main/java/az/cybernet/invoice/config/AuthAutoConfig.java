package az.cybernet.invoice.config;

import az.cybernet.authstarter.JwtAuthFilter;
import az.cybernet.authstarter.JwtService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({JwtAuthFilter.class, JwtService.class})
public class AuthAutoConfig {
}
