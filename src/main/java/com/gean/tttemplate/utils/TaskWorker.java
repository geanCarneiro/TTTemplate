/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author GeanCarneiro
 */
public class TaskWorker extends SwingWorker<Void, Void>{

    private Callback doLimpaWorker;
    private Callback task;
    
    public TaskWorker(Callback doLimpaWorker, Callback task) {
        this.doLimpaWorker = doLimpaWorker;
        this.task = task;
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        task.call();
        return null;
    }

    @Override
    protected void done() {
        super.done(); //To change body of generated methods, choose Tools | Templates.
        try {
            get();
            this.doLimpaWorker.call();
        } catch (Exception ex) { ex.printStackTrace();}
    }
    
}
