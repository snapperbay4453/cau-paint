
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;
import java.io.Serializable;

public class ShapeLayer implements Serializable{
    private Shape shape;
    private String name;
    private Color color;
    private BackgroundType backgroundType;
    private double radianAngle;
    private boolean isVisible;
    
    /*
    ** 생성자
    */
    public ShapeLayer(String name, Color color, BackgroundType backgroundType, double radianAngle, boolean isVisible) {
        this.name = name;
        this.color = color;
        this.backgroundType = backgroundType;
        this.radianAngle = radianAngle;
        this.isVisible = isVisible;
    }
    public ShapeLayer() {
        this.name = name;
        this.color = new Color(0, 0, 0);
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
        this.isVisible = true;
    }
    public ShapeLayer(ShapeLayer source) { // 복제 생성자
        this.name = source.getName();
        this.color = source.getColor();
        this.backgroundType = source.getBackgroundType();
        this.radianAngle = source.getRadianAngle();
        this.isVisible = source.isVisible;
    }
    
    public void create(Point recentMousePosition, Point currentMousePosition) {};
    public void translate(double tx, double ty) {};
    public void scale(Point recentMousePosition, Point currentMousePosition) {};
    public void rotate(Point recentMousePosition, Point currentMousePosition) {};
    
    public ShapeType getRealShapeType() {
        return ShapeType.SHAPE;
    }
        
    /*
    ** getter, setter
    */
    
    public Shape getShape() {
        return shape;
    }
    public String getName() {
        return name;
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
    public boolean getIsVisible() {
        return isVisible;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
    public void setName(String string) {
        this.name = string;
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
    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {

    }
}
