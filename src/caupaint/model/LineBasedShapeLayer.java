
package caupaint.model;
import java.awt.*;

abstract public class LineBasedShapeLayer extends ShapeLayer{
    
    /*
    ** 생성자
    */
    public LineBasedShapeLayer(Point position, Point size, Color color, double radianAngle) {
        super(position, size, color, radianAngle);
        setColor(super.getColor());
        setRadianAngle(super.getRadianAngle());
    }
    public LineBasedShapeLayer(Point position, Point size) {
        super(position, size);
        setColor(super.getColor());
        setRadianAngle(super.getRadianAngle());
    }
    public LineBasedShapeLayer() {
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
