
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;
import java.awt.Color;
import java.awt.Point;
import java.util.Vector;
import javax.swing.*;

public class Controller{
    
    private LayerContainer layerContainer;
    private Variable variable;
    private View view;
    
    /*
    ** 생성자
    */
    public Controller() {
        layerContainer = new LayerContainer();
        variable = new Variable(this);
        view = new View(layerContainer, variable, this);
        view.createView();
    }
    
    /*
    ** Layer, Shape 관련 메소드
    */
    public void addShapeLayer(ShapeLayer shapeLayer) {
        layerContainer.addShapeLayer(shapeLayer);
    }
    public void createShapeLayer(Point mousePosition) {
        layerContainer.createShapeLayer(mousePosition);
    }
    public void moveShapeLayer(int index, Point point) {
        layerContainer.moveShapeLayer(index, point);
    }
    public void resizeShapeLayer(int index, Point point) {
        layerContainer.resizeShapeLayer(index, point);
    }
    public void rotateShapeLayer(int index, Point point) {
        layerContainer.rotateShapeLayer(index, point);
    }
    public void swapShapeLayer(int sourceIndex, int destinationIndex) {
        if (sourceIndex <= 0 && destinationIndex <= 0) JOptionPane.showMessageDialog(null, "첫 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else if (sourceIndex >= layerContainer.getArrayList().size() - 1 && destinationIndex >= layerContainer.getArrayList().size() - 1) JOptionPane.showMessageDialog(null, "마지막 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else layerContainer.swapShapeLayer(sourceIndex, destinationIndex);
    }
    public void deleteShapeLayer(int index) {
        layerContainer.deleteShapeLayer(index);
    }
    public void clearLayer() {
        layerContainer.clear();
    }
    public Vector<ShapeLayer> getLayerArrayListToVector(){ // 사이드바에 Layer의 정보를 표시하기 위해 ArrayList를 Vector로 바꿔 반환하는 함수
        return layerContainer.getVector();
    }
    
    /*
    ** Variable 관련 메소드
    */
    public void chooseColor() {
        variable.chooseColor();
    }
    public void setCanvasSize(){
        variable.setCanvasSize();
    }
    public void setCanvasBackgroundColor(){
        variable.setCanvasBackgroundColor();
    }
    
    public Point getCanvasSize(){
        return variable.getCanvasSize();
    }
    public Color getCanvasBackgroundColor(){
        return variable.getCanvasBackgroundColor();
    }
    public void setPointStart(Point point){
        variable.setPointStart(point);
    }
    public void setPointEnd(Point point){
        variable.setPointEnd(point);
    }
    public void setLastSelectedLayerIndex(int index) {
        variable.setLastSelectedLayerIndex(index);
    }
    
    /*
    ** Canvas 관련 메소드
    */
    public void CanvasMousePressed(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                switch(variable.getShapeType()) {
                     case LINE:
                        addShapeLayer(new LineLayer(mousePosition, (new Point((int)mousePosition.getX() + 1, (int)mousePosition.getY()+ 1)), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                    case RECTANGLE:
                        addShapeLayer(new RectangleLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case ELLIPSE:
                        addShapeLayer(new EllipseLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case TRIANGLE:
                        addShapeLayer(new TriangleLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case RHOMBUS:
                        addShapeLayer(new RhombusLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                }
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case MOVE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case RESIZE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case ROTATE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            default:
                break;
        }
    }
    public void CanvasMouseReleased(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
            case MOVE:
            case RESIZE:
            case ROTATE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            default:
                break;
        }
    }
    public void CanvasMouseDragged(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                createShapeLayer(mousePosition);
                break;
            case MOVE:
                moveShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            case RESIZE:
                resizeShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            case ROTATE:
                rotateShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            default:
                break;
        }
    }
    
    /*
    ** 설정 관련 메소드
    */
    public void checkExit() {
        if (layerContainer.getArrayList().isEmpty() == false) { // 레이어가 하나라도 남아있을 경우
            switch ((JOptionPane.showConfirmDialog(null, "캔버스에 도형이 남아있습니다.\n정말 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE))) {
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
