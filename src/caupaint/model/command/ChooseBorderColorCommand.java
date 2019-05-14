
package caupaint.model.command;

import caupaint.model.*;

public class ChooseBorderColorCommand implements Command {
    
    Variable variable;
    
    public ChooseBorderColorCommand(Variable variable) {
        this.variable = variable;
    }
    
    @Override
    public void execute() {
        variable.chooseBorderColor();
    }
    
}
