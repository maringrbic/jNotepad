package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.JMenu;

/**
 * Represents the {@link JMenu} which can be lcoalizated.
 * 
 * This means the the {@link JMenu} can be translated to all
 * currently provided languages.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class LocalizableJMenu extends JMenu {

    /**
   /**
     * Represents the serial Version UID of the menu.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the key used for localization.
     */
    protected String key;

    /**
     * Represents the provider of the localization.
     */
    protected ILocalizationProvider provider;

    /**
     * Represents the listener of this menu.
     * Sets text of menu to the one fetched from the provider.
     */
    protected ILocalizationListener listener = 
	  () -> setText(provider.getString(key));

    /** 
      * Public constructor.
      * Sets fields to the given values and sets the default text.
      * Registers itself to the localization provider as a listener.
      * 
      * @param key Key used for localization.
      * @param provider Provider used for localization.
      */
     public LocalizableJMenu(String key, ILocalizationProvider provider) {

         this.key = key;
         this.provider = provider;
         
         setText(this.provider.getString(key));
         this.provider.addLocalizationListener(listener);
     }


}
