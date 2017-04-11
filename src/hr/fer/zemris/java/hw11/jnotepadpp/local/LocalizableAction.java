package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Represents the {@link Action} which can be localizated.
 * This means that this action will display appropriate representing string values
 * for the selected language.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public abstract class LocalizableAction extends AbstractAction{

    /**
     * Represents the serial Version UID of the localizable action.
     */
    private static final long serialVersionUID = 1L;

    /**
    * Represents the key used for fetching the translation.
    */
    protected String key;
    
    /**
     * Represents the localization provider used for fetching translations.
     */
    protected ILocalizationProvider provider;
    
    /**
     * Represents the listener of this action.
     * This listener will change the action name (the only string which
     * should be translated) to the new value.
     */
    protected ILocalizationListener listener = 
	  () -> putValue(Action.NAME, provider.getString(key));
    
    /** 
     * Public constructor. Sets fields to the given values. 
     * Adds appropriate listener to the given localization provider.
     *
     * @param key Key used for translation.
     * @param provider Localization provider.
     */
    public LocalizableAction(String key, ILocalizationProvider provider) {

        this.key = key;
        this.provider = provider;
        
        putValue(Action.NAME, this.provider.getString(key));
        this.provider.addLocalizationListener(listener);
    }


    

}
