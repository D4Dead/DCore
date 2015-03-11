package fr.d4delta.dcore;

/**
 * @author D4Death
 * @param <Application> 
 */
public abstract class Artifact<Application extends Core> {
    
    protected Application application;
    
    public Artifact(Application game) {
        application = game;
    }
    
}
