package com.esiee.mbdaihm.exercise;

import com.esiee.mbdaihm.Launch;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class used to check if natural earth file is properly loaded.
 */
public class CountriesCheck
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CountriesCheck.class);

    public static void main(String[] args)
    {
        Launch.initData();

        // Query the world regions
        String regions = DataManager.INSTANCE.getWorldRegions().getRegionValues().stream().
                collect(Collectors.joining(", ", "[ ", " ]"));

        LOGGER.info(regions);

        DataManager.INSTANCE.getWorldRegions().getSubRegionsMap().
            entrySet().stream().
                map(e-> "Region " + e.getKey() +
                 " is composed of " + e.getValue().stream().
                         collect(Collectors.joining(",", "[","]"))).
                forEach(System.out::println);

        // Query the "FRA" country code
        Optional<Country> country = DataManager.INSTANCE.getCountryByCode("FRA");

        LOGGER.info(country.toString());
    }
}
