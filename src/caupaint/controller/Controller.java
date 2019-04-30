
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;
import java.awt.Point;

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
    public void deleteLastShape() {
        layer.deleteLastShape();
    }
    public void clearLayer() {
        layer.clear();
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
    
    
}
