
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;

abstract public class PlaneBasedShapeLayer extends ShapeLayer{
    
    /*
    ** 생성자
    */
    public PlaneBasedShapeLayer(String name, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) {
        super(name, color, stroke, backgroundType, radianAngle, isVisible);
        setName(super.getName());
        setColor(super.getColor());
        setStroke(super.getStroke());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }
    public PlaneBasedShapeLayer() {
        super();
        setName(super.getName());
        setColor(super.getColor());
        setStroke(super.getStroke());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }
    public PlaneBasedShapeLayer(PlaneBasedShapeLayer source) { // 복제 생성자
        super(source);
        setName(super.getName());
        setColor(super.getColor());
        setStroke(super.getStroke());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }

    /*
    ** 도형 변형 관련 메소드
    */
    abstract public void create(Point recentMousePosition, Point currentMousePosition);
    abstract public void translate(double tx, double ty);
    abstract public void scale(Point recentMousePosition, Point currentMousePosition);
    abstract public void rotate(Point recentMousePosition, Point currentMousePosition);
    
    abstract public ShapeType getRealShapeType();
    
    /*
    ** getter, setter
    */
    abstract public double getX();
    abstract public double getY();
    abstract public double getWidth();
    abstract public double getHeight();

    abstract public void setX(double x);
    abstract public void setY(double y);
    abstract public void setWidth(double width);
    abstract public void setHeight(double height);
    
    /*
    ** 그래픽 관련 메소드
    */
    abstract public void draw(Graphics g);
}
