package az.company.mspayment.client;

import az.company.mspayment.exception.ExceptionResponseFeing;
import az.company.mspayment.model.client.CountryDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
public class CountryClientFallback implements CountryClient {
    private final Exception exception;

    @Override
    @CircuitBreaker(name = "getAvailablityBreaker")
    public List<CountryDto> getAllAvailableCountries(String currency) {
        log.info("This Method runned");
        if (exception instanceof TimeoutException) {
            return Collections.emptyList();
        }
        var uuid = UUID.randomUUID().toString();
        throw new ExceptionResponseFeing(uuid, "Service unavailable for now ... please retry after 30 seconds");
    }
}
