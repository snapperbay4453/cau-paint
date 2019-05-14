
package caupaint.model.command;

import caupaint.model.*;
import java.awt.event.ItemEvent;
import javax.swing.event.ChangeEvent;

public class SetStrokeWidthCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    int width;
    
    public SetStrokeWidthCommand(CanvasContainer canvasContainer, Variable variable, int width) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.width = width;
    }
    
    public void execute() {
       variable.setStrokeWidth(width);
       if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(canvasContainer.getSelectedLayerIndex(), variable.getStroke());
    }
    
}
