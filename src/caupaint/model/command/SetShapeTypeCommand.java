
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.*;

public class SetShapeTypeCommand implements Command {
    
    Variable variable;
    ShapeType shapeType;
    
    public SetShapeTypeCommand(Variable variable, ShapeType shapeType) {
        this.variable = variable;
        this.shapeType = shapeType;
    }
    
    @Override
    public void execute() {
        variable.setFunctionType(FunctionType.DRAW);
        variable.setShapeType(shapeType);
    }
    
}
