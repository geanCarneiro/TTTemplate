/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.utils;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author GeanCarneiro
 */
public class SingleWindowController {
    
    private HashMap<String, Window> openedWindows = new HashMap<>();
    
    public Window openWindow(Window newWindow) {
        Window window = this.openedWindows.get(newWindow.getClass().getName());
        
        if(window == null) {
            this.openedWindows.put(newWindow.getClass().getName(), newWindow);
            window = newWindow;
        }
        
        window.setVisible(true);
        
        window.addWindowListener(new CommonWindowAdapter(this));
        return window;
    }
    
    public void closeWindow(Window window) {
        this.openedWindows.remove(window.getClass().getName());
        window.dispose();
    }
    
    public Collection<Window> getAllOpenedWindow(){
        return this.openedWindows.values();
    }
    
}
class CommonWindowAdapter extends WindowAdapter {
    
    private SingleWindowController controller;
    
    public CommonWindowAdapter(SingleWindowController controller) {
        this.controller = controller;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        this.controller.closeWindow(e.getWindow());
    }
    
    
}
