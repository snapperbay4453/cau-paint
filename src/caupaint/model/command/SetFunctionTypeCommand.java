
package caupaint.model.command;

import caupaint.model.*;
import caupaint.model.Enum.*;

public class SetFunctionTypeCommand implements Command {
    
    Variable variable;
    FunctionType functionType;
    
    public SetFunctionTypeCommand(Variable variable, FunctionType functionType) {
        this.variable = variable;
        this.functionType = functionType;
    }
    
    public void execute() {
        variable.setFunctionType(functionType);
    }
    
}
