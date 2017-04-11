package hr.fer.zemris.java.hw11.jnotepadpp.icons;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import hr.fer.zemris.java.hw11.jnotepadpp.JNotepadPP;

/**
 * Represents the icon package used in {@link JNotepadPP}.
 * Each icon is public and initialized so it can be used immediately.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class Icons {
    
    /**
     * Represents the main program icon. 
     */
    public static final Image MAIN = 
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("icon.png"));

    /**
     * Represents the icon used when the file is not saved.
     */
    public static final Icon NOT_SAVED = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("disk--minus.png")));

    /**
     * Represents the icon used when the file is saved.
     */
    public static final Icon SAVED = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("disk--plus.png")));

    /**
     * Represents the icon used for opening new document.
     */
    public static final Icon OPEN_DOCUMENT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("folder-horizontal-open.png")));
    
    /**
     * Represents the icon used for saving document.
     */
    public static final Icon SAVE_DOCUMENT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("disk-return-black.png"))); 
    
    /**
     * Represents the icon used for saving a document as a file.
     */
    public static final Icon SAVE_AS_DOCUMENT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("disks-black.png"))); 
    
    /**
     * Represents the icon used for creating new document.
     */
    public static final Icon NEW_DOC = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("document.png"))); 
    /**
     * Represents the icon used for copy action.
     */
    public static final Icon COPY = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("document-copy.png"))); 
    
    /**
     * Represents the icon used for statistics.
     */
    public static final Icon STATISTICS = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("statistics.png"))); 
    
    /**
     * Represents the icon used for closing a tab.
     */
    public static final Icon CLOSE_TAB = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("cross.png"))); 
    
    /**
     * Represents the icon used for cut action.
     */
    public static final Icon CUT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("scissors-blue.png"))); 
    
    /**
     * Represents the icon used for paste action.
     */
    public static final Icon PASTE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("document--arrow.png"))); 
    
    /**
     * Represents the icon used for changing letter case.
     */
    public static final Icon CHANGE_CASE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("edit-all-caps.png"))); 
    
    /**
     * Represents the icon used for to uppercase action.
     */
    public static final Icon TO_UPPERCASE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("edit-uppercase.png"))); 
    
    /**
     * Represents the icon used for to lowercase action.
     */
    public static final Icon TO_LOWERCASE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("edit-lowercase.png"))); 
    
    /**
     * Represents the icon used for inverting case. 
     */
    public static final Icon INVERT_CASE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("edit-small-caps.png"))); 
    
    /**
     * Represents the icon used for sorting.
     */
    public static final Icon SORT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("sort.png"))); 
    
    /**
     * Represents the icon used for ascending sort. 
     */
    public static final Icon ASCENDING = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("sort-alphabet.png"))); 
    
    /**
     * Represents the icon used for descending sort. 
     */
    public static final Icon DESCENDING = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("sort-alphabet-descending.png"))); 
    
    /**
     * Represents the icon used for unique action.
     */
    public static final Icon UNIQUE = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("document-number-1.png"))); 
    
    /**
     * Represents the icon used for german language. 
     */
    public static final Icon GERMAN = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("Germany.png"))); 
    
    /**
     * Represents the icon used for croatian language.
     */
    public static final Icon CROATIAN = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("Croatia.png"))); 
    
    /**
     * Represents the icon used for english language.
     */
    public static final Icon ENGLISH = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("United-Kingdom.png"))); 
    
    /**
     * Represents the icon used for exit action. 
     */
    public static final Icon EXIT = new ImageIcon(
	  Toolkit.getDefaultToolkit().getImage(Icons.class.getResource("cross-octagon.png"))); 
    
}
