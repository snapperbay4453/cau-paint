
package caupaint.model.command;
import caupaint.model.*;

public class CreateNewCanvasCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public CreateNewCanvasCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        canvasContainer.createNewCanvas();
    }
    
}
