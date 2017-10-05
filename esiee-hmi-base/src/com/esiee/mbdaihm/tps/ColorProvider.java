/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esiee.mbdaihm.tps;

import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import java.awt.Color;
import java.awt.Paint;

/**
 *
 * @author ELODIECAROY
 */
class ColorProvider {

    private Indicator lastComputedIndicator = null;
    
    static Paint getColorForCountry(Country country) {
        
       boolean same = DataManager.INSTANCE.getCurrentIndicator() == lastComputedIndicator;
       return Color.GRAY;
    }
    
}
