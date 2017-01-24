package cfh.editor;

public interface MoveHandle extends MouseHandle {

    public int x();
    public int y();

    public void x(int x);
    public void y(int y);
}