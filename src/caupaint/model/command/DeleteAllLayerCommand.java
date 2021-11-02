
package caupaint.model.command;

import caupaint.model.*;

public class DeleteAllLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public DeleteAllLayerCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.deleteAllLayers();
    }
    
}
