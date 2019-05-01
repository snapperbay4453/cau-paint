
package caupaint.model;
import caupaint.observer.*;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class Layer implements LayerSubject{
    
    private ArrayList<Shape> layerArrayList; // Shape를 저장하는 ArrayList
    private Point recentMousePosition;
    
    private ArrayList<LayerObserver> LayerObserverArrayList = new ArrayList<LayerObserver>(); // Layer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public Layer() {
        layerArrayList = new ArrayList<Shape>();
        recentMousePosition = new Point(0,0);
    }
    
    /*
    ** Shape 관련 메소드
    */
    public void addShape(Shape shape) {
        layerArrayList.add(shape);
        notifyLayerObservers();
    }
    public void moveShape(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            layerArrayList.get(index).setPositionX(layerArrayList.get(index).getPositionX() + ((int)currentMousePosition.getX() - (int)recentMousePosition.getX()));
            layerArrayList.get(index).setPositionY(layerArrayList.get(index).getPositionY() + ((int)currentMousePosition.getY() - (int)recentMousePosition.getY()));
            recentMousePosition = currentMousePosition;
            notifyLayerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteShape(int index) throws ArrayIndexOutOfBoundsException {
        try {
            if (layerArrayList.size() == 0){ // 삭제할 도형이 없을 경우 예외 호출
                JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (index == -1) throw new ArrayIndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            if (JOptionPane.showConfirmDialog(null, "현재 도형을 삭제합니다.", "도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                layerArrayList.remove(index);
                notifyLayerObservers();
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void clear() {
        if (layerArrayList.size() == 0){ // 삭제할 도형이 없을 경우 예외 호출
            JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "모든 도형을 삭제합니다.", "모든 도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            layerArrayList.clear();
            notifyLayerObservers();
        }
    }
    
    /*
    ** getter, setter
    */
    public ArrayList<Shape> getArrayList() {
        return this.layerArrayList;
    }
    public Vector<Shape> getVector() {  // ArrayList를 Vector 형식으로 반환하는 메소드
        Vector<Shape> layerVector = new Vector<Shape>();
        for (Shape shape : layerArrayList) {
            layerVector.add(shape);
        }
        return layerVector;
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
    public void setRecentMousePosition(Point point) {
        recentMousePosition = point;
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
