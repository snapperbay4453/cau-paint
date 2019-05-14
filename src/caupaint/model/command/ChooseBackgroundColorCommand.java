
package caupaint.model.command;

import caupaint.model.*;

public class ChooseBackgroundColorCommand implements Command {
    
    Variable variable;
    
    public ChooseBackgroundColorCommand(Variable variable) {
        this.variable = variable;
    }
    
    @Override
    public void execute() {
        variable.chooseBackgroundColor();
    }
    
}
