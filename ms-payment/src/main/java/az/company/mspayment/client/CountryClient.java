package az.company.mspayment.client;


import az.company.mspayment.controller.FallbackController;

import az.company.mspayment.exception.ExceptionResponseFeing;
import az.company.mspayment.model.client.CountryDto;
import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}")
public interface CountryClient {
    @GetMapping("/api/countries")
    @CircuitBreaker(name = "getAllAvailableCountries",fallbackMethod = "getAvailablityBreaker")
    public List<CountryDto> getAllAvailableCountries(@RequestParam String currency);

    default void getAllAvailableCountriesFallback(@RequestParam String currency){
        var uuid = UUID.randomUUID().toString();
        throw new ExceptionResponseFeing(uuid, "Service unavailable for now ... please retry after 30 seconds");
    }

//    @RequiredArgsConstructor
//    class FeignConfiguration {
//
//        private final CircuitBreakerRegistry registry;
//
//        @Bean
//        public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
//            return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
//                    .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
//                    .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
//        }
//
//
//    }
}
