
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.controller.*;

import java.awt.*;
import static java.lang.Math.*;
import javax.swing.*;

public class Variable {
    
    Controller controller;
    
    private ShapeType shapeType;
    private Point pointStart;
    private Point pointEnd;
    private Color color;
    private Shape tempShape; // Layer에 추가하기 전 임시로 shape를 저장
    
    public Variable(Controller controller) {
        this.controller = controller;
        
        shapeType = ShapeType.RECTANGLE;
        pointStart = new Point(0,0);
        pointEnd = new Point(0,0);
        color = new Color(0, 0, 0);
        tempShape = null;
    }
    
    /*
    ** Shape의 속성 관련 메소드
    */
    public void chooseColor() {
        JColorChooser chooser=new JColorChooser();
        color = chooser.showDialog(null,"Color",Color.YELLOW);
    }
    
    /*
    ** tempShape 관련 메소드
    */
    public void makeTempShape() {
        switch (shapeType){
            case RECTANGLE:
                tempShape = new Rectangle();
                break;
            case OVAL:
                tempShape = new Oval();
                break;
        }
        tempShape.setColor(color);
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
    public ShapeType getShapeType() {
        return shapeType;
    }
    public Point getPointStart() {
        return pointStart;
    }
    public Point getPointEnd() {
        return pointEnd;
    }
    public Shape getTempShape() {
        return tempShape;
    }
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }
    public void setPointStart(Point point) {
        this.pointStart = point;
    }
    public void setPointEnd(Point point) {
        this.pointEnd = point;
    }
    
}
