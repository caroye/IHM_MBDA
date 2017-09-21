package com.esiee.mbdaihm;

import com.esiee.mbdaihm.dataaccess.geojson.NEGeoJsonDecoder;
import com.esiee.mbdaihm.dataaccess.geojson.RawCountry;
import com.esiee.mbdaihm.dataaccess.wdi.WDIIndicatorsDecoder;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import com.esiee.mbdaihm.tps.MouseWheelEventTest;
import com.esiee.mbdaihm.tps.ProjetCaroyBordier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Application entry point.
 */
public class Launch
{
    public static final String WDI_FOLDER = "./data/WDI";

    private static final String COUNTRIES_FILE = "./data/ne_50m_admin_0_countries.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(Launch.class);

    private static void populateCountries()
    {
        // Parse the countries data file
        List<RawCountry> rawData = NEGeoJsonDecoder.parseFile(new File(COUNTRIES_FILE));
        LOGGER.info("Parsed {} countries.", rawData.size());

        // Convert into our data model
        List<Country> countries = NEGeoJsonDecoder.convert(rawData);
        LOGGER.info("Converted {} countries.", countries.size());

        // Store in the DataManager
        DataManager.INSTANCE.setCountries(countries);

        // Print the list of countries for debug
        countries.forEach((Country country) ->
        {
            // Change the log level to info to display all countries with default log config
            LOGGER.debug("Country : {} - code : {} ; {} polygons.",
                         country.getName(),
                         country.getIsoCode(),
                         country.getGeometry().getPolygons().size());
        });
    }

    private static void populatesIndicators()
    {
        // Decode the indicators files
        List<Indicator> indicators = WDIIndicatorsDecoder.decode(WDI_FOLDER);
        LOGGER.info("Parsed {} indicators in WDI series.", indicators.size());

        // Categorise and store in the DataManager
        WDIIndicatorsDecoder.categoriseIndicators(indicators);
    }

    /**
     * Decode and store countries and indicators in the {@link DataManager}.
     */
    public static void initData()
    {
        // Log the working directory to help debug...
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        LOGGER.info("Started application, working dir: {} ", s);

        populateCountries();
        populatesIndicators();
    }

    /**
     * Application entry point.
     *
     * @param args no parameter used
     */
    public static void main(String[] args)
    {
        initData();
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProjetCaroyBordier tp = new ProjetCaroyBordier();
                tp.LayountComponents();
                tp.setVisible(true);
                MouseWheelEventTest test = new MouseWheelEventTest();
            }
        });

        // Instanciate and display the view here
    }
}
