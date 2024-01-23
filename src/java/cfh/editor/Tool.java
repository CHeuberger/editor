package cfh.editor;

import static java.awt.event.InputEvent.*;
import static java.awt.GridBagConstraints.*;
import static javax.swing.JOptionPane.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public abstract class Tool {

    public static final int MODS = ALT_DOWN_MASK | ALT_GRAPH_DOWN_MASK | CTRL_DOWN_MASK | SHIFT_DOWN_MASK | META_DOWN_MASK;

    private final String name;
    private final List<String> path;
    
    protected CompoundCanvas canvas;
    
    protected Tool(String... fullPath) {
        if (fullPath == null || fullPath.length == 0)
            throw new IllegalArgumentException("empy path");
        List<String> tmp = new ArrayList<>(Arrays.asList(fullPath));
        name = requireNonNull(tmp.remove(tmp.size()-1));
        path = Collections.unmodifiableList(tmp);
    }
    
    public String name() { return name; }
    public List<String> path() { return path; }
    
    public void canvas(CompoundCanvas canvas) {
        this.canvas = requireNonNull(canvas);
    }
    
    protected void repaint() {
        if (canvas != null) {
            canvas.repaint();
        }
    }
    
    private static final Insets gbcInsets = new Insets(2, 2, 2, 2);
    private static final GridBagConstraints gbcLabel = new GridBagConstraints(0, RELATIVE, 1, 1, 0.0, 0.0, BASELINE_LEADING, NONE, gbcInsets, 0, 0);
    private static final GridBagConstraints gbcField = new GridBagConstraints(RELATIVE, RELATIVE, REMAINDER, 1, 1.0, 0.0, BASELINE_LEADING, NONE, gbcInsets, 0, 0);
    
    // TODO own external class
    public boolean parameterDialog(int x, int y, String title, FieldDesc<?>... fields) {
        JPanel panel = new JPanel(new GridBagLayout());
        JLabel errorMessage = new JLabel();
        panel.add(errorMessage, gbcField);
        
        for (FieldDesc<?> desc : fields) {
            if (desc instanceof LabelField) {
                panel.add(desc.field, gbcField);
            } else {
                panel.add(new JLabel(desc.label), gbcLabel);
                panel.add(desc.field, gbcField);
            }
        }
        
        while (true) {
            JOptionPane pane = new JOptionPane(panel, INFORMATION_MESSAGE, OK_CANCEL_OPTION);
            JDialog dialog = pane.createDialog(canvas, title);
            dialog.setLocation(x, y);
            dialog.setVisible(true);
            int opt = (Integer) (pane.getValue());
//            int opt = showConfirmDialog(canvas, panel, title, OK_CANCEL_OPTION);
            if (opt != OK_OPTION)
                return false;

            List<String> errors = new ArrayList<>();
            for (FieldDesc<?> desc : fields) {
                try {
                    desc.value();  // check if an be parsed
                } catch (NumberFormatException ex) {
                    errors.add(desc.label + " " + ex.getMessage());
                }
            }
            if (errors.isEmpty())
                return true;
            
            errorMessage.setText(errors.stream().collect(joining(", ", "Errors: ", "")));
        }
    }

    // TODO activate/deactivate = set cursor, ...
    
    public void pressed(MouseEvent ev, double modelX, double modelY, Element over) { }
    public void released(MouseEvent ev, double modelX, double modelY, Element over) { }
    public void clicked(MouseEvent ev, double modelX, double modelY, Element over) { }
    public void dragged(MouseEvent ev, double modelX, double modelY, Element over) { }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    protected static abstract class FieldDesc<T> {
        private final String label;
        
        protected JComponent field;
        
        protected FieldDesc(String label) {
            this.label = label;
        }
        public abstract T value();
    }
    
    protected static class LabelField extends FieldDesc<Void> {
        public LabelField(String label) {
            super(label);
            field = new JLabel(label);
        }
        @Override
        public Void value() {
            return null;
        }
    }
    
    protected static class StringField extends FieldDesc<String> {
        public StringField(String label, String initial, int columns) {
            super(label);
            field = new JTextField(initial, columns);
        }
        @Override
        public String value() {
            return ((JTextField)field).getText();
        }
    }
    
    protected static class IntField extends FieldDesc<Integer> {
        public IntField(String label, String initial, int columns) {
            super(label);
            field = new JTextField(initial, columns);
        }
        @Override
        public Integer value() throws NumberFormatException {
            return Integer.decode(((JTextField)field).getText());
        }
    }
    
    protected static class DoubleField extends FieldDesc<Double> {
        public DoubleField(String label, String initial, int columns) {
            super(label);
            field = new JTextField(initial, columns);
        }
        @Override
        public Double value() throws NumberFormatException {
            return Double.valueOf(((JTextField)field).getText());
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static boolean isButton1(MouseEvent ev) {
        return (ev.getButton() == MouseEvent.BUTTON1 || (ev.getModifiersEx() & BUTTON1_DOWN_MASK) != 0);
    }
    
    public static boolean isButton2(MouseEvent ev) {
        return (ev.getButton() == MouseEvent.BUTTON2);
    }
    
    public static boolean isButton3(MouseEvent ev) {
        return (ev.getButton() == MouseEvent.BUTTON3 || (ev.getModifiersEx() & BUTTON3_DOWN_MASK) != 0);
    }
    
    public static boolean isPlain(MouseEvent ev) {
        return (ev.getModifiersEx() & MODS) == 0;
    }
    
    public static boolean isShift(MouseEvent ev) {
        return (ev.getModifiersEx() & MODS) == SHIFT_DOWN_MASK;
    }
    
    public static boolean isCtrl(MouseEvent ev) {
        return (ev.getModifiersEx() & MODS) == CTRL_DOWN_MASK;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public static interface MouseFunction{
        public void call(MouseEvent ev, double modelX, double modelY, Element over);
    }
}
