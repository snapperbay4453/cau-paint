
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;
import java.awt.Point;
import java.util.Vector;

public class Controller{
    
    private Layer layer;
    private Variable variable;
    private View view;
    
    /*
    ** 생성자
    */
    public Controller() {
        layer = new Layer();
        variable = new Variable(this);
        view = new View(layer, variable, this);
        view.createView();
    }
    
    /*
    ** Layer, Shape 관련 메소드
    */
    public void addShape(Shape shape) {
        layer.addShape(shape);
    }
    public void addShape(Point position, Point size) {
        layer.addRectangle(position, size);
    }
    public void modifyShape(Point position) {
        layer.modifyShapeSizeAbsolute(position);
    }
    public void deleteShape(int index) {
        layer.deleteShape(index);
    }
    public void clearLayer() {
        layer.clear();
    }
    public Vector<Shape> getLayerArrayListToVector(){
        return layer.getVector();
    }
    
    /*
    ** Variable 관련 메소드
    */
    public void chooseColor() {
        variable.chooseColor();
    }
    public Shape getTempShape() {
        return variable.getTempShape();
    }
    public void setPointStart(Point point){
        variable.setPointStart(point);
    }
    public void setPointEnd(Point point){
        variable.setPointEnd(point);
    }
    public void makeTempShape(){
        variable.makeTempShape();
    }
    public void refreshTempShape(){
        variable.refreshTempShape();
    }
    public void finalizeTempShape(){
        variable.finalizeTempShape();
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
                makeTempShape();
                break;
            case MOVE:
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
                finalizeTempShape();
                break;
            case MOVE:
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
                refreshTempShape();
                break;
            case MOVE:
                break;
            default:
                break;
        }
    }
    
}
