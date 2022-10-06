package se.experis.boxinator.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import se.experis.boxinator.models.dbo.Country;
import se.experis.boxinator.models.dto.JsonCountry;
import se.experis.boxinator.repositories.CountryRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseSeedingService {

    @Value("${spring.sql.init.mode}")
    private String initMode;

    private final ObjectMapper objectMapper;
    private final CountryRepository countryRepository;


    public DatabaseSeedingService(ObjectMapper objectMapper, CountryRepository countryRepository) {
        this.objectMapper = objectMapper;
        this.countryRepository = countryRepository;
    }

    @EventListener
    public void setupSeedData(ContextRefreshedEvent ctxRefreshed) {
        if(initMode.equals("always")){
            this.process();
        }
    }

    public void process() {
        TypeReference<List<JsonCountry>> typeReference = new TypeReference<List<JsonCountry>>() {};
        try(InputStream is = TypeReference.class.getResourceAsStream("/data/countries.json")) {
            // map json objects onto java objects
            List<JsonCountry> countries = objectMapper.readValue(is, typeReference);
            // find norway for distance reference
            List<JsonCountry> norways = countries.stream().filter(country -> country.name.equals("Norway")).collect(Collectors.toList());
            JsonCountry norway = norways.get(0);
            // convert to country and calculate multiplier based on country
            List<Country> dbCountries = countries.stream().map(country -> {
                Double dx = norway.latitude - country.latitude;
                Double dy = norway.longitude - country.longitude;
                // calculate distance
                Double dist = Math.sqrt( (dx*dx) + (dy*dy) );

                // map onto country
                Country dbCountry = new Country();
                dbCountry.setName(country.name);
                dbCountry.setShorthand(country.iso3);
                // if dist is 6 or less (aka sweden, denmark norway) set to 0
                if( dist <= 6) {
                    dist = 0.0;
                }
                dbCountry.setMultiplier((int) (dist / 2));

                return dbCountry;
            }).collect(Collectors.toList());

            //save all countries
            countryRepository.saveAll(dbCountries);
            System.out.printf("%s: iso3: %s, position %s, %s%n", norway.name, norway.iso3, norway.latitude, norway.longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
