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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ELODIECAROY
 */
public class Map extends JPanel{
    private List<Country> countries;
    private int zoom;
    private int x;
    private int y;
    private int diffX;
    private int diffY;

    public Map() {
        zoom = 1;
        x = 0;
        y = 0;
        diffX=0;
        diffY=0;
        countries = DataManager.INSTANCE.getCountries();
        addMouseWheelListener(new MouseWheelListener(){
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if( notches <0){
                    //zoom in
                    zoom -=notches;
                    if(zoom ==0){
                        zoom+=1;
                    }
                }else{
                    //zoom out
                    zoom -= notches;
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
    
    
    
    public List getCountryList(){
        return countries;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        AffineTransform tr = g2d.getTransform();
        tr.translate(getWidth()/2,getHeight()/2);
        tr.translate(diffX, diffY);
        tr.scale((zoom*getWidth())/(2*180f), (-1*zoom*getHeight())/(2*90f));
        g2d.setTransform(tr);
        
        
        
        for (Country country : countries) {
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
                /*if("FRA".equals(country.getIsoCode())){
                    g2d.setPaint(Color.CYAN);
                    g2d.fill(trait);
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
