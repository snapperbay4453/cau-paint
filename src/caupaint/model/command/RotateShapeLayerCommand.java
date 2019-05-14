
package caupaint.model.command;

import caupaint.model.*;
import caupaint.model.Enum.*;
import java.awt.Point;

public class RotateShapeLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    int index;
    MouseActionType mouseActionType;
    Point recentlyDraggedMousePosition;
    Point currentMousePosition;
    
    public RotateShapeLayerCommand(CanvasContainer canvasContainer, int index, MouseActionType mouseActionType, Point recentlyDraggedMousePosition, Point currentMousePosition) {
        this.canvasContainer = canvasContainer;
        this.index = index;
        this.mouseActionType = mouseActionType;
        this.recentlyDraggedMousePosition = recentlyDraggedMousePosition;
        this.currentMousePosition = currentMousePosition;
    }
    
    public void execute() {
        canvasContainer.modifyLayer(index, FunctionType.ROTATE, mouseActionType, recentlyDraggedMousePosition, currentMousePosition);
    }
    
}
