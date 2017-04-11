package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.components.JStatusBar;
import hr.fer.zemris.java.hw11.jnotepadpp.components.JToolbarButton;
import hr.fer.zemris.java.hw11.jnotepadpp.icons.Icons;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizableJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

/**
 * Represents the central class of the JNotepadPP program. 
 * Contains all necessary initializations and actions to create a visible
 * frame where user can use the program.
 * 
 * This is a simple text editor containing operations for text-editing such as
 * cut, copy, paste, delete, sort, remove duplicate lines and etc.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class JNotepadPP extends JFrame {

    /**
     * Represents the serial Version UID of the JFrame.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Represents the default font size.
     */
    private static final int DEFAULT_FONT_SIZE = 14;
    
    /**
     * Represents the default font of the frame.
     */
    public final static Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, DEFAULT_FONT_SIZE);
    
    /**
     * Represents the default title of the frame. 
     */
    private static final String DEFAULT_TITLE = "JNotepad++";

    /**
     * Represents the default frame dimension.
     */
    private static final Dimension DEFAULT_DIMENSION = new Dimension(800, 600);

    /**
     * Represents the frame location.
     */
    private static final Point DEFAULT_LOCATION = new Point(100, 100);
    
    /**
     * Represents the default actions of {@link JTextArea}.
     */
    private static final ActionMap DEFAULT_ACTIONS = new JTextArea().getActionMap();
    
    /**
     * Represents the central tabbed pane panel of the frame. 
     */
    private JPanel centralPanel = new JPanel();
    
    /**
     * Represents the tabbed pane of the tabbed pane panel. 
     */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /**
     * Represents the status bar at the bottom of frame. 
     */
    private JStatusBar statusBar = new JStatusBar();
   
    /**
     * Represents the current index of the new blank document created. 
     */
    private int newFileIndex = 1;
    
    /**
     * Represents the providerBridge of the 
     */
    private LocalizationProviderBridge providerBridge = new FormLocalizationProvider(
	  LocalizationProvider.getInstance(), 
	  this
	  );
    
    /** 
     * Public constructor.
     * Sets default size, location, icon and initializes the GUI.
     */
    public JNotepadPP() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocation(DEFAULT_LOCATION);
        setSize(DEFAULT_DIMENSION);
        
        setTitle(DEFAULT_TITLE);
        setIconImage(Icons.MAIN);
        addWindowListener(new WindowAdapter() {
	  @Override
	  public void windowClosing(WindowEvent e) {
	      exitAction.actionPerformed(
		    new ActionEvent(e.getSource(), e.getID(), e.paramString())
		    );
	  }
        });
        
        initGUI();
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }

    /**
     * Initializes the GUI of the frame.
     * Creates toolbars, menus, status bar and appropriate actions.
     * Sets the layout of the tabbed pane panel.
     */
    private void initGUI() {

        createTabbedPane();
        
        try {
	  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
	  //will be default
        }
        
        this.getContentPane().setLayout(new BorderLayout());
        
        centralPanel.setLayout(new BorderLayout());
        centralPanel.add(new JScrollPane(tabbedPane), BorderLayout.CENTER);
        
        this.getContentPane().add(centralPanel, BorderLayout.CENTER);
        
        createActions();
        createMenus();
        createToolbars();
        createStatusBar();
    }

    /**
     * Represents the action of creating a new blank document.
     * When a document is created, a new tab will be added to the tabbed pane with a name 'new X'.
     * X represents an index of the blank document.
     */
    private Action createNewDocumentAction = new LocalizableAction("new", providerBridge) {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

	  JTextArea textArea = getNewTextArea("");
	  
	  tabbedPane.addTab(
		this.provider.getString(this.key) + " " + newFileIndex, 
		Icons.NOT_SAVED, 
		new JScrollPane(textArea)
		);
	  newFileIndex++;
        }
    };

    /**
     * Represents the action of opening a document from disk.
     * User is allowed to select any file from disk, wether it
     * is a txt file or not.
     */
    private Action openDocumentAction = new LocalizableAction("open", providerBridge) {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
	  JFileChooser fc = new JFileChooser();
	  fc.setDialogTitle(this.provider.getString("openFile"));
	  if (fc.showOpenDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION)
	      return;

	  File fileName = fc.getSelectedFile();
	  Path filePath = fileName.toPath();

	  if (!Files.isReadable(filePath)) {
	      JOptionPane.showMessageDialog(
		    JNotepadPP.this, 
		    this.provider.getString("fileNotFound"), 
		    this.provider.getString("error"), 
		    JOptionPane.ERROR_MESSAGE
		    );
	      return;
	  }

	  byte[] octets;
	  try {
	      octets = Files.readAllBytes(filePath);
	  } catch (Exception ex) {
	      JOptionPane.showMessageDialog(
		    JNotepadPP.this, 
		    this.provider.getString("readingError"), 
		    this.provider.getString("error"),
		    JOptionPane.ERROR_MESSAGE
		    );
	      return;
	  }

	  String text = new String(octets, StandardCharsets.UTF_8);
	  JTextArea textArea = getNewTextArea(text);

	  tabbedPane.addTab(
		filePath.getFileName().toString(), 
		Icons.NOT_SAVED, 
		new JScrollPane(textArea),
		filePath.toString()
		);
        }

    };

    /**
     * Represents the action of saving a file to the disk.
     * File can be saved to disk to a new location or to an existing location.
     * Depending on that, different type of arguments will be sent while creating this object.
     * 
     * @author Marin Grbić
     * @version 1.0
     */
    private class SaveDocumentAction extends LocalizableAction {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Represents the flag of requesting a new path for saving.
         */
        private boolean requestNewPath;

        /**
         * Represents the function used for calculation which tab should be saved.
         */
        private Function<JTabbedPane, Integer> tabCalculation;

        /** 
         * Public constructor.
         * Sets fields to the given values.
         * 
         * @param key Key used for localization.
         * @param provider Localization provider.
         * @param requestNewPath True if a new path should be requested (Save As), elseway false.
         * @param tabCalculation Function which calculates the index of tab which should be saved.
         */
        public SaveDocumentAction(String key, ILocalizationProvider provider, 
	      boolean requestNewPath, Function<JTabbedPane, Integer> tabCalculation) {
	  super(key, provider);
	  this.requestNewPath = requestNewPath;
	  this.tabCalculation = tabCalculation;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
	  int selectedIndex = tabCalculation.apply(tabbedPane);
	  if (selectedIndex == -1)
	      return;

	  String path = tabbedPane.getToolTipTextAt(tabbedPane.getSelectedIndex());

	  Path openedFilePath;
	  if (path == null || requestNewPath) {
	      JFileChooser jfc = new JFileChooser();
	      jfc.setDialogTitle(this.provider.getString("saveDocument"));
	      if (jfc.showSaveDialog(JNotepadPP.this) != JFileChooser.APPROVE_OPTION) {
		JOptionPane.showMessageDialog(
		        JNotepadPP.this, 
		        this.provider.getString("fileNotSaved"), 
		        this.provider.getString("warning"),
		        JOptionPane.WARNING_MESSAGE
		        );
		return;
	      }
	      openedFilePath = jfc.getSelectedFile().toPath();
	  } else {
	      openedFilePath = Paths.get(path);
	  }

	  JTextArea currentArea = (JTextArea)(
		(JScrollPane)tabbedPane.getSelectedComponent()
		).getViewport().getView();
	  byte[] data = currentArea.getText().getBytes(StandardCharsets.UTF_8);

	  try {
	      Files.write(openedFilePath, data);
	  } catch (IOException e1) {
	      JOptionPane.showMessageDialog(
		    JNotepadPP.this, 
		    this.provider.getString("writingError"), 
		    this.provider.getString("error"), 
		    JOptionPane.ERROR_MESSAGE
		    );
	      return;
	  }

	  JOptionPane.showMessageDialog(
		JNotepadPP.this, 
		this.provider.getString("fileSaved"), 
		this.provider.getString("information"), 
		JOptionPane.INFORMATION_MESSAGE
		);

	  tabbedPane.setTitleAt(selectedIndex, openedFilePath.getFileName().toString());
	  tabbedPane.setToolTipTextAt(selectedIndex, openedFilePath.toString());
	  tabbedPane.setIconAt(selectedIndex, Icons.SAVED);
	  
	  //TODO: try to find better solution
	  //in case the tab name changed from New X to some path, tabbedPane will not call the method stateChanged
	  tabbedPane.getChangeListeners()[0].stateChanged(new ChangeEvent(e.getSource()));
        }
    };

    /**
     * Represents the function used for calculation of the tab index.
     * As a result, this function will return the index of selected tab.
     */
    private Function<JTabbedPane, Integer> selectedTab = t -> t.getSelectedIndex();
    
    /**
     * Represents the 'save document' Action.
     * This action does not prompt user to select a new destination path.
     */
    private Action saveDocumentAction = new SaveDocumentAction("save", providerBridge, false, selectedTab);
    
    /**
     * Represents the 'save as document' Action.
     * This action prompts user to select a new destination path.
     */
    private Action saveAsDocumentAction = new SaveDocumentAction("saveAs", providerBridge, true, selectedTab);
    
    /**
     * Represents the statistics action. This action calculates number
     * of currently assigned lines, characters and non-blank characters.
     * 
     * The result will be prompted to user in a new window.
     */
    private Action statisticsAction = new LocalizableAction("statistics", providerBridge) {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
	  int selectedIndex = tabbedPane.getSelectedIndex();
	  if (selectedIndex == -1) return;

	  JTextArea currentArea = (JTextArea)(
		(JScrollPane)tabbedPane.getSelectedComponent()
		).getViewport().getView();
	  String text = currentArea.getText();

	  int characters = text.length();
	  int nonBlankCharacters = text.replaceAll("\\s", "").length();
	  int lines = currentArea.getLineCount();

	  JOptionPane.showMessageDialog(
		JNotepadPP.this,
		String.format(this.provider.getString("statisticsFormat"), characters, nonBlankCharacters, lines), 
		this.provider.getString(this.key),
		JOptionPane.INFORMATION_MESSAGE
		);
        }
    };
    
    /**
     * Represents the action used for language changing.
     * 
     * This action will only set the providers language to the selected one.
     * Provided languages are: German, Croatian and English.
     * 
     * @author Marin Grbić
     * @version 1.0
     */
    private class LanguageChangeAction extends LocalizableAction {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Represents the selected language which is going to be set.
         */
        private String language;
        
        /** 
         * Public constructor.
         * Sets fields to the given values.
         * 
         * @param key Key used for localization.
         * @param language Selected language which is going to be set.
         */
        public LanguageChangeAction(String key, String language) {
	  super(key, JNotepadPP.this.providerBridge);
	  this.language = language;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
	  LocalizationProvider.getInstance().setLanguage(language);
        } 
    }
    
    /**
     * Represents the action which sets the language to Croatian.
     */
    private Action croatianAction = new LanguageChangeAction("croatian", "hr");
    
    /**
     * Represents the action which sets the language to English. 
     */
    private Action englishAction = new LanguageChangeAction("english", "en");
    
    /**
     * Represents the action which sets the language to German.
     */
    private Action germanAction = new LanguageChangeAction("german", "de");
  
    /**
     * Represents the action which closes the window and terminates the program.
     * 
     * If there are any not saved tabs, it will prompt the user to save it.
     */
    private Action exitAction = new LocalizableAction("exit", providerBridge) {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e){
	  int size = tabbedPane.getTabCount();

	  for(int i = size - 1; i >= 0; i--) {
	      if(tabbedPane.getIconAt(i).equals(Icons.SAVED)) continue;
	      if(promptFileSaving(i, e) == true) {
		return;
	      }
	  }
	  
	  statusBar.setWindowOpened(false);
	  dispose();
        }
    };

    /**
     * Represents the standard UI action which is already implemented in {@link JTextArea} component.
     * 
     * This class is used only for fetching the action from the {@link JTextArea} ActionMap
     * and setting an appropriate action name.
     * 
     * @author Marin Grbić
     * @version 1.0
     */
    private class UIAction extends LocalizableAction {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Represents the action key used for fetching the action from action map.
         */
        private String actionKey;
        
        /** 
         * Public constructor.
         * Sets fields to the given values.
         * 
         * @param key Key used for localization.
         * @param provider Localization provider.
         * @param actionKey Key used for fetching the action from action map.
         */
        public UIAction(String key, ILocalizationProvider provider, String actionKey) {
	  super(key, provider);
	  this.actionKey = actionKey;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
	  Action action = DEFAULT_ACTIONS.get(actionKey);
	  if(action == null) return;
	  
	  action.actionPerformed(e);
        }
    }
    
    /**
     * Represents the copy action.
     */
    private Action copyAction = new UIAction("copy", providerBridge, DefaultEditorKit.copyAction);
    
    /**
     * Represents the cut action.
     */
    private Action cutAction = new UIAction("cut", providerBridge, DefaultEditorKit.cutAction);

    /**
     * Represents the paste action.
     */
    private Action pasteAction = new UIAction("paste", providerBridge, DefaultEditorKit.pasteAction);
    
    /**
     * Represents the action which closes the current tab.
     * User will be prompted to save the file before this action will be executed.
     */
    private Action closeCurrentTabAction = new LocalizableAction("closeTab", providerBridge) {
        
        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
	  int selectedIndex = tabbedPane.getSelectedIndex();
	  if(selectedIndex == -1) return;
	  
	  if(promptFileSaving(selectedIndex, e)) return;
	  tabbedPane.remove(tabbedPane.getSelectedIndex());
        }
    };
    
    
    /**
     * Creates and initializes the tabbed pane which displays the tabs.
     * This method adds all appropriate listeners and initializes them.
     */
    private void createTabbedPane() {
       
        JPopupMenu closeTabMenu = new JPopupMenu();
        closeTabMenu.add(new JMenuItem(closeCurrentTabAction));
        tabbedPane.setComponentPopupMenu(closeTabMenu);
        
        tabbedPane.addMouseListener(new MouseAdapter() {
	  @Override
	  public void mouseClicked(MouseEvent e) {
	      if(e.getButton() == MouseEvent.BUTTON2) {
		closeCurrentTabAction.actionPerformed(
		        new ActionEvent(e.getSource(), e.getID(), e.paramString())
		        );
	      }
	      
	      
	  }
        });
        tabbedPane.addChangeListener(new ChangeListener() {
	  @Override
	  public void stateChanged(ChangeEvent e) {
	      int selectedIndex = tabbedPane.getSelectedIndex();
	      if(selectedIndex == -1) return;
	      
	      JTextArea currentArea = (JTextArea)(
		    (JScrollPane)tabbedPane.getSelectedComponent()
		    ).getViewport().getView();
	      refreshFrameTitle(selectedIndex);
	      updateStatusBar(currentArea);
}
	  
	  /**
	   * Refreshes the frame title depending on which tab is currently opened.
	   * Writes the full path to the file to the frame title adding - "JNotepad++"
	   * at the end of title.
	   * 
	   * @param selectedIndex Index of currently selected tab.
	   */
	  private void refreshFrameTitle(int selectedIndex) {
	      
	      String frameTitle = tabbedPane.getToolTipTextAt(selectedIndex);
	      if (frameTitle == null) {
		frameTitle = DEFAULT_TITLE;
	      } else {
		frameTitle += " - " + DEFAULT_TITLE;
	      }

	      JNotepadPP.this.setTitle(frameTitle);
	  }
        });
    }

    /**
     * Represents the action which is able to change the letter case of selected text.
     * 
     * Declares the function used for changing (to uppercase, to lowercase or inverting),
     * but does not define it.
     * 
     * @author Marin Grbić
     * @version 1.0
     */
    private class ChangeCaseAction extends LocalizableAction {

        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Represents the function used for case changing.
         */
        private Function<Character, Character> changeCaseFunction;
        
        /** 
         * Public constructor.
         * Sets fields to the given values.
         * 
         * @param key Key used for localization.
         * @param provider Localization provider.
         * @param changeCaseFunction Function used for case changing.
         */
        public ChangeCaseAction(String key, ILocalizationProvider provider, Function<Character, Character> changeCaseFunction) {
	  super(key, provider);
	  this.changeCaseFunction = changeCaseFunction;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
	  int selectedIndex = tabbedPane.getSelectedIndex();
	  if(selectedIndex == -1) return;
	  
	  JTextArea currentArea = (JTextArea)((JScrollPane)tabbedPane.getSelectedComponent()).getViewport().getView();
	  Caret caret = currentArea.getCaret();
	  if(caret.getDot() == caret.getMark()) return;
	  
	  char[] selectedText = currentArea.getSelectedText().toCharArray();
	  
	  int begin = Math.min(caret.getDot(), caret.getMark());
	  int length = Math.abs(caret.getDot() - caret.getMark());
	  
	  for(int i = 0; i < selectedText.length; i++) {
	      selectedText[i] = changeCaseFunction.apply(selectedText[i]);
	  }
	  
	  Document doc = currentArea.getDocument();
	  
	  try {
	      doc.remove(begin, length);
	      doc.insertString(begin, new String(selectedText), null);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }
        }
    }
    
    /**
     * Represents the action which sets selected text to uppercase.
     */
    private Action toUppercaseAction = new ChangeCaseAction("toUppercase", providerBridge, Character::toUpperCase);
    
    /**
     * Represents the action which sets selected text to lowercase.
     */
    private Action toLowercaseAction = new ChangeCaseAction("toLowercase", providerBridge, Character::toLowerCase);
    
    /**
     * Represents the the action which inverts the case of selected text.
     */
    private Action invertCaseAction = new ChangeCaseAction(
	  "invertCase", 
	  providerBridge, 
	  c -> Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c)
    );
    
    /**
     * Represents the action used for sorting of selected lines.
     * 
     * Lines can be sorted ascendind or descending, depending of the given comparator.
     * User can define the comparator trough constructor while creating a new instance of this action.
     * 
     * @author Marin Grbić
     * @version 1.0
     */
    private class SortAction extends LocalizableAction {

       /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Represents the comparator used for sorting.
         */
        private Comparator<String> comparator;
        
        /** 
         * Public constructor.
         * Sets fields to the given values.
         * 
         * @param key Key used for localization.
         * @param provider Localization provider.
         * @param comparator Comparator used for sorting.
         */
        public SortAction(String key, ILocalizationProvider provider, Comparator<String> comparator) {
	  super(key, provider);
	  this.comparator = comparator;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
	  
	  int selectedIndex = tabbedPane.getSelectedIndex();
	  if(selectedIndex == -1) return;
	  
	  JTextArea currentArea = (JTextArea)((JScrollPane)tabbedPane.getSelectedComponent()).getViewport().getView();

	  Caret caret = currentArea.getCaret();
	  if(caret.getDot() == caret.getMark()) return;
	  
	  int caretStartingOffset = Math.min(caret.getDot(), caret.getMark());
	  int caretEndingOffset = Math.max(caret.getDot(), caret.getMark());
	  
	  int startingLine = 0, endingLine = 0;
	  try {
	      startingLine = currentArea.getLineOfOffset(caretStartingOffset);
	      endingLine = currentArea.getLineOfOffset(caretEndingOffset);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }
	  
	  int startingOffset = 0, endingOffset = 0;
	  try {
	      startingOffset = currentArea.getLineStartOffset(startingLine);
	      endingOffset = currentArea.getLineEndOffset(endingLine);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }
	  
	  Document document = currentArea.getDocument();
	  
	  String text = null;
	  try {
	      text = document.getText(startingOffset, endingOffset - startingOffset);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }
	  
	  List<String> lines = Arrays.asList(text.split("\\R"));
	  
	  Collections.sort(lines, comparator);
	  
	  StringBuilder sb = new StringBuilder();
	  lines.forEach(s -> sb.append(s + "\n"));
	  
	  try {
	      document.remove(startingOffset, endingOffset - startingOffset);
	      document.insertString(startingOffset, sb.toString(), null);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }  
        }
    }
    
    /**
     * Represents the ascending sort action.
     */
    private Action ascendingSortAction = new SortAction(
	  "ascending", 
	  providerBridge, 
	  (a,b) -> {
	      Locale locale = Locale.forLanguageTag(LocalizationProvider.getInstance().getLanguage());
	      return Collator.getInstance(locale).compare(a,b);
	  }
	  );
    
    /**
     * Represents the descending sort action.
     */
    private Action descendingSortAction = new SortAction(
	  "descending", 
	  providerBridge, 
	  (a,b) -> {
	      Locale locale = Locale.forLanguageTag(LocalizationProvider.getInstance().getLanguage());
	      return -Collator.getInstance(locale).compare(a,b);
	  }
	  );
    
    /**
     * Represents the unique action which removes duplicated line in text.
     * This action affects on the entire text, not just on the selected one.
     * 
     * This action will also remove duplicated empty lines 
     * (lines containing only line separator character).
     */
    private Action uniqueAction = new LocalizableAction("unique", providerBridge) {
        
        /**
         * Represents the serial Version UID of the action.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent arg0) {
	  int selectedIndex = tabbedPane.getSelectedIndex();
	  if(selectedIndex == -1) return;
	  
	  JTextArea currentArea = (JTextArea)((JScrollPane)tabbedPane.getSelectedComponent()).getViewport().getView();
	  String text = currentArea.getText();
	  
	  List<String> lines = new ArrayList<>(Arrays.asList(text.split("\\R")));

	  for(int i = 0; i < lines.size() - 1; i++) {
	      for(int j = i + 1; j < lines.size(); j++) {
		if(lines.get(i).equals(lines.get(j))) {
		    lines.remove(j);
		    j--;
		}
	      }
	  }
	  
	  StringBuilder sb = new StringBuilder();
	  lines.forEach(s -> sb.append(s + "\n"));
	  
	  Document document = currentArea.getDocument();
	  try {
	      document.remove(0, document.getLength());
	      document.insertString(0, sb.toString(), null);
	  } catch (BadLocationException e) {
	      e.printStackTrace();
	  }
        }
    };

    /**
     *  Creates actions and initializes informations such as accelerator keys and action icons.
     */
    private void createActions() {

        createNewDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
        createNewDocumentAction.putValue(Action.SMALL_ICON, Icons.NEW_DOC);
        
        openDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
        openDocumentAction.putValue(Action.SMALL_ICON, Icons.OPEN_DOCUMENT);
        
        saveDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveDocumentAction.putValue(Action.SMALL_ICON, Icons.SAVE_DOCUMENT);
        
        saveAsDocumentAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control shift S"));
        saveAsDocumentAction.putValue(Action.SMALL_ICON, Icons.SAVE_AS_DOCUMENT);
        
        statisticsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("shift S"));
        statisticsAction.putValue(Action.SMALL_ICON, Icons.STATISTICS);
       
        closeCurrentTabAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt t f4"));
        closeCurrentTabAction.putValue(Action.SMALL_ICON, Icons.CLOSE_TAB);

        cutAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
        cutAction.putValue(Action.SMALL_ICON, Icons.CUT);
        
        copyAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
        copyAction.putValue(Action.SMALL_ICON, Icons.COPY);
        
        pasteAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
        pasteAction.putValue(Action.SMALL_ICON, Icons.PASTE);
        
        toUppercaseAction.putValue(Action.SMALL_ICON, Icons.TO_UPPERCASE);
        toLowercaseAction.putValue(Action.SMALL_ICON, Icons.TO_LOWERCASE);
        invertCaseAction.putValue(Action.SMALL_ICON, Icons.INVERT_CASE);
        ascendingSortAction.putValue(Action.SMALL_ICON, Icons.ASCENDING);
        descendingSortAction.putValue(Action.SMALL_ICON, Icons.DESCENDING);
        uniqueAction.putValue(Action.SMALL_ICON, Icons.UNIQUE);
        croatianAction.putValue(Action.SMALL_ICON, Icons.CROATIAN);
        germanAction.putValue(Action.SMALL_ICON, Icons.GERMAN);
        englishAction.putValue(Action.SMALL_ICON, Icons.ENGLISH);
        
        exitAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
        exitAction.putValue(Action.SMALL_ICON, Icons.EXIT);
    }

    /**
     * Creates the menu bar of the frame. 
     * Initializes all available menus and sets appropriate menu items.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new LocalizableJMenu("file", providerBridge);
        menuBar.add(fileMenu);

        fileMenu.add(new JMenuItem(createNewDocumentAction));
        fileMenu.add(new JMenuItem(openDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(saveDocumentAction));
        fileMenu.add(new JMenuItem(saveAsDocumentAction));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem(statisticsAction));
        fileMenu.add(new JMenuItem(exitAction));

        JMenu editMenu = new LocalizableJMenu("edit", providerBridge);
        menuBar.add(editMenu);
        
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));
        
        JMenu toolsMenu = new LocalizableJMenu("tools", providerBridge);
        menuBar.add(toolsMenu);
        JMenu changeCaseMenu = new LocalizableJMenu("changeCase", providerBridge);
        changeCaseMenu.setIcon(Icons.CHANGE_CASE);
        
        changeCaseMenu.add(new JMenuItem(toUppercaseAction));
        changeCaseMenu.add(new JMenuItem(toLowercaseAction));
        changeCaseMenu.add(new JMenuItem(invertCaseAction));
        toolsMenu.add(changeCaseMenu);

        JMenu sortMenu = new LocalizableJMenu("sort", providerBridge);
        sortMenu.setIcon(Icons.SORT);
        sortMenu.add(new JMenuItem(ascendingSortAction));
        sortMenu.add(new JMenuItem(descendingSortAction));
        toolsMenu.add(sortMenu);
        
        toolsMenu.add(new JMenuItem(uniqueAction));
        
        JMenu languagesMenu = new LocalizableJMenu("languages", providerBridge);
        languagesMenu.add(new JMenuItem(englishAction));
        languagesMenu.add(new JMenuItem(germanAction));
        languagesMenu.add(new JMenuItem(croatianAction));    
        
        menuBar.add(languagesMenu);
     
        this.setJMenuBar(menuBar);
    }

    /**
     * Creates the toolbar used in this frame.
     * Tollbar contains shortcuts to most of the available actions.
     */
    private void createToolbars() {

        JToolBar toolBar = new JToolBar(providerBridge.getString("tools"));
        toolBar.setFloatable(true);
        
        toolBar.add(new JToolbarButton(createNewDocumentAction));
        toolBar.add(new JToolbarButton(openDocumentAction));
        toolBar.addSeparator();
        
        toolBar.add(new JToolbarButton(saveDocumentAction));
        toolBar.add(new JToolbarButton(saveAsDocumentAction));
        toolBar.addSeparator();
        
        toolBar.add(new JToolbarButton(cutAction));
        toolBar.add(new JToolbarButton(copyAction));
        toolBar.add(new JToolbarButton(pasteAction));
        toolBar.addSeparator();
        
        toolBar.add(new JToolbarButton(statisticsAction));
        toolBar.addSeparator();
        
        toolBar.add(new JButton(closeCurrentTabAction));
        
        this.getContentPane().add(toolBar, BorderLayout.NORTH);
    }
    
    /**
     * Creates the status bar and adds it to the bottom of main panel.
     * 
     * Status bar contains informations about current text area, such
     * as text length and current caret position.
     */
    private void createStatusBar() {
        centralPanel.add(statusBar, BorderLayout.SOUTH);
        statusBar.setWindowOpened(true);
    }

    /**
     * Gets a newly initialized text area used for text editing.
     * This method registers several listeners and returns a new created instance of {@link JTextArea}.
     * 
     * @param text The text to be displayed in the area.
     * @return Initialized new instance of {@link JTextArea}.
     */
    private JTextArea getNewTextArea(String text) {

        JTextArea textArea = new JTextArea(text);
        textArea.setFont(DEFAULT_FONT);
        textArea.getDocument().addDocumentListener(new DocumentListener() {

	  @Override
	  public void removeUpdate(DocumentEvent e) {
	      action(e);
	  }

	  @Override
	  public void insertUpdate(DocumentEvent e) {
	      action(e);
	  }

	  @Override
	  public void changedUpdate(DocumentEvent e) {
	      action(e);
	  }
	  
	  /**
	   * Action to be done on document change.
	   * Updates the status bar and status icon.
	   * @param e The document event.
	   */
	  private void action(DocumentEvent e) {
	      tabbedPane.setIconAt(tabbedPane.getSelectedIndex(), Icons.NOT_SAVED);
	      statusBar.updateLength(textArea.getDocument().getLength());
	  }
        });
        
        textArea.addCaretListener((event)-> {
	  updateStatusBar(textArea);
        });
        
        JPopupMenu editMenu = new JPopupMenu();
      
        editMenu.add(new JMenuItem(cutAction));
        editMenu.add(new JMenuItem(copyAction));
        editMenu.add(new JMenuItem(pasteAction));
        editMenu.addSeparator();
        editMenu.add(new JMenuItem(closeCurrentTabAction));
        textArea.setComponentPopupMenu(editMenu);
        
        return textArea;
    }
    
    
    /**
     * Updates the status bar parameters.
     * Status bar is showing the user current text length, current caret position
     * and current time.
     * 
     * @param textArea Text area whose information is being shown.
     */
    private void updateStatusBar(JTextArea textArea) {
        Caret caret = textArea.getCaret();
        
        int position = caret.getDot();
        
        int line = 0;
        try {
	  line = textArea.getLineOfOffset(position);
        } catch (BadLocationException e1) {
	  e1.printStackTrace();
        }
        statusBar.updateCaretLine(line + 1);
        
        int column = 0;
        try {
	  column = position - textArea.getLineStartOffset(line);
        } catch (BadLocationException e1) {
	  e1.printStackTrace();
        }
        statusBar.updateCaretColumn(column + 1);
        
        int selected = Math.abs(caret.getDot() - caret.getMark());
        statusBar.updateCaretSelected(selected);
        statusBar.updateLength(textArea.getDocument().getLength());
    }
    
    /**
     * Prompts the user to save a file.
     * User can choose between three options: yes, no or cancel.
     * 
     * If yes was chosen, file will be saved to the selected location.
     * If no was chose, file will be skipped for saving.
     * If cancel was chosen, exit action will be aborted.
     * 
     * @param i Index of tab which is prompted to save.
     * @param e Action event of this action.
     * @return True if abort of action was called.
     */
    private boolean promptFileSaving(int i, ActionEvent e) {

	  String fileName = tabbedPane.getTitleAt(i);
	  
	  int answer = JOptionPane.showConfirmDialog(
		JNotepadPP.this, 
		fileName + " " + LocalizationProvider.getInstance().getString("notSavedFile"),
		LocalizationProvider.getInstance().getString("saveFile") + "?",
		JOptionPane.YES_NO_CANCEL_OPTION
		);
	  if(answer == JOptionPane.NO_OPTION) return false;
	  if(answer == JOptionPane.CANCEL_OPTION) return true;
	  
	  if(tabbedPane.getToolTipTextAt(i) != null) {
	      new SaveDocumentAction("save", JNotepadPP.this.providerBridge, false, tabbedPane -> i).actionPerformed(e);;
	  } else {
	      new SaveDocumentAction("saveAs", JNotepadPP.this.providerBridge, true, tabbedPane -> i).actionPerformed(e);;
	  }
	  
	  return false;
    }
    
    /**
     * Entry point of the program.
     * Creates a new instance {@link JNotepadPP} and sets the frame's visibility
     * to <code>true</code>.
     * @param args Command line arguments - not used.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
	  new JNotepadPP().setVisible(true);
        });
    }
}