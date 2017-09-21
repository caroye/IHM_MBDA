// DRAWING PANEL !!!!!!!!!!


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esiee.mbdaihm.tps;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import com.esiee.mbdaihm.datamodel.countries.Polygon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ELODIECAROY
 */
public class Map extends JPanel{
    private List<Country> countries;
    
    public void getCountryList(){
        countries = DataManager.INSTANCE.getCountries();
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform tr = g2d.getTransform();
        tr.translate(getWidth()/2, getHeight()/2);
        tr.scale(getWidth()/(2*180f), -1*getHeight()/(2*90f));
        g2d.setTransform(tr);
        
        getCountryList();///*
        
        for (Country country : countries) {
            List<Polygon> poly = country.getGeometry().getPolygons();
            for (Polygon polygon : poly) {
                int i =0;
                GeneralPath trait = new GeneralPath();
                trait.moveTo(polygon.points[i].lon, polygon.points[i].lat);
                for(i=1;i<polygon.points.length;i++){
                    trait.lineTo(polygon.points[i].lon, polygon.points[i].lat);
                }
                trait.closePath();
                g2d.setPaint(Color.BLACK);
                g2d.draw(trait);
                if("FRA".equals(country.getIsoCode())){
                    g2d.setPaint(Color.CYAN);
                    g2d.fill(trait);
                }
            }      
        }
        
        
        /*g2d.setPaint(Color.BLACK);
        GeneralPath triangle = new GeneralPath();
        triangle.moveTo(200,200);
        triangle.lineTo(100, 300);
        triangle.lineTo(300, 300);
        triangle.closePath();
        g2d.draw(triangle);*/
    }
}
