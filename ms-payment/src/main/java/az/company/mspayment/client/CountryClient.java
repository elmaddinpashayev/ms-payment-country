package az.company.mspayment.client;

import az.company.mspayment.model.client.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "http://localhost:8585",
        fallback = CountryClientFallback.class
)
public interface CountryClient {
    @GetMapping("/api/countries")
    List<CountryDto> getAllAvailableCountries(@RequestParam String currency);
}
