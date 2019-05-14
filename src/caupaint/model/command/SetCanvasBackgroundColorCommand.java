
package caupaint.model.command;

import caupaint.model.*;

public class SetCanvasBackgroundColorCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public SetCanvasBackgroundColorCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    public void execute() {
        canvasContainer.showSetCanvasBackgroundColorDialogBox();
    }
    
}
