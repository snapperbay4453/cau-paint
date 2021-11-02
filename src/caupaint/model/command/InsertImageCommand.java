
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.BackgroundType;

import java.awt.BasicStroke;
import java.awt.Color;

public class InsertImageCommand implements Command {
    
    CanvasContainer canvasContainer;
    Color backgroundColor;
    BasicStroke stroke;
    BackgroundType backgroundType;
    
    public InsertImageCommand(CanvasContainer canvasContainer, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType) {
        this.canvasContainer = canvasContainer;
        this.backgroundColor = backgroundColor;
        this.stroke = stroke;
        this.backgroundType = backgroundType;
    }
    
    @Override
    public void execute() {
        canvasContainer.insertImageLayer(backgroundColor, stroke, backgroundType);
    }
    
}
