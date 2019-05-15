
package caupaint.model.command;
import caupaint.model.*;

public class FlipLayerHorizontallyCommand implements Command {
    
    CanvasContainer canvasContainer;
    int index;
    
    public FlipLayerHorizontallyCommand(CanvasContainer canvasContainer, int index) {
        this.canvasContainer = canvasContainer;
        this.index = index;
    }
    
    public void execute() {
        canvasContainer.flipLayerHorizontally(index);
    }
    
}
