
package caupaint.model;
import caupaint.controller.*;

import java.awt.*;
import static java.lang.Math.*;

public class Variable {
    
    Controller controller;
    
    private Point pointStart;
    private Point pointEnd;
    private Shape tempShape;
    
    public Variable(Controller controller) {
        this.controller = controller;
        
        pointStart = new Point(0,0);
        pointEnd = new Point(0,0);
        tempShape = null;
    }
    
    public void makeTempShape() {
        tempShape = new Rectangle();
        refreshTempShape();
    }
    public void refreshTempShape() {
        tempShape.setPosition(new Point(min((int)pointStart.getX(), (int)pointEnd.getX()), min((int)pointStart.getY(), (int)pointEnd.getY())));
        tempShape.setSize(new Point(abs((int)pointStart.getX() - (int)pointEnd.getX()), abs((int)pointStart.getY() - (int)pointEnd.getY())));
    }
    public void finalizeTempShape() {   
        controller.addShape(tempShape);
        tempShape = null;
    }
    
    /*
    ** getter, setter
    */
    public Point getPointStart() {
        return pointStart;
    }
    public Point getPointEnd() {
        return pointEnd;
    }
    public Shape getTempShape() {
        return tempShape;
    }
    public void setPointStart(Point point) {
        this.pointStart = point;
    }
    public void setPointEnd(Point point) {
        this.pointEnd = point;
    }
    
}
