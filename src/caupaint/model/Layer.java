
package caupaint.model;
import java.util.ArrayList;
import javax.swing.*;

public class Layer implements LayerInterface{
    
    ArrayList<Shape> layerArrayList;
    
    public Layer() {
        layerArrayList = new ArrayList<Shape>();
    }
    
    public void addShape(Shape shape) {
        if (!layerArrayList.isEmpty()) {
            Shape lastShape = layerArrayList.get(layerArrayList.size() - 1);
            shape.setPositionX(lastShape.getPositionX() + 50);
            shape.setPositionY(lastShape.getPositionY() + 50);
        }
        
        layerArrayList.add(shape);
    }
    
    public void deleteShape(int index) {
        try {
            layerArrayList.remove(index);
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "삭제할 사각형이 없습니다", "오류!", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteLastShape() {
        this.deleteShape(layerArrayList.size() - 1);
    }
    public void clear() {
        layerArrayList.clear();
    }
    
    public ArrayList<Shape> getArrayList() {
        return this.layerArrayList;
    }
}
