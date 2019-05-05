
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.io.Serializable;

public class ShapeLayer implements Serializable{ // 파일로 저장해야 하므로 직렬화 구현
    private Shape shape; // 도형 클래스, 시작점의 위치와 너비, 높이 값을 포함
    private String name; // 레이어의 이름
    private Color color; // 색상
    private BasicStroke stroke; // 외곽선 속성
    private BackgroundType backgroundType; // 배경색 속성
    private double radianAngle; // 회전 각도(라디안)
    private boolean isVisible; // 화면에 표시 여부
    
    /*
    ** 생성자
    */
    public ShapeLayer(String name, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) {
        this.name = name;
        this.color = color;
        this.stroke = stroke;
        this.backgroundType = backgroundType;
        this.radianAngle = radianAngle;
        this.isVisible = isVisible;
    }
    public ShapeLayer() {
        this.name = name;
        this.color = new Color(0, 0, 0);
        this.stroke = new BasicStroke();
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
        this.isVisible = true;
    }
    public ShapeLayer(ShapeLayer source) { // 복제 생성자
        this.name = source.getName();
        this.color = source.getColor();
        this.stroke = source.getStroke();
        this.backgroundType = source.getBackgroundType();
        this.radianAngle = source.getRadianAngle();
        this.isVisible = source.isVisible;
    }

    /*
    ** 도형 변형 관련 메소드
    */
    public void create(Point recentMousePosition, Point currentMousePosition) {};
    public void translate(double tx, double ty) {};
    public void scale(Point recentMousePosition, Point currentMousePosition) {};
    public void rotate(Point recentMousePosition, Point currentMousePosition) {};

    /*
    ** getter, setter
    */
    public Shape getShape() { return shape; }
    public String getName() { return name; }
    public Color getColor() { return color; }
    public BasicStroke getStroke() { return stroke; }
    public BackgroundType getBackgroundType() { return backgroundType; }
    public double getRadianAngle() { return radianAngle; }
    public boolean getIsVisible() { return isVisible; }
    public ShapeType getRealShapeType() { return ShapeType.SHAPE; }
    
    public void setShape(Shape shape) { this.shape = shape; }
    public void setName(String string) { this.name = string; }
    public void setColor(Color color) { this.color = color; }
    public void setStroke(BasicStroke stroke) { this.stroke = stroke; }
    public void setBackgroundType (BackgroundType backgroundType) { this.backgroundType = backgroundType;  }
    public void setRadianAngle(double radianAngle) { this.radianAngle = radianAngle; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) { }
    
}
