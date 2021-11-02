
package caupaint.model.command;
import caupaint.model.*;
import javax.swing.JOptionPane;

public class RenameSelectedLayerCommand implements Command {
    
    CanvasContainer canvasContainer;
    
    public RenameSelectedLayerCommand(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
    }
    
    @Override
    public void execute() {
        try {
            if (canvasContainer.getSelectedLayerIndex() == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            String tempName = JOptionPane.showInputDialog(null, "새 이름을 입력하세요.", canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getName());
            if (tempName == "") { // 새로 입력한 이름이 비어 있으면
                JOptionPane.showMessageDialog(null, "이름을 지정해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (tempName == null) return; // 취소 버튼을 누른 경우
            canvasContainer.renameLayer(canvasContainer.getSelectedLayerIndex(), tempName);
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        //canvasContainer.renameLayer(canvasContainer.getSelectedLayerIndex());
    }
    
}
