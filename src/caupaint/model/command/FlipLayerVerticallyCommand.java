
package caupaint.model.command;
import caupaint.model.*;

public class FlipLayerVerticallyCommand implements Command {
    
    CanvasContainer canvasContainer;
    int index;
    
    public FlipLayerVerticallyCommand(CanvasContainer canvasContainer, int index) {
        this.canvasContainer = canvasContainer;
        this.index = index;
    }
    
    public void execute() {
        canvasContainer.flipLayerVertically(index);
    }
    
}
