package az.company.mspayment.client;


import az.company.mspayment.model.client.CountryDto;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}")
public interface CountryClient {

    @GetMapping("/api/countries")
    public List<CountryDto> getAllAvailableCountries(@RequestParam String currency);

    class FeignConfiguration {

        private final CircuitBreakerRegistry registry;

        public FeignConfiguration(CircuitBreakerRegistry registry) {
            this.registry = registry;
        }

        @Bean
        public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
            return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
        }
    }
}
