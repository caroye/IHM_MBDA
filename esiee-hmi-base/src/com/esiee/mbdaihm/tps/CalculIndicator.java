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
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author ELODIECAROY
 */
public class CalculIndicator {
    private HashMap <Country, Color> indic;
    private String codeIndic;
    private double min;
    private double max;
    private double step;
    private int year;
    
    public CalculIndicator(String code){
        indic = new HashMap<Country, Color>();
        codeIndic = "SP.DYN.LE00.FE.IN";//code
        min = 0;
        max = 0;
        step = 0;
        year = 1995;
        fill();
    }
    
    
    
    public void fill(){
        Indicator toTest = DataManager.INSTANCE.getIndicators().
                    filter(i->i.getCode().equals(codeIndic)).findFirst().get();


        List<RawWDIData> lifeExpectancyWomen
                    = WDIDataDecoder.decode(Launch.WDI_FOLDER,toTest.getCode() );
        DoubleSummaryStatistics stats = lifeExpectancyWomen.stream().
                mapToDouble(rd -> rd.getValueForYear(""+year)).
                filter(d -> !(Double.isNaN(d))).
                summaryStatistics();

        DataManager.INSTANCE.setCurrentIndicator(toTest);
        max = stats.getMax();
        min = stats.getMin();


        step = (max-min)/5;
        
        for (Country country : DataManager.INSTANCE.getCountries()) {
            double val = country.getValueForYear(year);
                if(val>min+4*step){
                    indic.put(country,Color.CYAN);
                }
                if(val<=min+4*step){
                    indic.put(country,Color.RED);
                }if(val<=min+3*step){
                    indic.put(country,Color.GREEN);
                }if(val<=min+2*step){
                    indic.put(country,Color.YELLOW);
                }if(val<=min+step){
                    indic.put(country,Color.PINK);
                }
        }
    }
    
    public HashMap <Country, Color> getColor(){
        return indic;
    }
    
}
