
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.BackgroundType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class InsertTextCommand implements Command {
    
    CanvasContainer canvasContainer;
    Color borderColor;
    Color backgroundColor;
    BasicStroke stroke;
    BackgroundType backgroundType;
    Font font;
    
    public InsertTextCommand(CanvasContainer canvasContainer, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, Font font) {
        this.canvasContainer = canvasContainer;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.stroke = stroke;
        this.backgroundType = backgroundType;
        this.font = font;
    }
    
    @Override
    public void execute() {
        canvasContainer.insertTextLayer(borderColor, backgroundColor, stroke, backgroundType, font);
    }
    
}
