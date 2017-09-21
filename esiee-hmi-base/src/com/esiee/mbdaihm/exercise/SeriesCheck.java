package com.esiee.mbdaihm.exercise;

import com.esiee.mbdaihm.Launch;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;

/**
 * Class used to check if WDI Data SeriesCheck is properly loaded.
 */
public class SeriesCheck
{

    public static void main(String[] args)
    {
        Launch.initData();

        // Dump all indicators name containing "agriculture"
        DataManager.INSTANCE.getIndicators().
                map(Indicator::getName).
                filter(name -> name.toLowerCase().contains("agriculture")).
                forEach(System.out::println);
    }
}
