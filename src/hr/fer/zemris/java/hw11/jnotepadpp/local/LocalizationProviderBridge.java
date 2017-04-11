package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Represents the bridge between the current opened frame
 * and the localization provider.
 * 
 * When asked to resolve a key delegates this request to wrapped
 * {@link ILocalizationProvider} object. When user calls method for connecting, 
 * the method will register an instance of {@link ILocalizationListener} on 
 * the decorated object. When user calls method for disconnecting, this object will be 
 * deregistered from decorated object.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /**
     * Represents the connected state of bridge.
     * Can be <code>true</code> or <code>false</code>.
     */
    private boolean connected = false;
    
    /**
     * Represents the actual provider used for localization.
     */
    private ILocalizationProvider provider;
    
    /**
     * Represents the listener of the bridge.
     * This listener will inform all provider's listeners
     * an action was performed.
     */
    private ILocalizationListener listener = () -> fire();
    
    /** 
     * Public constructor.
     * Sets the given provider.
     * 
     * @param provider Provider used for localization.
     */
    public LocalizationProviderBridge(ILocalizationProvider provider) {
        super();
        this.provider = provider;
    }
    
    /**
     * Connects the listener to the actual provider.
     */
    public void connect() {
        if(connected) return;
        
        provider.addLocalizationListener(listener);
        connected = true;
    }

    /**
     * Disconnects the listener from the actual provider.
     */
    public void disconnect() {
        provider.removeLocalizationListener(listener);
        connected = false;
    }
    
    @Override
    public String getString(String string) {
        return provider.getString(string);
    }
}
