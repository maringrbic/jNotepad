package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Represents the type of bridge which operates between an actual
 * {@link JFrame} and a {@link ILocalizationProvider}.
 * 
 * After frame was opened, will connect to the provider.
 * After fram was close, will disconnect from the provider.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /** 
     * Public constructor.
     * Sets the action depending on changes of the current window.
     * 
     * @param provider Provider used for localization.
     * @param frame Frame which is localizated.
     * 
     */
    public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
        super(provider);
        
        frame.addWindowListener(new WindowAdapter() {

	  @Override
	  public void windowOpened(WindowEvent e) {
	      connect();
	  }

	  @Override
	  public void windowClosed(WindowEvent e) {
	      disconnect();
	  }
        });
    }
}
