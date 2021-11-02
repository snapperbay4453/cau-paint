
package caupaint.model.command;

import caupaint.model.*;
import caupaint.model.Enum.*;

public class SetFontSizeCommand implements Command {
    
    CanvasContainer canvasContainer;
    Variable variable;
    int size;
    
    public SetFontSizeCommand(CanvasContainer canvasContainer, Variable variable, int size) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.size = size;
    }
    
    @Override
    public void execute() {
           variable.setFontSize(size);
           if ((canvasContainer.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
                canvasContainer.setLayerFont(canvasContainer.getSelectedLayerIndex(), variable.getFont());
           }
    }
    
}
