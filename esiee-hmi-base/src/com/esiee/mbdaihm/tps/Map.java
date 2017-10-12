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
import com.esiee.mbdaihm.datamodel.indicators.Indicator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ELODIECAROY
 */
public class Map extends JPanel{

    private float zoom;
    private int x;
    private int y;
    private int diffX;
    private int diffY;
    private double max;
    private double min;
    private double step;
    private int year;
    private CalculIndicator myColor;

    public Map() {
            zoom = 1.0f;
            x = 0;
            y = 0;
            diffX=0;
            diffY=0;
            year = 1995;
            min = 0;
            step = 0;
            //myColor = new  CalculIndicator("ceci est un code");
            //indic = WDIIndicatorsDecoder.decode(Launch.WDI_FOLDER);
            /*
            Indicator toTest = DataManager.INSTANCE.getIndicators().
                    filter(i->i.getCode().equals("SP.DYN.LE00.FE.IN")).findFirst().get();


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
            */
            
            addMouseWheelListener(new MouseWheelListener(){
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int notches = e.getWheelRotation();
                    if( notches <0){
                        //zoom in
                        zoom = zoom*1.1f;
                        if(zoom ==0){
                            zoom+=1;
                        }
                    }else{
                        //zoom out
                        zoom = zoom/1.1f;
                        if(zoom ==0){
                            zoom+=1;
                        }
                    }
                    Map.this.repaint();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            });
            addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            addMouseMotionListener(new MouseMotionListener(){
                @Override
                public void mouseDragged(MouseEvent e) {
                    int eX = e.getX();
                    int eY = e.getY();
                    diffX += eX-x;
                    diffY += eY-y;
                    x= e.getX();
                    y=e.getY();
                    Map.this.repaint();
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            });
        
    }
    
       
    
    @Override
    protected void paintComponent(Graphics g){
        
        if(true);
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform tr = g2d.getTransform();
        tr.translate(getWidth()/2,getHeight()/2);
        tr.translate(diffX, diffY);
        tr.scale((zoom*getWidth())/(2*180f), (-1*zoom*getHeight())/(2*90f));
        g2d.setTransform(tr);
        g2d.setStroke(new BasicStroke(1.0f/zoom));
        
        for (Country country : DataManager.INSTANCE.getCountries()) {
           // System.out.println(country.getName());
            List<Polygon> poly = country.getGeometry().getPolygons();
            for (Polygon polygon : poly) {
                int i =0;
                GeneralPath path = new GeneralPath();
                path.moveTo(polygon.points[i].lon, polygon.points[i].lat);
                for(i=1;i<polygon.points.length;i++){
                    path.lineTo(polygon.points[i].lon, polygon.points[i].lat);
                }
                path.closePath();
                g2d.setPaint(Color.BLACK);
                g2d.draw(path);
                //g2d.setPaint(ColorProvider.getColorForCountry(country,"SP.DYN.LE00.FE.IN",1995));
                //g2d.setPaint(Color.GRAY);
                g2d.setPaint(ColorProvider.getColorForCountry(country,"SP.DYN.LE00.FE.IN",year));
                g2d.fill(path);
                /*double val = country.getValueForYear(year);
                if(val>min+4*step){
                    g2d.setPaint(Color.CYAN);
                    g2d.fill(path);
                }
                if(val<=min+4*step){
                    g2d.setPaint(Color.RED);
                    g2d.fill(path);
                }if(val<=min+3*step){
                    g2d.setPaint(Color.GREEN);
                    g2d.fill(path);
                }if(val<=min+2*step){
                    g2d.setPaint(Color.YELLOW);
                    g2d.fill(path);
                }if(val<=min+step){
                    g2d.setPaint(Color.PINK);
                    g2d.fill(path);
                }
                
                /*if("FRA".equals(country.getIsoCode())){
                    g2d.setPaint(Color.CYAN);
                    g2d.fill(path);
                }*/
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
