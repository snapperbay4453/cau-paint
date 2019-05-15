
package caupaint.model.command;
import caupaint.model.*;
import caupaint.view.MainView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadFromFileCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public LoadFromFileCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        try {
            canvasContainer.loadLayersFromFile(canvasContainer.getFilePathToOpen());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
