package fr.d4delta.dcore;

import java.util.HashMap;

/**
 *
 * @author D4Death
 */
public abstract class Registerer<E extends EventListener> {
    
    final Class type;
    
    public abstract void register(E e, HashMap<String, String> options);
    
    public Registerer(Class type) {
        this.type = type;
    }
}
