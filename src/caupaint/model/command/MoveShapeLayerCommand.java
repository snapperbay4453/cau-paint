
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.*;

import java.awt.Point;

public class MoveShapeLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    int index;
    MouseActionType mouseActionType;
    Point recentlyDraggedMousePosition;
    Point currentMousePosition;
    
    public MoveShapeLayerCommand(CanvasContainer canvasContainer, int index, MouseActionType mouseActionType, Point recentlyDraggedMousePosition, Point currentMousePosition) {
        this.canvasContainer = canvasContainer;
        this.index = index;
        this.mouseActionType = mouseActionType;
        this.recentlyDraggedMousePosition = recentlyDraggedMousePosition;
        this.currentMousePosition = currentMousePosition;
    }
    
    public void execute() {
        canvasContainer.moveLayer(index, mouseActionType, recentlyDraggedMousePosition, currentMousePosition);
    }
    
}
