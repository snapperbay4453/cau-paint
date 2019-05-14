
package caupaint.model.command;

import caupaint.model.*;
import javax.swing.JOptionPane;

public class CheckExitCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public CheckExitCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    public void execute() {
        if (canvasContainer.getShapeLayerArrayList().isEmpty() == false) { // 레이어가 하나라도 남아있을 경우
            switch ((JOptionPane.showConfirmDialog(null, "캔버스에 도형이 남아있습니다.\n정말 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                break;
            }
               }
        else System.exit(0); // 레이어가 비어있을 경우
    }
    
}
