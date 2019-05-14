
package caupaint.model.command;

import caupaint.model.*;

public class InsertImageCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    
    public InsertImageCommand(CanvasContainer canvasContainer, Variable variable) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
    }
    
    @Override
    public void execute() {
        canvasContainer.insertImageLayer();
    }
    
}
