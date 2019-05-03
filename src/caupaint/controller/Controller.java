
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;
import java.awt.Point;
import java.util.Vector;

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
    public void moveShapeLayer(int index, Point point) {
        layerContainer.moveShapeLayer(index, point);
    }
    public void resizeShapeLayer(int index, Point point) {
        layerContainer.resizeShapeLayer(index, point);
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
    public ShapeLayer getTempShapeLayer() {
        return variable.getTempShapeLayer();
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
    
    public void makeTempShapeLayer(){
        variable.makeTempShapeLayer();
    }
    public void refreshTempShapeLayer(){
        variable.refreshTempShapeLayer();
    }
    public void finalizeTempShapeLayer(){
        variable.finalizeTempShapeLayer();
    }
    
    /*
    ** Canvas 관련 메소드
    */
    public void CanvasMousePressed(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                setPointStart(mousePosition);
                setPointEnd(mousePosition);
                makeTempShapeLayer();
                break;
            case MOVE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case RESIZE:
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
                finalizeTempShapeLayer();
                break;
            case MOVE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case RESIZE:
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
                setPointEnd(mousePosition);
                refreshTempShapeLayer();
                break;
            case MOVE:
                moveShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            case RESIZE:
                resizeShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            default:
                break;
        }
    }
    
}
