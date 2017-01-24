package cfh.editor;

import static javax.swing.JOptionPane.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("\n"
                    + "Usage: java " + Main.class.getName() + " <world-class>\n");
            return;
        }
        Class<? extends World> worldClass;
        World world;
        try {
            worldClass = Class.forName(args[0]).asSubclass(World.class);
            world = worldClass.newInstance();
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            showMessageDialog(null, ex, "Exception", ERROR_MESSAGE);
            return;
        }
        new Main(world);
    }
    
    
    private final World world;
    private JFrame frame;
    
    
    private Main(World world) {
        this.world = world;
        SwingUtilities.invokeLater(this::initGUI);
    }
    
    private void initGUI() {
        frame = new JFrame("Editor");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new EditorPanel(world));
        
        frame.setSize(1400, 800);
        frame.validate();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
