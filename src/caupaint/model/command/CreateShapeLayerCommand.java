
package caupaint.model.command;

import caupaint.model.*;
import java.awt.Point;

public class CreateShapeLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    Point currentMousePosition;
    
    public CreateShapeLayerCommand(CanvasContainer canvasContainer, Variable variable, Point currentMousePosition) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.currentMousePosition = currentMousePosition;
    }
    
    public void execute() {
        canvasContainer.createNewLayer(variable.getShapeType(), variable.getMouseActionType(), variable.getRecentlyPressedMousePosition(), currentMousePosition, 
            variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getBackgroundType());
    }
    
}
