
package caupaint.model;
import java.awt.*;

abstract public class ShapeLayer {
    private Shape shape;
    private Color color;
    private double degree;
    
    private static String iconName = "shape";
    
    /*
    ** 생성자
    */
    public ShapeLayer(Point position, Point size, Color color, int degree) {
        this.color = color;
        this.degree = degree;
    }
    public ShapeLayer(Point position, Point size) {
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }
    public ShapeLayer() {
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }
    
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
    public String getIconName() {
        return "shape";
    }
    
    public Shape getShape() {
        return shape;
    }
    public Color getColor() {
        return color;
    }
    public double getDegree() {
        return degree;
    }

    abstract public void setX(double x);
    abstract public void setY(double y);
    abstract public void setWidth(double width);
    abstract public void setHeight(double height);
    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setDegree(double degree) {
        this.degree = degree;
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {

    }
}
