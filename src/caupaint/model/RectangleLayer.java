
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;

public class RectangleLayer extends ShapeLayer{

    /*
    ** 생성자
    */
    public RectangleLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isFlipped, isVisible);
    }
    public RectangleLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
        super.setName("새 사각형");
    }
    public RectangleLayer(RectangleLayer source) { // 복제 생성자
        super(source);
    }
    
    /*
    ** Builder 메소드
    */
    public static class Builder extends ShapeLayer.Builder { 
        public RectangleLayer build() {
            BasicStroke tempStroke = new BasicStroke(strokeWidth, Constant.defaultSolidLineBasicStroke.getEndCap(), Constant.defaultSolidLineBasicStroke.getLineJoin(), Constant.defaultSolidLineBasicStroke.getMiterLimit(), strokeDash, strokeDashPhase);
            return new RectangleLayer(name, position, size, borderColor, backgroundColor, tempStroke, backgroundType, radianAngle, isFlipped, isVisible);
        }
    }

    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point currentMousePosition) {
        switch(mouseActionType) {
            case PRESSED:
                setPosition(currentMousePosition);
                break;
            case DRAGGED:
                setPosition(new Point(min((int)recentlyPressedMousePosition.getX(), (int)currentMousePosition.getX()), 
                min((int)recentlyPressedMousePosition.getY(), (int)currentMousePosition.getY())));
                setSize(new Point((int)abs(currentMousePosition.getX() - recentlyPressedMousePosition.getX()),
                (int)abs(currentMousePosition.getY() - recentlyPressedMousePosition.getY())));
                break;
            case RELEASED: break;
            default: break;
        }
    };
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장

        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        if (getBackgroundType() == BackgroundType.FILL) {
            g.setColor(getBackgroundColor());
            g.fillRect((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        }
        g.setColor(getBorderColor());
        g.drawRect((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.RECTANGLE; }

}
