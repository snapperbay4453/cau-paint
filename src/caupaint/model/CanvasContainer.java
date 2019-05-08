
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class CanvasContainer implements Serializable, CanvasContainerSubject{
    
    private ArrayList<ShapeLayer> shapeLayerArrayList; // Shape를 저장하는 ArrayList
    private Point canvasSize; // 캔버스의 너비와 높이를 저장
    private Color canvasBackgroundColor; // 캔버스의 배경색을 저장
    
    transient private ArrayList<CanvasContainerObserver> CanvasContainerObserverArrayList = new ArrayList<CanvasContainerObserver>(); // CanvasContainer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public CanvasContainer() {
        shapeLayerArrayList = new ArrayList<ShapeLayer>(); // 레이어들의 정보를 담는 ArrayList
        canvasSize = Constant.defaultCanvasSize; // 캔버스의 크기
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor; // 캔버스의 배경색
    }
    
    /*
    ** shapeLayerArrayList 조작 관련 메소드
    */
    public void addLayerToArrayList(ShapeLayer shapeLayer) { // 이미 생성된 레이어를 ArrayList에 추가
        shapeLayerArrayList.add(shapeLayer);
    }
    public void copyLayer(int index) throws IndexOutOfBoundsException{ // 선택한 레이어를 복제함
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
                case TEXT:
                    copiedShapeLayer = new TextLayer((TextLayer)shapeLayerArrayList.get(index));  break;  
                default: return;
            }
            shapeLayerArrayList.add(index + 1, copiedShapeLayer);
            shapeLayerArrayList.get(index + 1).setName(shapeLayerArrayList.get(index).getName() + Constant.defaultCopiedFileSuffix);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void swapNearLayers(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 인접한 두 레이어의 index를 서로 바꿈
        if (sourceIndex == -1) JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else if (sourceIndex <= 0 && destinationIndex <= 0) JOptionPane.showMessageDialog(null, "첫 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else if (sourceIndex >= getShapeLayerArrayList().size() - 1 && destinationIndex >= getShapeLayerArrayList().size() - 1) JOptionPane.showMessageDialog(null, "마지막 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else swapLayers(sourceIndex, destinationIndex);
    }
    public void deleteLayer(int index) throws ArrayIndexOutOfBoundsException { // 선택한 레이어를 삭제함
        try {
            if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
                JOptionPane.showMessageDialog(null, "삭제할 레이어가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (index == -1) throw new ArrayIndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            if (JOptionPane.showConfirmDialog(null, "현재 레이어를 삭제합니다.", "도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                shapeLayerArrayList.remove(index);
                notifyCanvasContainerObservers();
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteAllLayers() { // 모든 레이어를 삭제함
        if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
            JOptionPane.showMessageDialog(null, "삭제할 레이어가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "모든 레이어를 삭제합니다.", "모든 도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            shapeLayerArrayList.clear();
            notifyCanvasContainerObservers();
        }
    }

    
    /*
    ** ShapeLayer 생성 관련 메소드
    */
    public void createLayer(ShapeType shapetype, Point point, Color color, BasicStroke stroke, Font font, BackgroundType backgroundType) { // ShapeLayer 클래스를 상속하는 레이어를 생성
        switch(shapetype) {
             case LINE:
                shapeLayerArrayList.add(new LineLayer("새 직선", point, new Point(0, 0), color, stroke, backgroundType, 0, true));
                break;
            case RECTANGLE:
                shapeLayerArrayList.add(new RectangleLayer("새 직사각형", point, new Point(0, 0), color, stroke, backgroundType, 0, true));
                break;
             case ELLIPSE:
                shapeLayerArrayList.add(new EllipseLayer("새 타원", point, new Point(0, 0), color, stroke, backgroundType, 0, true));
                break;
             case TRIANGLE:
                shapeLayerArrayList.add(new TriangleLayer("새 삼각형", point, new Point(0, 0), color, stroke, backgroundType, 0, true));
                break;
             case RHOMBUS:
                shapeLayerArrayList.add(new RhombusLayer("새 마름모", point, new Point(0, 0), color, stroke, backgroundType, 0, true));
                break;
             case TEXT:
                shapeLayerArrayList.add(new TextLayer("새 텍스트", point, new Point(0, 0), color, stroke, backgroundType, 0, true, font));
                break;
        }
        notifyCanvasContainerObservers();
    }
    public void initializeLayer(Point currentMousePosition) { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 버튼을 눌러 도형 생성 및 위치 지정
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).initialize(currentMousePosition);
        notifyCanvasContainerObservers();
    }
    public void keepInitializingLayer(Point recentlyPressedMousePosition, Point currentMousePosition) { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 버튼을 누른 채로 드래그하여 크기 변형
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).keepInitializing(recentlyPressedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
    }
    public void finishInitializingLayer() { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 드래그를 완료하여 크기 지정 완료
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).finishInitializing();
        notifyCanvasContainerObservers();
    }
    
    /*
    ** ShapeLayer 변수 조작 관련 메소드
    */
    public void renameLayer(int index) { // 선택한 레이어의 name을 변경함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            String tempName = JOptionPane.showInputDialog(null, "새 이름을 입력하세요.", shapeLayerArrayList.get(index).getName());
            if (tempName == "") { // 새로 입력한 이름이 비어 있으면
                JOptionPane.showMessageDialog(null, "이름을 지정해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (tempName == null) return; // 취소 버튼을 누른 경우
            shapeLayerArrayList.get(index).setName(tempName);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void setLayerStroke (int index, BasicStroke stroke) { // 선택한 레이어의 stroke를 변경함
        shapeLayerArrayList.get(index).setStroke(stroke);
        notifyCanvasContainerObservers();
    }
    public void setLayerFont (int index, Font font) { // 선택한 레이어의 font를 변경함
        ((TextLayer)shapeLayerArrayList.get(index)).setFont(font);
        notifyCanvasContainerObservers();
    }
    public void toggleLayerIsVisible(int index) throws IndexOutOfBoundsException{ // 선택한 레이어의 isVisible을 변경함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).setIsVisible( ! shapeLayerArrayList.get(index).getIsVisible()); // isVisible 토글
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
    ** ShapeLayer 도형 변형 관련 메소드
    */
    public void moveLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            shapeLayerArrayList.get(index).translate(recentlyDraggedMousePosition, currentMousePosition);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resizeLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).scale(recentlyDraggedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void rotateLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).rotate(recentlyDraggedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
    ** Canvas 관련 메소드
    */
    public void showSetCanvasSizeDialogBox() {
        int tempWidth;  int tempHeight;
        try {
            tempWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 넓이를 입력하세요.",  (int)canvasSize.getX()));
            if (tempWidth <= 0) throw new IllegalArgumentException();
            tempHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 높이를 입력하세요.", (int)canvasSize.getY()));
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
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.add(o); }
    public void removeCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.remove(o); }
    public void notifyCanvasContainerObservers() { for (CanvasContainerObserver o : CanvasContainerObserverArrayList) { o.updateCanvasContainer(); } }
    
}
