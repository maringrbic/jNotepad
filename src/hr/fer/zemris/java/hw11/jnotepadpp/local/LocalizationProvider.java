package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Represents the singleton design pattern used to store the information 
 * on the selected language and the loaded resource bundle.
 * 
 * This is a concrete implementation of a localization provider having 
 * appropriate method for setting a new language.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Represents the path to the translation files.
     */
    private static final String TRANSLATIONS_PATH = 
	  "hr.fer.zemris.java.hw11.jnotepadpp.local.translations";
    
    /**
     * Represents the default language to set.
     */
    private static final String DEFAULT_LANGUAGE = "en";
    
    /**
     * Represents the provider of the localization.
     */
    private static LocalizationProvider provider;
    
    /**
     * Represents the current language set.
     */
    private String language;
    
    /**
     * Represents the resource bundle used for localization. 
     */
    private ResourceBundle bundle;

    /** 
     * Private constructor.
     * Sets the default language to English, initializes
     * the resource bundle.
     */
    private LocalizationProvider() {
        this.language = DEFAULT_LANGUAGE;
        Locale locale = Locale.forLanguageTag(DEFAULT_LANGUAGE);
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS_PATH, locale);
    }

    /**
     * Gets a new instance of this class if the current instance is null.
     * This method will assure the user that only one instance of this
     * object will be created.
     * 
     * @return Instance of this class.
     */
    public static LocalizationProvider getInstance() {
        if(provider == null) {
	  provider = new LocalizationProvider();
        }
        return provider;
    }
    /**
     * The language setter.
     * @param language The language to set.
     */
    public void setLanguage(String language) {
        this.language = language;
        Locale locale = Locale.forLanguageTag(this.language);
        this.bundle = ResourceBundle.getBundle(TRANSLATIONS_PATH, locale);
        fire();
    }

    @Override
    public String getString(String string) {
        return bundle.getString(string);
    }

    /**
     * Current language getter.
     * @return Gets the language.
     */
    public String getLanguage() {
        return this.language;
    }
}
