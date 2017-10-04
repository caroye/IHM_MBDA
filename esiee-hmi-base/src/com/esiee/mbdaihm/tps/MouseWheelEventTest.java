/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esiee.mbdaihm.tps;

/**
 *
 * @author ELODIECAROY
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MouseWheelEventTest extends JPanel implements MouseWheelListener {

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int notches = e.getWheelRotation();
        if( notches <0){
            //zoom in
            
        }else{
            //zoom out
        }
    }
  
}
