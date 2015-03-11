package fr.d4delta.dcore;

import fr.d4delta.dcore.event.Event;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author D4Death
 */
public class EventListener<E extends Event> {
    final public Artifact artifact;
    final private Method method;
    
    public EventListener(Artifact target, Method method) {
        this.artifact = target;
        this.method = method;
    }
    
    public void trigger(E evt) throws Throwable {
        try {
            method.invoke(artifact, evt);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
    
}
