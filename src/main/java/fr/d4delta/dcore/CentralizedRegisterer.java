/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d4delta.dcore;

import java.util.HashMap;

/**
 *
 * @author D4Death
 */
public class CentralizedRegisterer {
    
    private final HashMap<Class, Registerer> registererHashmap = new HashMap<>();
    
    public void registerPlug(Plug p) {
        Class[] interfaces = p.getClass().getInterfaces();
        
        for(int i = 0; i < interfaces.length; i++) {
            tryToRegister(interfaces[i], p);
        }
        
        tryToRegister(p.getClass().getEnclosingClass(), p);
    }
    
    private void tryToRegister(Class clazz, Plug p) {
        if(clazz != null && !clazz.equals(Plug.class)) {
            try {
                clazz.asSubclass(Plug.class);
                Registerer potentialRegisterer = getRegistererFor(clazz);
                
                if(potentialRegisterer != null) {
                    potentialRegisterer.register(p);
                }
                    
            } catch(ClassCastException e) {}
        }
    }
    
    public void addRegisterer(Registerer registerer) {
        registererHashmap.put(registerer.type, registerer);
    }
    
    public Registerer getRegistererFor(Class plugType) {
        return registererHashmap.get(plugType);
    }
}
