package hr.fer.zemris.java.hw11.jnotepadpp.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Represents the status bar containing informations about the text area.
 * 
 * This status bar contains text length, current caret position, date and time.
 * To show some informations, at least one text area should be opened.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class JStatusBar extends JPanel {

    /**
     * Represents the serial Version UID of the status bar.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the label showing the text length.
     */
    private JLabel length = new JLabel(" length:");

    /**
     * Represents the current caret position.
     */
    private JPanel carretPosition = new JPanel();

    /**
     * Represents the clock of the status bar.
     */
    private JLabel clock = new JLabel();
    
    /**
     * Represents the border thickness.
     */
    private static final int BORDER = 2;
    
    /**
     * Represents the constant of one second.
     */
    private static final int ONE_SECOND = 1000;
    
    /**
     * Represents the window opened flag.
     */
    private volatile boolean windowOpened = true;
    
    /** 
     * Public constructor.
     * Sets the layout and initializes the status bar.
     */
    public JStatusBar() {
        setLayout(new BorderLayout());
        add(length, BorderLayout.WEST);
        setBorder(BorderFactory.createMatteBorder(BORDER, 0, 0, 0, Color.GRAY));
        carretPosition.add(new JLabel("Ln: "));
        carretPosition.add(new JLabel(" Col: "));
        carretPosition.add(new JLabel(" Sel: "));
        add(carretPosition, BorderLayout.CENTER);

        initClock();
        add(clock, BorderLayout.EAST);
    }

    /**
     * Initializes the status bar's clock and date.
     */
    private void initClock() {
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter clockFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");;

        Thread t = new Thread(() -> {
	  while (windowOpened) {
	      try {
		Thread.sleep(ONE_SECOND);
	      } catch (Exception ex) {}

	      SwingUtilities.invokeLater(() -> {
		String dateText = LocalDate.now().format(dateFormatter);
		String timeText = LocalTime.now().format(clockFormatter);
		clock.setText(dateText + " " + timeText + " ");
	      });
	  }
        });
        t.setDaemon(true);
        t.start();

    }

    /**
     * 
     * Updates the current text length.
     * 
     * @param length The length to set.
     */
    public void updateLength(int length) {
        this.length.setText(" length: " + length);
    }

    /**
     * 
     * Updates the current caret line.
     * 
     * @param line Line index to set.
     */
    public void updateCaretLine(int line) {
        updateComponent(0, "Ln: ", line);
    }

    /**
     * Updates the current caret column.
     * 
     * @param column Column index to set.
     */
    public void updateCaretColumn(int column) {
        updateComponent(1, " Col: ", column);
    }

    /**
     * 
     * Updates the current number of selected characters.
     * 
     * @param selected Number of selected characters.
     */
    public void updateCaretSelected(int selected) {
        updateComponent(2, " Sel: ", selected);
    }

    /**
     * Updates a component defined by it's index.
     * 
     * Sets appropriate string and given value.
     * 
     * @param n Index of component stored in this panel.
     * @param s String to be represented.
     * @param value Number to be represented.
     */
    private void updateComponent(int n, String s, int value) {
        ((JLabel) this.carretPosition.getComponent(n)).setText(s + value);
    }

    /**
     * The windowOpened setter.
     * @param windowOpened The windowOpened to set.
     */
    public void setWindowOpened(boolean windowOpened) {
        this.windowOpened = windowOpened;
    }
}
