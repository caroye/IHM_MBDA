package com.esiee.mbdaihm.dataaccess.geojson;

/**
 * Sample model for Jackson library parsing, inner level for countries properties.
 */
public class RawProperties
{
    //<editor-fold defaultstate="expanded" desc="Type constants...">
    public static final String TYPE_DEPENDENCY = "Dependency";

    public static final String TYPE_INDETERMINATE = "Indeterminate";

    public static final String TYPE_COUNTRY = "Country";

    public static final String TYPE_SOVEREIGN_COUNTRY = "Sovereign country";
    //</editor-fold>

    //<editor-fold defaultstate="expanded" desc="Attributes...">
    public String type;

    public int scalerank;

    public String admin;

    public String sovereignt;

    public String income_grp;

    public String region_wb;

    public String subregion;

    public String iso_a3;
    //</editor-fold>

}
