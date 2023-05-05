package az.company.mspayment.client;


import az.company.mspayment.controller.FallbackController;

import az.company.mspayment.model.client.CountryDto;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}",
        configuration = CountryClient.FeignConfiguration.class,
        fallback = FallbackController.class
)
public interface CountryClient {

    @GetMapping("/api/countries")
//    @CircuitBreaker(name = "getAllAvailableCountries",fallbackMethod = "getAllAvailableCountriesFallback")
    public List<CountryDto> getAllAvailableCountries(@RequestParam String currency);

//    default void getAllAvailableCountriesFallback(@RequestParam String currency){
//        return ResponseEntity.ok("dd");
//    }

    @RequiredArgsConstructor
    class FeignConfiguration {

        private final CircuitBreakerRegistry registry;

        @Bean
        public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
            return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
        }


    }
}
