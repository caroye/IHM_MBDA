package com.esiee.mbdaihm.datamodel.countries;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model class for countries.
 */
@JsonIgnoreProperties(
        {
            "geometry" // Don't serialize geometry for REST API
        })
public class Country
{
    //<editor-fold defaultstate="expanded" desc="Attributes ...">
    private String name;

    private String region;

    private String subRegion;

    private String isoCode;

    private Geometry geometry;

    // Contains values between years 1960 and 2015 included
    private double[] values;
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Methods ...">
    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry(Geometry geometry)
    {
        this.geometry = geometry;
    }

    public String getName()
    {
        return name;
    }

    public String getRegion()
    {
        return region;
    }

    public String getIsoCode()
    {
        //System.out.println(isoCode);
        return isoCode;
    }

    public String getSubRegion()
    {
        return subRegion;
    }

    /**
     *
     * @return array of values, one year per index starting at year 1960
     */
    public double[] getValues()
    {
        return values;
    }

    public void setValues(double[] values)
    {
        this.values = values;
    }

    /**
     * Retrieve the value of stored indicator for given year.
     *
     * @param year The year of the value to retrieve
     * @return indicator value for given year or Double.NaN if no value present
     */
    public double getValueForYear(int year)
    {
        if (year < 1960 || year > 2015)
        {
            throw new IllegalArgumentException("Year must be between 1960 and 2015");
        }

        if (values == null)
        {
            return Double.NaN;
        }

        return values[year - 1960];
    }

    @Override
    public String toString()
    {
        return "Country{"
                + "name='" + name + '\''
                + ", region='" + region + '\''
                + ", subRegion='" + subRegion + '\''
                + ", isoCode='" + isoCode + '\''
                + ", polygons='" + geometry.getPolygons().size() + '\''
                + '}';
    }
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Builder ...">
    public static final class Builder
    {
        private String name;

        private String region;

        private String subRegion;

        private String isoCode;

        private Geometry geometry;

        public Builder()
        {
        }

        public Builder withName(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder withRegion(final String region)
        {
            this.region = region;
            return this;
        }

        public Builder withSubRegion(final String subRegion)
        {
            this.subRegion = subRegion;
            return this;
        }

        public Builder withIsoCode(final String isoCode)
        {
            this.isoCode = isoCode;
            return this;
        }

        public Builder withGeometry(final Geometry geometry)
        {
            this.geometry = geometry;
            return this;
        }

        public Country build()
        {
            final Country result = new Country();
            result.name = name;
            result.region = region;
            result.subRegion = subRegion;
            result.isoCode = isoCode;
            result.geometry = geometry;
            return result;
        }

        public Builder mergeFrom(final Country src)
        {
            this.name = src.name;
            this.region = src.region;
            this.subRegion = src.subRegion;
            this.isoCode = src.isoCode;
            this.geometry = src.geometry;
            return this;
        }
    }
    //</editor-fold>
}
