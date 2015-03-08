/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.d4death.dcore;

/**
 * The artifact class.
 * An artifact is a part of a core.
 * It aim to register the plug, wich can be himself for exemple public class Sample extends Artifact implements TickPlug
 * It's quite hard to understand what does it do with this pesky description, but just look some project to understand how it works.
 * (An example paint a thousand words)
 * @author D4Death
 * @param <Game> 
 */
public abstract class Artifact<Game extends Core> {
    
    protected Game theGame;
    
    public Artifact(Game game) {
        theGame = game;
    }
    
    public abstract void register(CentralizedRegisterer registerer) throws Exception ;
}
