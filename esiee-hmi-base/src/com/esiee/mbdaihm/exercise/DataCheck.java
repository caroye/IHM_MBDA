package com.esiee.mbdaihm.exercise;

import com.esiee.mbdaihm.Launch;
import com.esiee.mbdaihm.dataaccess.wdi.RawWDIData;
import com.esiee.mbdaihm.dataaccess.wdi.WDIDataDecoder;
import com.esiee.mbdaihm.datamodel.countries.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;

/**
 * Class used to demonstrate how to parse a specific indicator.
 */
public class DataCheck
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataCheck.class);

    public static void main(String[] args)
    {
        Launch.initData();

        LOGGER.info("------------- LIFE EXPECTANCY FEMALE ITALY -----------------------");

        List<RawWDIData> lifeExpectancyWomen
                = WDIDataDecoder.decode(Launch.WDI_FOLDER, "SP.DYN.LE00.FE.IN");

        // Print the life expectancy for 1982
        Optional<Double> opt = lifeExpectancyWomen.stream().
                filter(rd -> rd.countryCode.equals("ITA")).
                map((RawWDIData rd) -> rd.getValueForYear("1982")).
                findFirst();
        opt.ifPresent(d -> LOGGER.info("Women life expectancy at birth in 1982 in Italy = {}", d));

        // Print the life expectancy for all years
        RawWDIData data = lifeExpectancyWomen.stream().
                filter(rd -> rd.countryCode.equals("ITA")).
                findFirst().
                orElseThrow(IllegalStateException::new);

        Country c = new Country.Builder().withName("Italy").build();
        c.setValues(data.getValuesArray());

        for (int i = 1960; i < 2016; i++)
        {
            LOGGER.info("{} -> {}", i, c.getValueForYear(i));
        }
        LOGGER.info("------------------------------------------------------------------");

        LOGGER.info("-------- COUNTRY WITH HIGHEST POPULATION DENSITY 2014 ------------");
        // http://data.worldbank.org/indicator/EN.POP.DNST?locations=MO

        List<RawWDIData> density = WDIDataDecoder.decode(Launch.WDI_FOLDER, "EN.POP.DNST");

        DoubleSummaryStatistics stats = density.stream().
                mapToDouble(rd -> rd.getValueForYear("2014")).
                filter(d -> !(Double.isNaN(d))).
                summaryStatistics();

        LOGGER.info("Max value by summary stats = {}", stats.getMax());

        RawWDIData maxData = density.stream().
                reduce((rw1, rw2) -> rw2.getValueForYear("2014") > rw1.getValueForYear("2014") ? rw2 : rw1).
                orElseThrow(IllegalStateException::new);

        LOGGER.info("Max found at {} for country {}",
                    maxData.getValueForYear(2014), maxData.countryCode);

        LOGGER.info("------------------------------------------------------------------");
    }
}
