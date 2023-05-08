package az.company.mscountry.controller;


import az.company.mscountry.model.CountryResponse;
import az.company.mscountry.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class ConfigController {

    private final CountryService countryService;

    @GetMapping
    public List<CountryResponse> getCountries(@RequestParam String currency){
        return countryService.getAvialableCountries(currency);
    }

    @GetMapping("/test")
    public ResponseEntity<String > getCountries2(){
        System.out.println("success");
        return ResponseEntity.ok("success");
    }


}
