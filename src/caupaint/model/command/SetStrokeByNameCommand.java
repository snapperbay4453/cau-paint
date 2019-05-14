
package caupaint.model.command;

import caupaint.model.*;

public class SetStrokeByNameCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    String name;
    
    public SetStrokeByNameCommand(CanvasContainer canvasContainer, Variable variable, String name) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.name = name;
    }
    
    public void execute() {
        variable.setStrokeByName(name);
        if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(canvasContainer.getSelectedLayerIndex(), variable.getStroke());
    }
    
}
