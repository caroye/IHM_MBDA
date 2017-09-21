/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esiee.mbdaihm.tps;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author ELODIECAROY
 */
public class ArrayListListModel extends AbstractListModel<String>{
    private final ArrayList<String> content = new ArrayList<>();
    
    public void add(String value){
        content.add(value);
        fireContentsChanged(value, 0, 0);
    }
    @Override
    public int getSize() {
        return content.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getElementAt(int index) {
        return content.get(index);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
