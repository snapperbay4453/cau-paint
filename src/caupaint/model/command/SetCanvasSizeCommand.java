
package caupaint.model.command;
import caupaint.model.*;

public class SetCanvasSizeCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public SetCanvasSizeCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    public void execute() {
        canvasContainer.showSetCanvasSizeDialogBox();
    }
    
}
