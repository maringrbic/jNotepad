package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Represents the listener used for operating
 * with the localization provider which is a subject
 * in the observer pattern.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public interface ILocalizationListener {

    /**
     * Method called when the localization language was changed.
     * It's task is to reproduce the translation to the GUI.
     */
    void localizationChanged();
}
