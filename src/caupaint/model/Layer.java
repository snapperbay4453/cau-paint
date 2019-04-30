
package caupaint.model;
import caupaint.observer.*;
import java.awt.Point;

import java.util.ArrayList;
import javax.swing.*;

public class Layer implements LayerSubject{
    
    ArrayList<Shape> layerArrayList; // Shape를 저장하는 ArrayList
    
    ArrayList<LayerObserver> LayerObserverArrayList = new ArrayList<LayerObserver>(); // Layer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public Layer() {
        layerArrayList = new ArrayList<Shape>();
    }
    
    /*
    ** Shape 관련 메소드
    */
    public void addShape(Shape shape) {
        layerArrayList.add(shape);
        notifyLayerObservers();
    }
    public void addRectangle(Point position, Point size) {
        layerArrayList.add(new Rectangle(position, size));
        notifyLayerObservers();
    }
    public void modifyShapeSizeAbsolute(Point point) {
        Shape lastShape = layerArrayList.get(layerArrayList.size() - 1);
        lastShape.setSize(new Point((int)point.getX() - lastShape.getPositionX(), (int)point.getY() - lastShape.getPositionY()));
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
    
    /*
    ** draw 관련 메소드
    */
    public void drawShape(Shape shape, Point pointStart, Point pointEnd) {
        
    }
    
    /*
    ** getter, setter
    */
    public ArrayList<Shape> getArrayList() {
        return this.layerArrayList;
    }
    public Shape getShape(int index) {
        return layerArrayList.get(index);
    }
    public void setArrayList(ArrayList<Shape> layerArrayList) {
        this.layerArrayList = layerArrayList;
        notifyLayerObservers();
    }
    public void setShape(int index, Shape shape) {
       layerArrayList.set(index, shape);
       notifyLayerObservers();
    }
    
    /*
    ** 옵저버 관련 메소드
    */
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
