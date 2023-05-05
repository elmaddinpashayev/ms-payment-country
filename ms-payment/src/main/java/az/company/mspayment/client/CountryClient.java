package az.company.mspayment.client;

import az.company.mspayment.model.client.CountryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-country", url = "${client.ms-country.endpoint}",
        fallback = CountryClientFallback.class
)
public interface CountryClient {
    @GetMapping("/api/countries")
    List<CountryDto> getAllAvailableCountries(@RequestParam String currency);
}
