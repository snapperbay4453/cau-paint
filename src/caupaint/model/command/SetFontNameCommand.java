
package caupaint.model.command;

import caupaint.model.*;
import caupaint.model.Enum.*;

public class SetFontNameCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    String name;
    
    public SetFontNameCommand(CanvasContainer canvasContainer, Variable variable, String name) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.name = name;
    }
    
    public void execute() {
        variable.setFontName(name);
        if ((canvasContainer.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
           canvasContainer.setLayerFont(canvasContainer.getSelectedLayerIndex(), variable.getFont());
        }
    }
    
}
