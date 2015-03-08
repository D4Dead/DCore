/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d4death.dcore;

import fr.d4death.dcore.list.ThreeSectionsPlugList;
import fr.d4delta.dexceptionutil.ExceptionManager;
import fr.d4delta.dexceptionutil.ExceptionHandler;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 *
 * @author D4Death
 */
public abstract class Core extends Thread {
    
    private ExceptionHandler exHandler = null;
    
    public Core(String appName) {
        exHandler = new ExceptionManager(appName);
    }
    
    public Core(ExceptionHandler exceptionHandler) {
        exHandler = exceptionHandler;
    }
    
    @Override
    public void run() {
        try {
            letUsBegin();
        } catch(Exception e) {
            exHandler.handle(e);
        }
    }
    
    //Atomic for multi thread handlings
    private final AtomicBoolean shouldDie = new AtomicBoolean(false);
    
    public synchronized void finish() {
        shouldDie.set(true);
    }
    
    private final CentralizedRegisterer centralizedRegisterer = new CentralizedRegisterer();
    
    public abstract void loadArtifacts(CentralizedRegisterer centralizedRegisterer) throws Exception ;
    
    public final ThreeSectionsPlugList<InitPlug> initPlugs = new ThreeSectionsPlugList<>(InitPlug.class);
    public final ThreeSectionsPlugList<TickPlug> tickPlugs = new ThreeSectionsPlugList<>(TickPlug.class);
    public final ThreeSectionsPlugList<EndPlug> endPlugs = new ThreeSectionsPlugList<>(EndPlug.class);
    
    private void letUsBegin() throws Exception {
        
        centralizedRegisterer.addRegisterer(initPlugs);
        centralizedRegisterer.addRegisterer(tickPlugs);
        centralizedRegisterer.addRegisterer(endPlugs);
        
        loadArtifacts(centralizedRegisterer);
        
        for(InitPlug i: initPlugs) {
            i.onInit();
        }
        
        Iterator<TickPlug> tickIterator = tickPlugs.getCircularIterator();
        while(!shouldDie.get() && tickIterator.hasNext()) {
            tickIterator.next().tick();
        }
        
        for(EndPlug e: endPlugs) {
            e.onEnd();
        }
    }
    
    

}
