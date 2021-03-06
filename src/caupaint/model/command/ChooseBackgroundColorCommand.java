
package caupaint.model.command;
import caupaint.model.*;

import java.awt.Color;
import javax.swing.JColorChooser;

public class ChooseBackgroundColorCommand implements Command {
    
    Variable variable;
    
    public ChooseBackgroundColorCommand(Variable variable) {
        this.variable = variable;
    }
    
    @Override
    public void execute() {
        Color tempColor = new Color(0, 0, 0);
        tempColor = new JColorChooser().showDialog(null,"Color",Color.YELLOW);
        if (tempColor != null) variable.setBackgroundColor(tempColor);
        //variable.chooseBorderColor();
    }
    
}
