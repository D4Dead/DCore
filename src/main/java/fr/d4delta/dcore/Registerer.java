/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d4death.dcore;

/**
 *
 * @author D4Death
 * @param <PlugType> Type of the registerer
 */
public abstract class Registerer<PlugType extends Plug> {
    
    final Class type;
    
    public abstract void register(PlugType e);
    
    public Registerer(Class type) {
        this.type = type;
    }
}
