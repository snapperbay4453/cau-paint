
package caupaint.model.command;
import caupaint.model.*;
import caupaint.model.Enum.*;

public class SetBackgroundTypeCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    BackgroundType backgroundType;
    
    public SetBackgroundTypeCommand(CanvasContainer canvasContainer, Variable variable, BackgroundType backgroundType) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.backgroundType = backgroundType;
    }
    
    @Override
    public void execute() {
        variable.setBackgroundType(backgroundType);
            if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.changeLayerBackgroundTypeAndColors(canvasContainer.getSelectedLayerIndex(), variable.getBackgroundType(), variable.getBorderColor(), variable.getBackgroundColor());
    }
    
}
