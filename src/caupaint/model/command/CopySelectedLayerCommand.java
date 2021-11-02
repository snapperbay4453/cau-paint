
package caupaint.model.command;

import caupaint.model.*;

public class CopySelectedLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public CopySelectedLayerCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.copyLayer(canvasContainer.getSelectedLayerIndex());
    }
    
}
