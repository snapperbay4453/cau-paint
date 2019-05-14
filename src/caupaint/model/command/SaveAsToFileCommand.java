
package caupaint.model.command;

import caupaint.model.*;
import caupaint.view.MainView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaveAsToFileCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public SaveAsToFileCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        try {
            canvasContainer.saveLayersToFile(canvasContainer.getNewFilePathToSave());
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
