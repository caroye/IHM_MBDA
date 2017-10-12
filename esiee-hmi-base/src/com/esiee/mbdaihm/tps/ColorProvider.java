/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esiee.mbdaihm.tps;

import com.esiee.mbdaihm.Launch;
import com.esiee.mbdaihm.dataaccess.wdi.RawWDIData;
import com.esiee.mbdaihm.dataaccess.wdi.WDIDataDecoder;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import java.awt.Color;
import java.awt.Paint;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 *
 * @author ELODIECAROY
 */
class ColorProvider {

    private Indicator lastComputedIndicator = null;
    
    public ColorProvider(Indicator indic){
        lastComputedIndicator = indic;
    }
    static Paint getColorForCountry(Country country, String codeIndic, int year) {
        Indicator toTest = DataManager.INSTANCE.getIndicators().
                    filter(i->i.getCode().equals(codeIndic)).findFirst().get();


        List<RawWDIData> myIndicatorToMap
                    = WDIDataDecoder.decode(Launch.WDI_FOLDER,toTest.getCode() );
        DoubleSummaryStatistics stats = myIndicatorToMap.stream().
                mapToDouble(rd -> rd.getValueForYear(""+year)).
                filter(d -> !(Double.isNaN(d))).
                summaryStatistics();

        DataManager.INSTANCE.setCurrentIndicator(toTest);
        double max = stats.getMax();
        double min = stats.getMin();
        double step = (max-min)/5;
       //if(DataManager.INSTANCE.getCurrentIndicator() == lastComputedIndicator){
       //DataManager.INSTANCE.getCurrentIndicator()
       double val = country.getValueForYear(year);
                if(val>min+4*step){
                    return Color.CYAN;
                }
                if(val<=min+4*step){
                    return Color.RED;
                }if(val<=min+3*step){
                    return Color.GREEN;
                }if(val<=min+2*step){
                    return Color.YELLOW;
                }if(val<=min+step){
                    return Color.PINK;
                }
       return Color.GRAY;
    }
    
}
