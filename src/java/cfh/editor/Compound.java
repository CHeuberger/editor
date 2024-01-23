package cfh.editor;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Compound extends BoxElement {

    private final List<Element> elements = new ArrayList<>();
    
    
    public void add(Element element) {
        synchronized (elements) {
            if (elements.contains(element))
                throw new IllegalArgumentException("already containing element: " + element);
            if (element.parent() != null)
                throw new IllegalArgumentException("element: " + element + " already added at: " + element.parent());
            
            element.parent(this);
            elements.add(element);
        }
    }
    
    public void remove(Element element) {
        synchronized (elements) {
            if (!elements.contains(element)) 
                throw new IllegalArgumentException("element not contained: " + element);
            
            element.parent(null);
            elements.remove(element);
        }
    }
    
    public List<Element> elements() {
        synchronized (elements) {
            return Collections.unmodifiableList(new ArrayList<>(elements));
        }
    }
    
    public void paintContent(Graphics2D gg) {
        for (Element element : elements) {
            Graphics2D g2 = (Graphics2D) gg.create();
            element.paintElement(g2);
            g2.dispose();
        }
    }

    @Override
    public void paintElement(Graphics2D gg) {
        if (gg.getBackground() != null) {
            gg.clearRect(x(), y(), w(), h());
        }
        gg.drawRect(x(), y(), w(), h());
    }
}
