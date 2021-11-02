
package caupaint.model.command;

import caupaint.model.*;
import java.awt.event.MouseEvent;

public class SelectLayerByMousePointCommand implements Command {
    
    CanvasContainer canvasContainer;
    MouseEvent event;
    
    public SelectLayerByMousePointCommand(CanvasContainer canvasContainer, MouseEvent event) {
        this.canvasContainer = canvasContainer;
        this.event = event;
    }
    
    public void execute() {
        canvasContainer.setSelectedLayerIndex(canvasContainer.selectLayerByMousePoint(event.getPoint()));
    }
    
}
