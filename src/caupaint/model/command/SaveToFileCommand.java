
package caupaint.model.command;
import caupaint.model.*;
import caupaint.view.MainView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveToFileCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public SaveToFileCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        try {
            canvasContainer.saveLayersToFile(canvasContainer.getFilePathToSave());
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
