package fr.d4delta.dcore;

import fr.d4delta.dcore.event.EventOption;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author D4Death
 */
public class CentralizedRegisterer {
    
    private final HashMap<Class, Registerer> registererHashmap = new HashMap<>();
    
    public void registerEventListener(Artifact a) {
        Method[] mets = a.getClass().getMethods();
        
        for(Method m : mets) {
            Class[] parameters = m.getParameterTypes();
            Registerer registerer;
            if(parameters.length == 1 && (registerer = registererHashmap.get(parameters[0])) != null)  {
                Annotation[] anots = m.getDeclaredAnnotations();
                HashMap<String, String> optionHashmap = new HashMap<>();
                for(Annotation an: anots) {
                    if(an instanceof EventOption) {
                        EventOption optionAnnotation = (EventOption) an;
                        String[] params = optionAnnotation.options();
                        for(String s: params) {
                            String[] keyAndVal = s.split("=");
                            optionHashmap.put(keyAndVal[0], keyAndVal[1]);
                        }
                    }
                }
                registerer.register(new EventListener(a, m), optionHashmap);
            }
        }
    }
    
    public void registerEventListener(Artifact... as) {
        for(Artifact a: as) {
            registerEventListener(a);
        }
    }
    
    public void addRegisterer(Registerer registerer) {
        registererHashmap.put(registerer.type, registerer);
    }
    
    public Registerer getRegistererFor(Class plugType) {
        return registererHashmap.get(plugType);
    }
}
