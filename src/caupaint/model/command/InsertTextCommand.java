
package caupaint.model.command;

import caupaint.model.*;

public class InsertTextCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    
    public InsertTextCommand(CanvasContainer canvasContainer, Variable variable) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
    }
    
    @Override
    public void execute() {
        canvasContainer.insertTextLayer(variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getBackgroundType(), variable.getFont());
    }
    
}
