package cfh.test;


import java.util.Arrays;
import java.util.List;

import cfh.editor.Compound;
import cfh.editor.MoveTool;
import cfh.editor.Tool;


public class World implements cfh.editor.World {

    private final Compound root = new Compound();
    
    public World() {
        Compound compound = new Compound();
        compound.name("one");
        compound.x(20);
        compound.y(20);
        compound.w(50);
        compound.h(50);
        root.add(compound);
//        root.add(new Compound().name("two").x(90).y(20).w(30).h(30));
    }
    
    @Override
    public List<Tool> tools() {
        return Arrays.asList(
                new MoveTool(),
                new SourceTool()
//                new Tool("test", "1") { },
//                new Tool("test", "2") { },
//                new Tool("test", "sub", "3") { }
                );
    }
    
    @Override
    public Compound root() {
        return root;
    }
}
