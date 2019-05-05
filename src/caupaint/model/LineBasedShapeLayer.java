
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;

abstract public class LineBasedShapeLayer extends ShapeLayer{
    
    /*
    ** 생성자
    */
    public LineBasedShapeLayer(String name, Color color, BackgroundType backgroundType, double radianAngle, boolean isVisible) {
        super(name, color, backgroundType, radianAngle, isVisible);
        setName(super.getName());
        setColor(super.getColor());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }
    public LineBasedShapeLayer() {
        super();
        setName(super.getName());
        setColor(super.getColor());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }
    public LineBasedShapeLayer(LineBasedShapeLayer source) { // 복제 생성자
        super(source);
        setName(super.getName());
        setColor(super.getColor());
        setBackgroundType(super.getBackgroundType());
        setRadianAngle(super.getRadianAngle());
        setIsVisible(super.getIsVisible());
    }
    
    abstract public void create(Point recentMousePosition, Point currentMousePosition);
    abstract public void translate(double tx, double ty);
    abstract public void scale(Point recentMousePosition, Point currentMousePosition);
    abstract public void rotate(Point recentMousePosition, Point currentMousePosition);
    
    abstract public ShapeType getRealShapeType();
    
    /*
    ** getter, setter
    */
    abstract public double getX1();
    abstract public double getY1();
    abstract public double getX2();
    abstract public double getY2();

    abstract public void setX1(double x);
    abstract public void setY1(double y);
    abstract public void setX2(double x);
    abstract public void setY2(double y);
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {

    }
}
