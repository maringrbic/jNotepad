package hr.fer.zemris.java.hw11.jnotepadpp.components;

import javax.swing.Action;
import javax.swing.JButton;

/**
 * Represents a simple toolbar button.
 * 
 * Only difference between {@link JToolbarButton} and {@link JButton} is 
 * the fact {@link JToolbarButton} is not showing any text, but only the icon.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class JToolbarButton extends JButton {

    /**
     * Represents the serial Version UID of the toolbar button.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Public constructor.
     * Sets the action to be done after clicking on button.
     * 
     * @param a The given action.
     * 
     */
    public JToolbarButton(Action a) {
        super(a);
        setText("");
    }
    
    @Override
    public void repaint() {
        super.repaint();
        setText("");
    }
}
