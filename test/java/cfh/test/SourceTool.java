package cfh.test;


import java.awt.event.MouseEvent;

import cfh.editor.Element;
import cfh.editor.Tool;


public class SourceTool extends Tool {

    public SourceTool() {
        super("source", "external source");
    }
    
    @Override
    public void clicked(MouseEvent ev, double modelX, double modelY, Element over) {
        if (isButton1(ev)) {
            if (ev.getClickCount() == 2 && over != null) {
                // TODO
            } else {
                int x = (int) modelX;
                int y = (int) modelY;
                StringField nameField = new StringField("Name:", "", 20);
                DoubleField voltageField = new DoubleField("Volt: ", "0.0", 5);

                if (!parameterDialog("Source (" + x + "," + y + ")", nameField, voltageField))
                    return;
                
                Source element = new Source();
                element.name(nameField.value());
                element.voltage(voltageField.value());
                element.x(x);
                element.y(y);
                element.w(20);
                element.h(20);
                canvas.compound().add(element);
                repaint();
            }
        }
    }
}
