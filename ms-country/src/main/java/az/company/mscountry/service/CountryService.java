package az.company.mscountry.service;


import az.company.mscountry.model.CountryResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryService {
    public List<CountryResponse> getAvialableCountries(String curreny) {
        if (curreny.equals("USD")) {
            return List.of(new CountryResponse("USA", BigDecimal.valueOf(10000)),
                    new CountryResponse("BRAZIL", BigDecimal.valueOf(1000)));
        }
        return new ArrayList<>();
    }

}
