package hr.fer.zemris.java.hw11.jnotepadpp.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;

//not supported yet
/**
 * Represents a simple line counter.
 * 
 * This line counter is placed left to the text area.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class JLineCounter extends JList<Long> implements ListDataListener {

    /**
     * Represents the serial Version UID of the line cvounter.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Represents the border thickness.
     */
    private static final int BORDER = 3;

    /**
     * Represents the default font size of the line counter.
     */
    private static final int DEFAULT_FONT_SIZE = 13;
    
    /** 
     * Public constructor.
     * Sets the model to a new {@link LineCounterModel}.
     */
    public JLineCounter() {
        setFont(JNotepadPP.DEFAULT_FONT);
        setForeground(Color.GRAY);
        setSelectionBackground(getBackground());
        setModel(new LineCounterModel());
        setBorder(BorderFactory.createEmptyBorder(0, BORDER, 0, BORDER));
        setFont(new Font(getFont().getName(), Font.PLAIN, DEFAULT_FONT_SIZE));
    }

    @Override
    public void contentsChanged(ListDataEvent e) {
        super.repaint();
    }

    @Override
    public void intervalAdded(ListDataEvent e) {
        super.repaint();
    }

    @Override
    public void intervalRemoved(ListDataEvent e) {
        super.repaint();
    }

}
