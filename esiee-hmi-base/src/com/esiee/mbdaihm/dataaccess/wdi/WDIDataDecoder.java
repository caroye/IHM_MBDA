package com.esiee.mbdaihm.dataaccess.wdi;

import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jackson based decoder class used to parse data for a specific indicator.
 * <b>CAUTION: Data file name is hard coded to: WDI_Data-utf8.csv</b>
 */
public class WDIDataDecoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WDIDataDecoder.class);

    private static final String FILE_NAME = "WDI_Data-utf8.csv";

    /**
     * Retrieve the data from WDI_Data-utf8.csv file for the given indicator code.
     *
     * @param directoryPath directory where to find WDI_Data-utf8.csv
     * @param indicatorCode the indicator code to parse, e.g. AG.AGR.TRAC.NO
     * @return a list of RawWDIData instances
     */
    public static List<RawWDIData> decode(String directoryPath, String indicatorCode)
    {
        LOGGER.info("Start decoding: " + indicatorCode);

        long in = System.nanoTime();

        int inCount = 0;
        int countryNoMatchCount = 0;
        int noDataCount = 0;
        int dataSetCount = 0;

        List<RawWDIData> result = new ArrayList<>();

        CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();

        CsvMapper mapper = new CsvMapper();

        File file = new File(directoryPath + "/" + FILE_NAME);

        MappingIterator<RawWDIData> readValues;

        try
        {
            readValues = mapper.readerFor(RawWDIData.class).
                    with(headerSchema).
                    without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
                    readValues(file);
        }
        catch (IOException ex)
        {
            LOGGER.error("Exception decoding indicator: " + indicatorCode, ex);
            return result;
        }

        while (readValues != null && readValues.hasNext())
        {
            RawWDIData data = readValues.next();

            if (data.indicatorCode.equals(indicatorCode))
            {
                inCount++;

                // Safety check if we have the country code in our data
                Optional<Country> country
                        = DataManager.INSTANCE.getCountryByCode(data.countryCode);

                if (!country.isPresent())
                {
                    countryNoMatchCount++;
                    continue;
                }

                country.get().setValues(data.getValuesArray());

                dataSetCount++;
                result.add(data);
            }

        }

        long out = System.nanoTime();

        LOGGER.info("Data decoding time = {} ms.", (out - in) / (1000 * 1000));
        LOGGER.info("#### Decoded data stats ####");
        LOGGER.info("\t- input countries count = {}", inCount);
        LOGGER.info("\t- discarded non matching countries count = {}", countryNoMatchCount);
        LOGGER.info("\t- countries with no data count = {}", noDataCount);
        LOGGER.info("\t- final data set count = {}", dataSetCount);
        LOGGER.info("########");

        return result;
    }
}
