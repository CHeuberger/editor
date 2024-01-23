package cfh.test;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import cfh.editor.BoxElement;


public class Light extends BoxElement {
    
    private double voltage;
    
    @Override
    public void paintElement(Graphics2D gg) {
        // TODO
        if (gg.getBackground() != null) {
            gg.clearRect(x(), y(), w(), h());
        }
        gg.drawRect(x(), y(), w(), h());
        drawConnection(gg, x(), y()+h()/2);
        
        if (w() > 5 && h() > 5) {
            Ellipse2D.Double ellipse = new Ellipse2D.Double(x()+2, y()+2, w()-4, h()-4);
            gg.draw(ellipse);
            gg.setClip(ellipse);
            gg.drawLine(x()+2, y()+2, x()+w()-2, y()+h()-2);
            gg.drawLine(x()+2, y()+h()-2, x()+w()-2, y()+2);
        }
    }

    public void voltage(double voltage) {
        this.voltage = voltage;
    }
}
