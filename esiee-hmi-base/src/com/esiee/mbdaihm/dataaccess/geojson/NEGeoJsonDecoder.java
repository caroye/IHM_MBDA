package com.esiee.mbdaihm.dataaccess.geojson;

import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.countries.Geometry;
import com.esiee.mbdaihm.datamodel.countries.Polygon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class dedicated to parse GeoJSON data. Data from Natural Earth : http://www.naturalearthdata.com Conversion to JSON
 * format done online with : http://newconverter.mygeodata.eu
 */
public final class NEGeoJsonDecoder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NEGeoJsonDecoder.class);

    //<editor-fold defaultstate="expanded" desc="Constructor ...">
    /**
     * Utility class empty private constructor.
     */
    private NEGeoJsonDecoder()
    {
    }
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Static Method...">
    /**
     * Parse an input file with Jackson and create a list of RawCountry objects.
     *
     * @param input the JSON file to parse
     * @return the list of parsed data or null if a problem occurred
     */
    public static List<RawCountry> parseFile(File input)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<RawCountry> result = null;

        try
        {
            result = mapper.readValue(input, new TypeReference<List<RawCountry>>()
                              {
                              });
        }
        catch (IOException e)
        {
            LOGGER.error("IOException when parsing file " + input.getAbsolutePath(), e);
        }

        return result;
    }

    /**
     * Convert Jackson library decoded data (RawCountry) into the application data model (Country).
     *
     * @param in the list of RawCountry decoded instances
     * @return a list of converted Country instances
     */
    public static List<Country> convert(List<RawCountry> in)
    {
        return in.stream()
                // .filter(c -> c.properties.scalerank < 10) Not very useful considering data
                .map(c ->
                {
                    Country country = new Country.Builder()
                            .withName(c.properties.admin)
                            .withIsoCode(c.properties.iso_a3)
                            .withRegion(c.properties.region_wb)
                            .withSubRegion(c.properties.subregion)
                            .build();

                    if (c.geometry.type.equals("Polygon"))
                    {
                        List<Object> jsonPoly = c.geometry.coordinates.get(0); // 0 is the base polygon; others are holes
                        Polygon poly = new Polygon(jsonPoly);

                        Geometry geo = new Geometry();
                        geo.addPolygon(poly);
                        country.setGeometry(geo);
                    }
                    else // Multi polygons
                    {
                        int count = c.geometry.coordinates.size();
                        Geometry geo = new Geometry();

                        for (int i = 0; i < count; i++)
                        {
                            List<Object> jsonPoly = (List<Object>) c.geometry.coordinates.get(i).get(0); // 0 is the base polygon; others are holes

                            Polygon poly = new Polygon(jsonPoly);
                            geo.addPolygon(poly);
                        }

                        country.setGeometry(geo);
                    }

                    return country;
                }).collect(Collectors.toList());
    }
    //</editor-fold>
}
