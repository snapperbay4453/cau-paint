
package caupaint.model.command;

import caupaint.model.*;

public class RenameSelectedLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public RenameSelectedLayerCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.renameLayer(canvasContainer.getSelectedLayerIndex());
    }
    
}
