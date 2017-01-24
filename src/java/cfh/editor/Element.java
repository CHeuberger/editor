package cfh.editor;

import static java.util.Objects.*;

import java.awt.Graphics2D;


public abstract class Element {

    private Compound parent;
    private String name;
    
    
    protected Element() {
    }
    
    public void parent(Compound parent) {
        this.parent = parent;
    }
    
    public void name(String name) {
        this.name = requireNonNull(name);
    }
    
    
    public Compound parent() { return parent; }
    
    public String name() { return name; }
    
    public abstract void paintElement(Graphics2D gg);

    public abstract boolean over(double lx, double ly);
    
    public abstract Moveable moveable(double lx, double ly);
}
