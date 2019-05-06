
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;

public class EllipseLayer extends ShapeLayer{
    
    /*
    ** 생성자
    */
    public EllipseLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, color, stroke, backgroundType, radianAngle, isVisible);
    }
    public EllipseLayer(Point position, Point size) { // 생성에 최소한으로 필요한 정보만 전달받음
        super(position, size);
        super.setName("새 타원");
    }
    public EllipseLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
        super.setName("새 타원");
    }
    public EllipseLayer(EllipseLayer source) { // 복제 생성자
        super(source);
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(Point currentMousePosition) {};
    @Override
    public void keepInitializing(Point recentlyPressedMousePosition, Point currentMousePosition){
        setPosition(new Point(min((int)recentlyPressedMousePosition.getX(), (int)currentMousePosition.getX()), 
                              min((int)recentlyPressedMousePosition.getY(), (int)currentMousePosition.getY())));
        setSize(new Point((int)abs(currentMousePosition.getX() - recentlyPressedMousePosition.getX()),
                          (int)abs(currentMousePosition.getY() - recentlyPressedMousePosition.getY())));
    }
    @Override
    public void finishInitializing() {};
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g.setColor(getColor());
        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        if (getBackgroundType() == BackgroundType.EMPTY) g.drawOval((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        else if (getBackgroundType() == BackgroundType.FILL) g.fillOval((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.ELLIPSE; }
    
}
