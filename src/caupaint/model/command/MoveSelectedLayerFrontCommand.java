
package caupaint.model.command;
import caupaint.model.*;

public class MoveSelectedLayerFrontCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public MoveSelectedLayerFrontCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        if (canvasContainer.swapNearLayers(canvasContainer.getSelectedLayerIndex(), canvasContainer.getSelectedLayerIndex() - 1) == 0) canvasContainer.setSelectedLayerIndex(canvasContainer.getSelectedLayerIndex() - 1);
    }
    
}
