
package caupaint.model;
import caupaint.observer.*;
import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class LayerContainer implements Serializable, LayerContainerSubject{
    
    private ArrayList<ShapeLayer> layerArrayList; // Shape를 저장하는 ArrayList
    private Point canvasSize;
    private Color canvasBackgroundColor;
    transient private Point recentMousePosition;
    
    transient private ArrayList<LayerContainerObserver> LayerContainerObserverArrayList = new ArrayList<LayerContainerObserver>(); // LayerContainer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public LayerContainer() {
        layerArrayList = new ArrayList<ShapeLayer>();
        canvasSize = Constant.defaultCanvasSize;
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
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
    public void renameShapeLayer(int index) {
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            String tempName = JOptionPane.showInputDialog(null, "새 이름을 입력하세요.", "이름 변경", JOptionPane.QUESTION_MESSAGE);
            if (tempName.isEmpty()) { // 새로 입력한 이름이 비어 있으면
                JOptionPane.showMessageDialog(null, "이름을 지정해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            layerArrayList.get(index).setName(tempName);
            notifyLayerContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }

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
    public void toggleSelectedLayerVisible(int index) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        layerArrayList.get(index).setIsVisible( ! layerArrayList.get(index).getIsVisible()); // isVisible 토글
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
    public void copyShapeLayer(int index) throws IndexOutOfBoundsException{ // 레이어를 복제함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer tempShapeLayer;
            switch(layerArrayList.get(index).getRealShapeType()) {
                case LINE:
                    tempShapeLayer = new LineLayer((LineLayer)layerArrayList.get(index));
                    break;
                case RECTANGLE:
                    tempShapeLayer = new RectangleLayer((RectangleLayer)layerArrayList.get(index));
                    break;
                case ELLIPSE:
                    tempShapeLayer = new EllipseLayer((EllipseLayer)layerArrayList.get(index));
                    break;
                case TRIANGLE:
                    tempShapeLayer = new TriangleLayer((TriangleLayer)layerArrayList.get(index));
                    break;
                case RHOMBUS:
                    tempShapeLayer = new RhombusLayer((RhombusLayer)layerArrayList.get(index));
                    break;
                default:
                    tempShapeLayer = new ShapeLayer((ShapeLayer)layerArrayList.get(index));
                    break;
            }

            layerArrayList.add(index + 1, tempShapeLayer);
            layerArrayList.get(index + 1).setName(layerArrayList.get(index).getName() + Constant.defaultCopiedFileSuffix);
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
    public void clearShapeLayers() {
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
    ** Canvas 관련 메서드
    */
    public void showSetCanvasSizeDialogBox() {
        int tempWidth;  int tempHeight;
        try {
            tempWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 넓이를 입력하세요.", "넓이 입력", JOptionPane.QUESTION_MESSAGE));
            if (tempWidth <= 0) throw new IllegalArgumentException();
            tempHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 높이를 입력하세요.", "높이 입력", JOptionPane.QUESTION_MESSAGE));
            if (tempHeight <= 0) throw new IllegalArgumentException();
            canvasSize = new Point(tempWidth, tempHeight);
        } catch (NumberFormatException exp){
            JOptionPane.showMessageDialog(null, "잘못된 값이 입력되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exp){
            JOptionPane.showMessageDialog(null, "크기는 0보다 큰 수만 지정할 수 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        notifyLayerContainerObservers();
    }
    public void showSetCanvasBackgroundColorDialogBox() {
        JColorChooser chooser=new JColorChooser();
        canvasBackgroundColor = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyLayerContainerObservers();
    }
    public void clearCanvas() {
        canvasSize = Constant.defaultCanvasSize;
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
        notifyLayerContainerObservers();
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
    public Point getCanvasSize() {
        return canvasSize;
    }
    public Color getCanvasBackgroundColor() {
        return canvasBackgroundColor;
    }
    public void setArrayList(ArrayList<ShapeLayer> layerArrayList) {
        this.layerArrayList = layerArrayList;
        notifyLayerContainerObservers();
    }
    public void setShapeLayer(int index, ShapeLayer shapeLayer) {
       layerArrayList.set(index, shapeLayer);
       notifyLayerContainerObservers();
    }
    public void setCanvasSize(Point size) {
        this.canvasSize = size;
        notifyLayerContainerObservers();
    }
    public void setCanvasBackgroundColor(Color color) {
        this.canvasBackgroundColor = color;
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
