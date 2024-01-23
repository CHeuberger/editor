package cfh.editor;

import static cfh.editor.Tool.*;
import static java.util.Objects.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


@SuppressWarnings("serial")
public class CompoundCanvas extends Component {

    private final Compound compound;
    
    private Tool tool = null;
    
    private double x = 0;
    private double y = 0;
    private double zoom = 1;
    
    
    CompoundCanvas(Compound compound) {
        this.compound = requireNonNull(compound);
        
        MouseAdapter scrollListener = new MouseAdapter() {
            private Point point = null;
            @Override
            public void mousePressed(MouseEvent ev) {
                if (isScrollButton(ev)) {
                    point = ev.getPoint();
                    point.x -= x;
                    point.y -= y;
                }
            }
            @Override
            public void mouseReleased(MouseEvent ev) {
                point = null;
            }
            @Override
            public void mouseDragged(MouseEvent ev) {
                if (point != null && isScrollButton(ev)) {
                    x = ev.getX() - point.x;
                    y = ev.getY() - point.y;
                    repaint();
                }
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (isScrollResetButton(ev)) {
                    zoom = 1.0;
                    x = 0.0;
                    y = 0.0;
                    repaint();
                }
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent ev) {
                if (isPlain(ev)) {
                    double fx = (ev.getX() - x) / zoom;
                    double fy = (ev.getY() - y) / zoom;
                    zoom *= Math.pow(0.9, ev.getPreciseWheelRotation());
                    if (zoom < 0.1) zoom = 0.1;
                    if (zoom > 10.0) zoom = 10.0;
                    if (0.999 < zoom && zoom < 1.001) zoom = 1.0;
                    x = ev.getX() - fx * zoom;
                    y = ev.getY() - fy * zoom;
                    repaint();
                } else if (isCtrl(ev)) {
                    x -= 10 * ev.getUnitsToScroll();
                    repaint();
                } else if (isShift(ev)) {
                    y -= 10 * ev.getUnitsToScroll();
                    repaint();
                }
            }
        };
        addMouseListener(scrollListener);
        addMouseMotionListener(scrollListener);
        addMouseWheelListener(scrollListener);
        
        MouseAdapter toolAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent ev) {
                if (tool != null) call(tool::pressed, ev);
            }
            @Override
            public void mouseReleased(MouseEvent ev) {
                if (tool != null) call(tool::released, ev);
            }
            @Override
            public void mouseClicked(MouseEvent ev) {
                if (tool != null) call(tool::clicked, ev);
            }
            @Override
            public void mouseDragged(MouseEvent ev) {
                if (tool != null) call(tool::dragged, ev);
            }
            
            private void call(Tool.MouseFunction function, MouseEvent ev) {
                double modelX = (ev.getX() - x) / zoom;
                double modelY = (ev.getY() - y) / zoom;
                Element over = null;
                List<Element> elements = compound.elements();
                ListIterator<Element> iter = elements.listIterator(elements.size());
                while (iter.hasPrevious()) {
                    Element element = iter.previous();
                    if (element.over(modelX, modelY)) {
                        over = element;
                        break;
                    }
                }
                function.call(ev, modelX, modelY, over);
            }
        };
        addMouseListener(toolAdapter);
        addMouseMotionListener(toolAdapter);
    }
    
    public Compound compound() { return compound; }
    
    public void tool(Tool tool) {
        this.tool = tool;
    }
    

    @Override
    public void paint(Graphics g) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.drawString(String.format(Locale.ROOT, "%1.1f,%1.1f  %d", x, y, (int) (100 * zoom)), 4, getHeight()-4);
        gg.setBackground(null);
        if (compound != null) {
            try {
                gg.translate(x, y);
                gg.scale(zoom, zoom);
                compound.paintContent(gg);
            } finally {
                gg.dispose();
            }
        }
    }
    
    
    private boolean isScrollButton(MouseEvent ev) {
        return isPlain(ev) && isButton3(ev);
    }
    
    private boolean isScrollResetButton(MouseEvent ev) {
        return ev.getClickCount() == 2 && isButton3(ev);
    }
}
