package cfh.editor;

import java.awt.event.MouseEvent;


public class MoveTool extends Tool {

    public static final String NAME = "move";

    private Moveable moving = null;
    private double elementX;
    private double elementY;
    
    
    public MoveTool() {
        super("move");
    }
    
    
    @Override
    public void pressed(MouseEvent ev, double modelX, double modelY, Element over) {
        if (isButton1(ev) && over != null) {
            moving = over.moveable(modelX, modelY);
            if (moving != null) {
                elementX = modelX - moving.x();
                elementY = modelY - moving.y();
            }
        }
    }

    @Override
    public void released(MouseEvent ev, double modelX, double modelY, Element over) {
        if (isButton1(ev)) {
            moving = null;
        }
    }

    @Override
    public void dragged(MouseEvent ev, double modelX, double modelY, Element over) {
        if (moving != null && isButton1(ev)) {
            moving.x((int) (modelX - elementX));
            moving.y((int) (modelY - elementY));
            repaint();
        }
    }
}
