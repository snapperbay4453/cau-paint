
package caupaint.model.command;
import caupaint.model.*;

public class ToggleSelectedLayerVisibleCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public ToggleSelectedLayerVisibleCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.toggleLayerIsVisible(canvasContainer.getSelectedLayerIndex());
    }
    
}
