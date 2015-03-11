package fr.d4delta.dcore;

import fr.d4delta.dcore.event.EndEvent;
import fr.d4delta.dcore.event.InitEvent;
import fr.d4delta.dcore.event.TickEvent;
import fr.d4delta.dcore.list.ThreeSectionsPlugList;
import fr.d4delta.dexceptionutil.ExceptionManager;
import fr.d4delta.dexceptionutil.ExceptionHandler;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;



/**
 *
 * @author D4Delta
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
    
    public final ThreeSectionsPlugList<EventListener<InitEvent>> initEvents = new ThreeSectionsPlugList<>(InitEvent.class);
    public final ThreeSectionsPlugList<EventListener<TickEvent>> tickEvent = new ThreeSectionsPlugList<>(TickEvent.class);
    public final ThreeSectionsPlugList<EventListener<EndEvent>> endEvents = new ThreeSectionsPlugList<>(EndEvent.class);
    
    private void letUsBegin() throws Exception {
        
        centralizedRegisterer.addRegisterer(initEvents);
        centralizedRegisterer.addRegisterer(tickEvent);
        centralizedRegisterer.addRegisterer(endEvents);
        
        loadArtifacts(centralizedRegisterer);
        initEvents.triggerEvent(new InitEvent());
        
        Iterator<EventListener<TickEvent>> tickIterator = tickEvent.getCircularIterator();
        while(!shouldDie.get() && tickIterator.hasNext()) {
            tickIterator.next().trigger(new TickEvent());
        }
        
        endEvents.triggerEvent(new EndEvent());
    
    }
    
    

}
