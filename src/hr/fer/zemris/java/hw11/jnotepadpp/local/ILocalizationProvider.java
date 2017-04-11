package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ResourceBundle;

/**
 * Represents the provider which can provide translation for given string key.
 * 
 * Objects which are instances of classes that implement this interface 
 * will be able to give the translations for given keys. 
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public interface ILocalizationProvider {

    /**
     * Adds new listener to the collection of listeners.
     * 
     * @param listener Listener which is going to be stored to the collection.
     */
    void addLocalizationListener(ILocalizationListener listener);
    
    /**
     * Removes the given listener from the collection of listeners.
     * 
     * If such listener does not exist in the collection, does nothing.
     * 
     * @param listener Listener which should be removed from the collection.
     */
    void removeLocalizationListener(ILocalizationListener listener);
    
    /**
     * Gets appropriate translation for the given key string.
     * This method does not know what language is currently set,
     * operates depending on the current {@link ResourceBundle}.
     * 
     * @param string Key used for fetching the translation.
     * @return Appropriate translation for the given key.
     */
    String getString(String string);
}
