package hr.fer.zemris.java.hw11.jnotepadpp.components;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * Represents the list model used for line counting.
 * 
 * This model is able to resize the line counter, add new values
 * and notify all listeners that a change was made.
 * 
 * @author Marin Grbić
 * @version 1.0
 */
public class LineCounterModel implements ListModel<Long> {

    /**
     * Represents the listeners of this list model.
     */
    private List<ListDataListener> listeners = new ArrayList<>();
    
    /**
     * Represents the numbers to show on line counter.
     */
    private List<Long> numbers = new ArrayList<>();
    
    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public Long getElementAt(int i) {
        return numbers.get(i);
    }

    @Override
    public int getSize() {
        return numbers.size();
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
    
    /**
     * Updates the list to the given number of lines.
     * 
     * @param numberOfLines Current number of lines in document.
     */
    public void updateList(int numberOfLines) {
        
        if(numberOfLines == 0) {
	  numbers = new ArrayList<>();
        } else if(numberOfLines < numbers.size()) {
	  numbers.removeAll(numbers.subList(numberOfLines, numbers.size()));
        } else {
	  int size = numbers.size();
	  for(long i = size + 1; i <= numberOfLines; i++) {
	      numbers.add(i);
	  }
        }
        
        listeners.forEach(l -> l.contentsChanged(null));
    }

}
