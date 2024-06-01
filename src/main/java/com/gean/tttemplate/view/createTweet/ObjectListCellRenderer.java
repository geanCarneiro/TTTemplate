/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.view.createTweet;

import java.awt.Component;
import java.util.LinkedHashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author GeanCarneiro
 */
public class ObjectListCellRenderer implements ListCellRenderer<LinkedHashMap>{

    @Override
    public Component getListCellRendererComponent(JList<? extends LinkedHashMap> list, LinkedHashMap value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setText((String) value.get(value.get("listName")));
        return label;
    }
    
}
