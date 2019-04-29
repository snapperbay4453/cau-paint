
package caupaint.model;
import caupaint.observer.*;

import java.util.ArrayList;
import javax.swing.*;

public class Layer implements LayerInterface, LayerSubject{
    
    ArrayList<Shape> layerArrayList;
    
    ArrayList<LayerObserver> LayerObserverArrayList = new ArrayList<LayerObserver>();
    
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
        notifyLayerObservers();
    }
    public void deleteShape(int index) {
        try {
            layerArrayList.remove(index);
            notifyLayerObservers();
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "삭제할 사각형이 없습니다", "오류!", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteLastShape() {
        this.deleteShape(layerArrayList.size() - 1);
    }
    public void clear() {
        layerArrayList.clear();
        notifyLayerObservers();
    }
    
    public ArrayList<Shape> getArrayList() {
        return this.layerArrayList;
    }
    
    public void registerLayerObserver(LayerObserver o) {
	LayerObserverArrayList.add(o);
    }
    public void removeLayerObserver(LayerObserver o) {
	LayerObserverArrayList.remove(o);
    }
    public void notifyLayerObservers() {
	for (LayerObserver o : LayerObserverArrayList) {
		o.updateLayer();
	}
    }
    
}
