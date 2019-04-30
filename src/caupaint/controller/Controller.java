
package caupaint.controller;
import caupaint.model.*;
import caupaint.view.*;
import caupaint.observer.*;
import java.awt.Point;

public class Controller{
    
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
    
}
