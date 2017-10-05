package com.esiee.mbdaihm.datamodel;

import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.countries.WorldRegions;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton, following enum pattern, used to easily store and retrieve data.
 */
public enum DataManager
{
    /**
     * Unique instance.
     */
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    //<editor-fold defaultstate="expanded" desc="Attributes ...">
    private List<Country> countries = Collections.EMPTY_LIST;

    private Map<String, Country> countriesMap = Collections.emptyMap();

    private WorldRegions regions;

    // Topic, subtopic and indicators.
    private final Map<String, Map<String, List<Indicator>>> indicatorsMap = new TreeMap<>();

    private List<Indicator> indicators;

    //@TODO : To remove depending on choices made for the application
    private Indicator currentIndicator;
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Methods ...">
    /**
     * Retrieve the list of countries.
     *
     * @return a list of all countries
     */
    public List<Country> getCountries()
    {
        return countries;
    }

    /**
     * Retrieve the regions of the world.
     *
     * @return the regions of the world
     */
    public WorldRegions getWorldRegions()
    {
        return regions;
    }

    /**
     * Store the list of countries in the DataManager instance. The mapping between valid country code and Country is
     * also created.
     *
     * @param countries the countries to store
     */
    public void setCountries(List<Country> countries)
    {
        countriesMap = countries.stream().
                filter(c -> !c.getIsoCode().equals("-99")).
                collect(Collectors.toMap(Country::getIsoCode, Function.identity()));

        LOGGER.info("Stored {} countries with valid code ( != -99) ", countriesMap.size());

        this.countries = countries;
        regions = new WorldRegions(countries);
    }

    /**
     * Retrieve the mapping of indicators split in topics and subtopics. First key is the topic, it maps to a map with
     * subtopic as a key and a list of indicators for this topic & subtopic
     *
     * @return the indicators mapping
     */
    public Map<String, Map<String, List<Indicator>>> getIndicatorsMap()
    {
        return indicatorsMap;
    }

    public Optional<Country> getCountryByCode(String code)
    {
        return Optional.ofNullable(countriesMap.get(code));
    }

    public Indicator getCurrentIndicator()
    {
        return currentIndicator;
    }

    public void setCurrentIndicator(Indicator currentIndicator)
    {
        this.currentIndicator = currentIndicator;
    }

    public void setIndicators(List<Indicator> indicators)
    {
        this.indicators = indicators;
    }

    public Stream<Indicator> getIndicators()
    {
        return indicators.stream();
    }
    //</editor-fold>
}
