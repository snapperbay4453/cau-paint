
package caupaint.model.command;

import caupaint.model.*;

public class SetSelectedLayerIndexCommand implements Command {
    
    CanvasContainer canvasContainer;
    int index;
    
    public SetSelectedLayerIndexCommand(CanvasContainer canvasContainer, int index) {
        this.canvasContainer = canvasContainer;
        this.index = index;
    }
    
    public void execute() {
        canvasContainer.setSelectedLayerIndex(index);
    }
    
}
