
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;

public class TriangleLayer extends ShapeLayer{

    /*
    ** 생성자
    */
    public TriangleLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, color, stroke, backgroundType, radianAngle, isVisible);
    }
    public TriangleLayer(Point position, Point size) {
        super(position, size);
        super.setName("새 삼각형");
    }
    public TriangleLayer() {
        super();
        super.setName("새 삼각형");
    }
    public TriangleLayer(TriangleLayer source) { // 복제 생성자
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
    ** 변수 관련 메소드
    */
    
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
        if (getBackgroundType() == BackgroundType.EMPTY) g.drawPolygon(new int[] {(int)getVertex(0).getX(), (int)getVertex(1).getX(), (int)getVertex(2).getX()}, new int[] {(int)getVertex(0).getY(), (int)getVertex(1).getY(), (int)getVertex(2).getY()}, 3);
        else if (getBackgroundType() == BackgroundType.FILL) g.fillPolygon(new int[] {(int)getVertex(0).getX(), (int)getVertex(1).getX(), (int)getVertex(2).getX()}, new int[] {(int)getVertex(0).getY(), (int)getVertex(1).getY(), (int)getVertex(2).getY()}, 3);
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    public Point getVertex(int index) {
        switch (index) {
            case 0: return new Point((int)(getCentralPoint().getX()), (int)((getCentralPoint().getY() - getSize().getY() / 2)));
            case 1: return new Point((int)(getCentralPoint().getX() + getSize().getX() / 2), (int)((getCentralPoint().getY() + getSize().getY() / 2)));
            case 2: return new Point((int)(getCentralPoint().getX() - getSize().getX() / 2), (int)((getCentralPoint().getY() + getSize().getY() / 2)));
            default: return null;
        }
    }
    @Override public ShapeType getRealShapeType() { return ShapeType.TRIANGLE; }
    
}
