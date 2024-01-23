package cfh.editor;

import static java.lang.Math.*;


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
        assert w > 0 : w;
        this.w = w;
    }

    public void h(int h) {
        assert h > 0 : h;
        this.h = h;
    }

    @Override
    public boolean over(double lx, double ly) {
        return lx > x-0.5 && lx < x+w+0.5 && ly > y-0.5 && ly < y+h+0.5;
    }

    @Override
    public Moveable moveable(double lx, double ly) {
        if (!over(lx, ly))
            return null;
        if (lx >= x+w-5 && ly >= y+h-5)
            return new Moveable() {
                private final BoxElement e = BoxElement.this; 
                @Override
                public void x(int to) {
                    e.w(max(1, to - e.x()));
                }
                @Override
                public int x() {
                    return e.x() + e.w();
                }
                @Override
                public void y(int to) {
                    e.h(max(1, to - e.y()));
                }
                @Override
                public int y() {
                    return e.y() + e.h();
                }
            };
        else
            return new Moveable() {
                @Override
                public void x(int to) {
                    BoxElement.this.x(to);
                }
                @Override
                public int x() {
                    return BoxElement.this.x();
                }
                @Override
                public void y(int to) {
                    BoxElement.this.y(to);
                }
                @Override
                public int y() {
                    return BoxElement.this.y();
                }
            };
    }
}
