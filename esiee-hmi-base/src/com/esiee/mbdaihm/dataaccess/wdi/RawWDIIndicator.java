package com.esiee.mbdaihm.dataaccess.wdi;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Raw class used to retrieve information about one indicator from the WDB WDI dedicated file.
 */
public class RawWDIIndicator
{

    @JsonProperty("Series Code")
    public String code;

    @JsonProperty("Topic")
    public String topic;

    @JsonProperty("Indicator Name")
    public String name;

}
