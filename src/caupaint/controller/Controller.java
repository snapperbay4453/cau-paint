
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;

public class Controller implements ControllerInterface{
    
    private Layer layer;
    private View view;
    
    /*
    ** 생성자
    */
    public Controller() {
        layer = new Layer();
        view = new View(this, layer);
        view.createView();
    }
    
    /*
    ** Shape 관련 메소드
    */
    public void addShape(Shape shape) {
        layer.addShape(shape);
    }
    public void deleteLastShape() {
        layer.deleteLastShape();
    }
    public void clearLayer() {
        layer.clear();
    }
    
}
