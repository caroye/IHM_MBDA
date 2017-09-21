package com.esiee.mbdaihm.dataaccess.wdi;

import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.indicators.EIndicatorType;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class decoding data contained in the WDI_Series CSV file: data are information about each indicator.
 * <b>File content must be UTF-8 encoded for Jackson MappingIterator.</b>
 */
public class WDIIndicatorsDecoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WDIIndicatorsDecoder.class);

    private static final String FILE_NAME = "WDI_Series-utf8.csv";

    private static final String NO_SUB_TOPIC = "General";

    private static final String EMPTY_TOPIC = "General";

    /**
     * Read the WDI SeriesCheck file to find all indicators it contains.
     *
     * @param directoryPath path containing the WDI SeriesCheck data file
     * @return a list of all indicators
     */
    public static List<Indicator> decode(String directoryPath)
    {
        long in = System.nanoTime();

        // Parse the data file
        List<Indicator> result = new ArrayList<>();

        CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();

        CsvMapper mapper = new CsvMapper();

        File file = new File(directoryPath + "/" + FILE_NAME);

        MappingIterator<RawWDIIndicator> readValues;

        try
        {
            readValues = mapper.readerFor(RawWDIIndicator.class).
                    with(headerSchema).
                    without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).
                    readValues(file);
        }
        catch (IOException ex)
        {
            LOGGER.error("IOException during WDI_Series indicators parsing.", ex);
            return result;
        }

        // Convert from RawWDIIndicator to Indicator
        Indicator indicator;
        String topic;
        String subTopic;

        while (readValues != null && readValues.hasNext())
        {
            RawWDIIndicator indic = readValues.next();

            String[] split = indic.topic.split(":");

            topic = split[0];

            if ("".equals(topic))
            {
                topic = EMPTY_TOPIC;
            }
            subTopic = split.length == 2 ? split[1] : NO_SUB_TOPIC;
            subTopic = subTopic.trim();

            EIndicatorType type;

            if (indic.name.contains("%"))
            {
                type = EIndicatorType.PERCENTAGE;
            }
            else
            {
                type = EIndicatorType.VALUES;
            }

            indicator = new Indicator.Builder()
                    .withTopic(topic)
                    .withSubTopic(subTopic)
                    .withName(indic.name)
                    .withCode(indic.code)
                    .withType(type)
                    .build();

            result.add(indicator);
        }

        long out = System.nanoTime();

        LOGGER.info("Indicators list decoding time = {} ms.", (out - in) / (1000 * 1000));

        return result;
    }

    /**
     * Browse through the given indicators and place them in proper order in data manager.
     *
     * @param input list of the indicators to place in DataManager
     */
    public static void categoriseIndicators(List<Indicator> input)
    {
        // First store the whole list
        DataManager.INSTANCE.setIndicators(input);

        // Then organise in the map
        Map<String, Map<String, List<Indicator>>> indicatorsMap = DataManager.INSTANCE.getIndicatorsMap();

        Map<String, List<Indicator>> topic;

        List<Indicator> subtopic;

        for (Indicator ind : input)
        {
            // Retrieve the map for the topic
            topic = indicatorsMap.get(ind.getTopic());

            // Create if null
            if (topic == null)
            {
                topic = new TreeMap<>();
                indicatorsMap.put(ind.getTopic(), topic);
            }

            // Next retrieve the sub topic list
            subtopic = topic.get(ind.getSubTopic());

            // Create if null
            if (subtopic == null)
            {
                subtopic = new ArrayList<>();
                topic.put(ind.getSubTopic(), subtopic);
            }

            subtopic.add(ind);
        }
    }
}
