
package caupaint.model;
import java.awt.*;

abstract public class PlaneBasedShapeLayer extends ShapeLayer{
    
    /*
    ** 생성자
    */
    public PlaneBasedShapeLayer(Point position, Point size, Color color, double radianAngle) {
        super(position, size, color, radianAngle);
        setColor(super.getColor());
        setRadianAngle(super.getRadianAngle());
    }
    public PlaneBasedShapeLayer(Point position, Point size) {
        super(position, size);
        setColor(super.getColor());
        setRadianAngle(super.getRadianAngle());
    }
    public PlaneBasedShapeLayer() {
        super();
        setColor(super.getColor());
        setRadianAngle(super.getRadianAngle());
    }
    
    abstract public void create(Point recentMousePosition, Point currentMousePosition);
    abstract public void translate(double tx, double ty);
    abstract public void scale(Point recentMousePosition, Point currentMousePosition);
    abstract public void rotate(Point recentMousePosition, Point currentMousePosition);
    
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
    public void draw(Graphics g) {

    }
}
