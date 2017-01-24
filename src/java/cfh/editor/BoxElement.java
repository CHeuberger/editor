package cfh.editor;


public abstract class BoxElement extends Element {

    private int x;
    private int y;
    private int w;
    private int h;

    
    protected BoxElement() {
    }
    
    public int x() { return x; }
    
    public int y() { return y; }
    
    public void x(int x) {
        this.x = x;
    }
    
    public void y(int y) {
        this.y = y;
    }
    
    public int w() { return w; }
    
    public int h() { return h; }
    
    public void w(int w) {
        assert w >= 0 : w;
        this.w = w;
    }
    
    public void h(int h) {
        assert h >= 0 : h;
        this.h = h;
    }
    
    @Override
    public MouseHandle inside(double lx, double ly) {
        if (lx >= x+w-5 && lx < x+w && ly >= y+h-5 && ly < y+h) 
            return new MoveHandle() {
                @Override
                public void x(int toX) {
                    w(toX - x);
                }
                @Override
                public int x() {
                    return x + w;
                }
                @Override
                public void y(int toY) {
                    h(toY - y);
                }
                @Override
                public int y() {
                    return y + h;
                }
            };
        else if (lx >= x && lx < x+w && ly >= y && ly < y+h)
            return new MoveHandle() {
                @Override
                public void x(int toX) {
                    BoxElement.this.x(toX);
                }
                @Override
                public int x() {
                    return BoxElement.this.x();
                }
                @Override
                public void y(int toY) {
                    BoxElement.this.y(toY);
                }
                @Override
                public int y() {
                    return BoxElement.this.y();
                }
        };
        else
            return null;
    }
    
}
