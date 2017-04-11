package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the class which implements {@link ILocalizationProvider} 
 * and adds it the ability to register, de-register and inform 
 * currently stored listeners.
 *  
 * Does not know anything about fetching translations, just keeps the 
 * appropriate listeners.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * Represents the listeners of the localization provider.
     */
    private List<ILocalizationListener> listeners;

    /** 
     * Public constructor.
     * Initializes the list of listeners.
     */
    public AbstractLocalizationProvider() {
        this.listeners = new ArrayList<ILocalizationListener>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Informs all currently stored listeners by 
     * calling the appropriate method for every listener.
     */
    public void fire() {
        listeners.forEach(l -> l.localizationChanged());
    }

}
