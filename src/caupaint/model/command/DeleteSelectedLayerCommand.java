
package caupaint.model.command;

import caupaint.model.*;

public class DeleteSelectedLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public DeleteSelectedLayerCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.deleteLayer(canvasContainer.getSelectedLayerIndex());
    }
    
}
