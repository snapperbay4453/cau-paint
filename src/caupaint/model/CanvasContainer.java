
package caupaint.model;
import caupaint.observer.*;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class CanvasContainer implements Serializable, CanvasContainerSubject{
    
    private ArrayList<ShapeLayer> shapeLayerArrayList; // Shape를 저장하는 ArrayList
    private Point canvasSize; // 캔버스의 너비와 높이를 저장
    private Color canvasBackgroundColor; // 캔버스의 배경색을 저장
    transient private Point recentMousePosition;
    
    transient private ArrayList<CanvasContainerObserver> CanvasContainerObserverArrayList = new ArrayList<CanvasContainerObserver>(); // CanvasContainer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public CanvasContainer() {
        shapeLayerArrayList = new ArrayList<ShapeLayer>();
        canvasSize = Constant.defaultCanvasSize;
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
        recentMousePosition = new Point(0,0);
    }
    
    /*
    ** ShapeLayer 관련 메소드
    */
    public void addLayer(ShapeLayer shapeLayer) { // ShapeLayer 클래스를 상속하는 레이어를 추가
        shapeLayerArrayList.add(shapeLayer);
        notifyCanvasContainerObservers();
    }
    public void renameLayer(int index) { // 
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            String tempName = JOptionPane.showInputDialog(null, "새 이름을 입력하세요.", "이름 변경", JOptionPane.QUESTION_MESSAGE);
            if (tempName.isEmpty()) { // 새로 입력한 이름이 비어 있으면
                JOptionPane.showMessageDialog(null, "이름을 지정해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            shapeLayerArrayList.get(index).setName(tempName);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void createLayer(Point currentMousePosition) { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 버튼을 누른 채로 드래그하여 초기 크기 정보 저장
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).create(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyCanvasContainerObservers();
    }
    public void moveLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            shapeLayerArrayList.get(index).translate(((int)currentMousePosition.getX() - (int)recentMousePosition.getX()), ((int)currentMousePosition.getY() - (int)recentMousePosition.getY()));
            recentMousePosition = currentMousePosition;
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resizeLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).scale(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void rotateLayer(int index, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).rotate(recentMousePosition, currentMousePosition);
        recentMousePosition = currentMousePosition;
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void toggleSelectedLayerVisible(int index) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).setIsVisible( ! shapeLayerArrayList.get(index).getIsVisible()); // isVisible 토글
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void swapLayers(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 두 레이어의 index를 서로 바꿈
        try {
            if (sourceIndex == -1 || destinationIndex == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer tempShapeLayer = shapeLayerArrayList.get(sourceIndex); // sourceIndex의 레이어 정보를 임시로 저장
            shapeLayerArrayList.set(sourceIndex, shapeLayerArrayList.get(destinationIndex)); // sourceIndex에 destinationIndex의 레이어 정보를 저장
            shapeLayerArrayList.set(destinationIndex, tempShapeLayer); // destinationIndex에 임시로 저장했던 sourceIndex의 레이어 정보를 저장
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void swapNearLayers(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 인접한 두 레이어의 index를 서로 바꿈
        if (sourceIndex <= 0 && destinationIndex <= 0) JOptionPane.showMessageDialog(null, "첫 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else if (sourceIndex >= getShapeLayerArrayList().size() - 1 && destinationIndex >= getShapeLayerArrayList().size() - 1) JOptionPane.showMessageDialog(null, "마지막 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else swapLayers(sourceIndex, destinationIndex);
    }
    public void copyLayer(int index) throws IndexOutOfBoundsException{ // 레이어를 복제함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer copiedShapeLayer;
            switch(shapeLayerArrayList.get(index).getRealShapeType()) { // Shape 객체의 실제 형식에 따라 다른 복제 생성자를 호출함
                case LINE:
                    copiedShapeLayer = new LineLayer((LineLayer)shapeLayerArrayList.get(index));    break;
                case RECTANGLE:
                    copiedShapeLayer = new RectangleLayer((RectangleLayer)shapeLayerArrayList.get(index));  break;
                case ELLIPSE:
                    copiedShapeLayer = new EllipseLayer((EllipseLayer)shapeLayerArrayList.get(index));  break;
                case TRIANGLE:
                    copiedShapeLayer = new TriangleLayer((TriangleLayer)shapeLayerArrayList.get(index));    break;
                case RHOMBUS:
                    copiedShapeLayer = new RhombusLayer((RhombusLayer)shapeLayerArrayList.get(index));  break;
                default:
                    copiedShapeLayer = new ShapeLayer((ShapeLayer)shapeLayerArrayList.get(index));  break;
            }
            shapeLayerArrayList.add(index + 1, copiedShapeLayer);
            shapeLayerArrayList.get(index + 1).setName(shapeLayerArrayList.get(index).getName() + Constant.defaultCopiedFileSuffix);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteLayer(int index) throws ArrayIndexOutOfBoundsException {
        try {
            if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
                JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (index == -1) throw new ArrayIndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            if (JOptionPane.showConfirmDialog(null, "현재 도형을 삭제합니다.", "도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                shapeLayerArrayList.remove(index);
                notifyCanvasContainerObservers();
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "도형이 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteAllLayers() {
        if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
            JOptionPane.showMessageDialog(null, "삭제할 도형이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "모든 도형을 삭제합니다.", "모든 도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            shapeLayerArrayList.clear();
            notifyCanvasContainerObservers();
        }
    }

    /*
    ** Canvas 관련 메소드
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
        notifyCanvasContainerObservers();
    }
    public void showSetCanvasBackgroundColorDialogBox() {
        JColorChooser chooser=new JColorChooser();
        canvasBackgroundColor = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyCanvasContainerObservers();
    }
    public void clearCanvas() {
        canvasSize = Constant.defaultCanvasSize;
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
        notifyCanvasContainerObservers();
    }
    
    /*
    ** getter, setter
    */
    public ArrayList<ShapeLayer> getShapeLayerArrayList() { return this.shapeLayerArrayList; }
    public Vector<ShapeLayer> getShapeLayerArrayListToVector() {  // ArrayList를 Vector 형식으로 반환하는 메소드
        Vector<ShapeLayer> layerVector = new Vector<ShapeLayer>();
        for (ShapeLayer shapeLayer : shapeLayerArrayList) {
            layerVector.add(shapeLayer);
        }
        return layerVector;
    }
    public Point getCanvasSize() { return canvasSize; }
    public Color getCanvasBackgroundColor() {
        return canvasBackgroundColor;
    }
    public void setArrayList(ArrayList<ShapeLayer> shapeLayerArrayList) { this.shapeLayerArrayList = shapeLayerArrayList; notifyCanvasContainerObservers(); }
    public void setShapeLayer(int index, ShapeLayer shapeLayer) { shapeLayerArrayList.set(index, shapeLayer); notifyCanvasContainerObservers(); }
    public void setCanvasSize(Point size) { this.canvasSize = size; notifyCanvasContainerObservers(); }
    public void setCanvasBackgroundColor(Color color) { this.canvasBackgroundColor = color; notifyCanvasContainerObservers(); }
    public void setRecentMousePosition(Point point) { recentMousePosition = point; }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.add(o); }
    public void removeCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.remove(o); }
    public void notifyCanvasContainerObservers() { for (CanvasContainerObserver o : CanvasContainerObserverArrayList) { o.updateCanvasContainer(); } }
    
}
