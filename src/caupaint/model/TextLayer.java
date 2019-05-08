
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import static java.lang.Math.*;

public class TextLayer extends ShapeLayer{
 
    private String fontName;
    private int fontStyle; // 0: PLAIN, 1: BOLD, 2: ITALIC, BOLD & ITALIC: 3
    private int fontSize;
    
    /*
    ** 생성자
    */
    public TextLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible, Font font) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, color, stroke, backgroundType, radianAngle, isVisible);
        setFontName(font.getFontName());
        setFontStyle(font.getStyle());
        setFontSize(font.getSize());
    }
    public TextLayer(Point position, Point size) { // 생성에 최소한으로 필요한 정보만 전달받음
        super(position, size);
        super.setName("새 텍스트");
        setFontName(Constant.defaultFont.getFontName());
        setFontStyle(Constant.defaultFont.getStyle());
        setFontSize(Constant.defaultFont.getSize());
    }
    public TextLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
        super.setName("새 텍스트");
        setFontName(Constant.defaultFont.getFontName());
        setFontStyle(Constant.defaultFont.getStyle());
        setFontSize(Constant.defaultFont.getSize());
    }
    public TextLayer(TextLayer source) { // 복제 생성자
        super(source);
        this.fontName = source.getFontName();
        this.fontStyle = source.getFontStyle();
        this.fontSize = source.getFontSize();
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(Point currentMousePosition) {
        setPosition(currentMousePosition);
    };
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
        g.setFont(new Font(fontName, fontStyle, fontSize)); // 폰트 설정
        g.setColor(getColor());
        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        g.drawString(getName(), (int)getPosition().getX(), (int)getPosition().getY());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    public Font getFont() { return new Font(fontName, fontStyle, fontSize); }
    public String getFontName() { return fontName; };
    public int getFontStyle() { return fontStyle; };
    public int getFontSize() { return fontSize; };
    @Override public ShapeType getRealShapeType() { return ShapeType.TEXT; }

    public void setFont(Font font) {
        this.fontName = font.getName();
        this.fontStyle = font.getStyle();
        this.fontSize = font.getSize();
    }
    public void setFontName(String fontName) { this.fontName = fontName; }
    public void setFontStyle(int fontStyle) { this.fontStyle = fontStyle; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    
}