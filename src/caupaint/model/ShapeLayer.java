
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;

abstract public class ShapeLayer {
    private Shape shape;
    private Color color;
    private BackgroundType backgroundType;
    private double radianAngle;
    
    /*
    ** 생성자
    */
    public ShapeLayer(Point point1, Point point2, Color color, BackgroundType backgroundType, double radianAngle) {
        this.color = color;
        this.backgroundType = backgroundType;
        this.radianAngle = radianAngle;
    }
    public ShapeLayer(Point point1, Point point2) {
        this.color = new Color(0, 0, 0);
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
    }
    public ShapeLayer() {
        this.color = new Color(0, 0, 0);
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
    }
    
    abstract public void create(Point recentMousePosition, Point currentMousePosition);
    abstract public void translate(double tx, double ty);
    abstract public void scale(Point recentMousePosition, Point currentMousePosition);
    abstract public void rotate(Point recentMousePosition, Point currentMousePosition);
    
    /*
    ** getter, setter
    */
    public String getIconName() {
        return "shape";
    }
    
    public Shape getShape() {
        return shape;
    }
    public Color getColor() {
        return color;
    }
    public BackgroundType getBackgroundType() {
        return backgroundType;
    }
    public double getRadianAngle() {
        return radianAngle;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setBackgroundType (BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
    }
    public void setRadianAngle(double radianAngle) {
        this.radianAngle = radianAngle;
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {

    }
}
