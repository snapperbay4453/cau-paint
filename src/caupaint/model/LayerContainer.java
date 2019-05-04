
package caupaint.model;
import caupaint.observer.*;
import java.awt.Point;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class LayerContainer implements LayerContainerSubject{
    
    private ArrayList<ShapeLayer> layerArrayList; // Shape를 저장하는 ArrayList
    private Point recentMousePosition;
    
    private ArrayList<LayerContainerObserver> LayerContainerObserverArrayList = new ArrayList<LayerContainerObserver>(); // LayerContainer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public LayerContainer() {
        layerArrayList = new ArrayList<ShapeLayer>();
        recentMousePosition = new Point(0,0);
    }
    
    /*
    ** Shape 관련 메소드
    */
    public void addShapeLayer(ShapeLayer shapeLayer) {
        layerArrayList.add(shapeLayer);
        notifyLayerContainerObservers();
    }
    public void createShapeLayer(Point currentMousePosition) {
        layerArrayList.get(layerArrayList.size() - 1).create(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyLayerContainerObservers();
    }
    public void moveShapeLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            layerArrayList.get(index).translate(((int)currentMousePosition.getX() - (int)recentMousePosition.getX()), ((int)currentMousePosition.getY() - (int)recentMousePosition.getY()));
            recentMousePosition = currentMousePosition;
            notifyLayerContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resizeShapeLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        layerArrayList.get(index).scale(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyLayerContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void rotateShapeLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        layerArrayList.get(index).rotate(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyLayerContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void swapShapeLayer(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 두 레이어의 index를 서로 바꿈
        try {
            if (sourceIndex == -1 || destinationIndex == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer tempShapeLayer = layerArrayList.get(sourceIndex); // sourceIndex의 레이어 정보를 임시로 저장
            layerArrayList.set(sourceIndex, layerArrayList.get(destinationIndex)); // sourceIndex에 destinationIndex의 레이어 정보를 저장
            layerArrayList.set(destinationIndex, tempShapeLayer); // destinationIndex에 임시로 저장했던 sourceIndex의 레이어 정보를 저장
            notifyLayerContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteShapeLayer(int index) throws ArrayIndexOutOfBoundsException {
        try {
            if (layerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
                JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (index == -1) throw new ArrayIndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            if (JOptionPane.showConfirmDialog(null, "현재 도형을 삭제합니다.", "도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                layerArrayList.remove(index);
                notifyLayerContainerObservers();
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void clear() {
        if (layerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
            JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "모든 도형을 삭제합니다.", "모든 도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            layerArrayList.clear();
            notifyLayerContainerObservers();
        }
    }
    
    /*
    ** getter, setter
    */
    public ArrayList<ShapeLayer> getArrayList() {
        return this.layerArrayList;
    }
    public Vector<ShapeLayer> getVector() {  // ArrayList를 Vector 형식으로 반환하는 메소드
        Vector<ShapeLayer> layerVector = new Vector<ShapeLayer>();
        for (ShapeLayer shapeLayer : layerArrayList) {
            layerVector.add(shapeLayer);
        }
        return layerVector;
    }
    public ShapeLayer getShapeLayer(int index) {
        return layerArrayList.get(index);
    }
    public void setArrayList(ArrayList<ShapeLayer> layerArrayList) {
        this.layerArrayList = layerArrayList;
        notifyLayerContainerObservers();
    }
    public void setShapeLayer(int index, ShapeLayer shapeLayer) {
       layerArrayList.set(index, shapeLayer);
       notifyLayerContainerObservers();
    }
    public void setRecentMousePosition(Point point) {
        recentMousePosition = point;
    }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerLayerContainerObserver(LayerContainerObserver o) {
	LayerContainerObserverArrayList.add(o);
    }
    public void removeLayerContainerObserver(LayerContainerObserver o) {
	LayerContainerObserverArrayList.remove(o);
    }
    public void notifyLayerContainerObservers() {
	for (LayerContainerObserver o : LayerContainerObserverArrayList) {
		o.updateLayerContainer();
	}
    }
    
}
