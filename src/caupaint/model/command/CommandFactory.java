
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class CommandFactory {
    
    public static Command create(String commandName, CanvasContainer canvasContainer, Variable variable, EventObject event, Object args) {
        
        switch (commandName) {
            case "createNewCanvas": return new CreateNewCanvasCommand(canvasContainer);
            case "loadFromFile": return new LoadFromFileCommand(canvasContainer);
            case "saveToFile": return new SaveToFileCommand(canvasContainer);
            case "saveAsToFile": return new SaveAsToFileCommand(canvasContainer);
            
            case "setCanvasSize": return new SetCanvasSizeCommand(canvasContainer);
            case "setCanvasBackgroundColor": return new SetCanvasBackgroundColorCommand(canvasContainer);
            
            case "drawPolyline": return new SetShapeTypeCommand(variable, ShapeType.POLYLINE);
            case "drawPen": return new SetShapeTypeCommand(variable, ShapeType.PEN);
            case "drawRectangle": return new SetShapeTypeCommand(variable, ShapeType.RECTANGLE);
            case "drawEllipse": return new SetShapeTypeCommand(variable, ShapeType.ELLIPSE);
            case "drawTriangle": return new SetShapeTypeCommand(variable, ShapeType.TRIANGLE);
            case "drawRhombus": return new SetShapeTypeCommand(variable, ShapeType.RHOMBUS);
            
            case "insertText": return new InsertTextCommand(canvasContainer, variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getBackgroundType(), variable.getFont());
            case "insertImage": return new InsertImageCommand(canvasContainer, variable.getBackgroundColor(), variable.getStroke(), variable.getBackgroundType());
            
            case "selectShape": return new SetFunctionTypeCommand(variable, FunctionType.SELECT);
            case "moveShape": return new SetFunctionTypeCommand(variable, FunctionType.MOVE);
            case "resizeShape": return new SetFunctionTypeCommand(variable, FunctionType.RESIZE);
            case "rotateShape": return new SetFunctionTypeCommand(variable, FunctionType.ROTATE);
            case "freeTransformShape": return new SetFunctionTypeCommand(variable, FunctionType.FREE_TRANSFORM);
            
            case "chooseBorderColor": return new ChooseBorderColorCommand(variable);
            case "chooseBackgroundColor": return new ChooseBackgroundColorCommand(variable);
            
            case "emptyBackgroundType": return new SetBackgroundTypeCommand(canvasContainer, variable, BackgroundType.EMPTY);
            case "fillBackgroundType": return new SetBackgroundTypeCommand(canvasContainer, variable, BackgroundType.FILL);
            
            case "setStrokeByName": return new SetStrokeByNameCommand(canvasContainer, variable, (String)args);
            case "setStrokeWidth": return new SetStrokeWidthCommand(canvasContainer, variable, (int)args);
            case "setFontName": return new SetFontNameCommand(canvasContainer, variable, (String)args);
            case "setFontSize": return new SetFontSizeCommand(canvasContainer, variable, (int)args);
            
            case "toggleSelectedLayerVisible": return new ToggleSelectedLayerVisibleCommand(canvasContainer);
            case "moveSelectedLayerFront": return new MoveSelectedLayerFrontCommand(canvasContainer);
            case "moveSelectedLayerBack": return new MoveSelectedLayerBackCommand(canvasContainer);
            case "renameSelectedLayer": return new RenameSelectedLayerCommand(canvasContainer);
            case "copySelectedLayer": return new CopySelectedLayerCommand(canvasContainer);
            case "deleteSelectedLayer": return new DeleteSelectedLayerCommand(canvasContainer);
            case "deleteAllLayer": return new DeleteAllLayerCommand(canvasContainer);
            
            case "setSelectedLayerIndex": return new SetSelectedLayerIndexCommand(canvasContainer, (int)args);
            
            case "createNewShapeLayer": return new CreateShapeLayerCommand(canvasContainer, variable, ((MouseEvent)event).getPoint());
            case "selectLayerByMousePoint": return new SelectLayerByMousePointCommand(canvasContainer, (MouseEvent)event);
            case "moveShapeLayer": return new MoveShapeLayerCommand(canvasContainer, canvasContainer.getSelectedLayerIndex(), variable.getMouseActionType(), variable.getRecentlyDraggedMousePosition(), ((MouseEvent)event).getPoint());
            case "resizeShapeLayer": return new ResizeShapeLayerCommand(canvasContainer, canvasContainer.getSelectedLayerIndex(), variable.getMouseActionType(), variable.getRecentlyPressedMousePosition(), variable.getRecentlyDraggedMousePosition(), ((MouseEvent)event).getPoint());
            case "rotateShapeLayer": return new RotateShapeLayerCommand(canvasContainer, canvasContainer.getSelectedLayerIndex(), variable.getMouseActionType(), variable.getRecentlyDraggedMousePosition(), ((MouseEvent)event).getPoint());
            case "flipLayerHorizontally": return new FlipLayerHorizontallyCommand(canvasContainer, canvasContainer.getSelectedLayerIndex());
            case "flipLayerVertically": return new FlipLayerVerticallyCommand(canvasContainer, canvasContainer.getSelectedLayerIndex());
            case "freeTransformShapeLayer": return new FreeTransformShapeLayerCommand(canvasContainer, canvasContainer.getSelectedLayerIndex(), variable.getMouseActionType(), variable.getRecentlyPressedMousePosition(), variable.getRecentlyDraggedMousePosition(), ((MouseEvent)event).getPoint());
            
            
            case "checkExit": return new CheckExitCommand(canvasContainer);
            
            default: return null;
        }
    }
}
