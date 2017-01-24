package cfh.test;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import cfh.editor.BoxElement;


public class Source extends BoxElement {
    
    private double voltage;
    
    @Override
    public void paintElement(Graphics2D gg) {
        // TODO
        if (gg.getBackground() != null) {
            gg.clearRect(x(), y(), w(), h());
        }
        gg.drawRect(x(), y(), w(), h());
        drawConnection(gg, x()+w(), y()+h()/2);
        
        if (w() > 5 && h() > 5) {
            gg.drawOval(x()+2, y()+2, w()-4, h()-4);
            if (w() > 14 && h() > 14) {
                double dw = (w() - 2) / 5.0;
                double dh = (h() - 2) / 5.0;
                double cx = x() + w()/2.0;
                double cy = y() + h()/2.0;
                gg.draw(new Ellipse2D.Double(cx-dw/2-dw, cy-dh/2, dw, dh));
                gg.draw(new Ellipse2D.Double(cx+dw/2, cy-dh/2, dw, dh));
            }
        }
    }

    public void voltage(double voltage) {
        this.voltage = voltage;
    }
}
