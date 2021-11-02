
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;
import javax.swing.JOptionPane;

public class TextLayer extends ShapeLayer{
 
    private String fontName;
    private int fontStyle; // 0: PLAIN, 1: BOLD, 2: ITALIC, BOLD & ITALIC: 3
    private int fontSize;
    
    /*
    ** 생성자
    */
    public TextLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible, Font font) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isFlipped, isVisible);
        setFontName(font.getFontName());
        setFontStyle(font.getStyle());
        setFontSize(font.getSize());
    }
    public TextLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
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
    ** Builder 메소드
    */
    public static class Builder extends ShapeLayer.Builder { 
        public String getDefaultName() { return "새 텍스트"; }
        public TextLayer build() {
            BasicStroke tempStroke = new BasicStroke(strokeWidth, Constant.defaultSolidLineBasicStroke.getEndCap(), Constant.defaultSolidLineBasicStroke.getLineJoin(), Constant.defaultSolidLineBasicStroke.getMiterLimit(), strokeDash, strokeDashPhase);
            return new TextLayer(name, position, size, borderColor, backgroundColor, tempStroke, backgroundType, radianAngle, isFlipped, isVisible, font);
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
    ** 레이어 변형 관련 메소드
    */
    @Override
    public void scale(Point recentlyDraggedMousePosition, Point currentMousePosition, ShapeLayerAnchorType anchorType) {
        JOptionPane.showMessageDialog(null, "이 레이어는 크기 조정 기능을 지원하지 않습니다.", "지원하지 않는 명령", JOptionPane.ERROR_MESSAGE);
    }
    @Override
    public void flipHorizontally(){
        JOptionPane.showMessageDialog(null, "이 레이어는 반전 기능을 지원하지 않습니다.", "지원하지 않는 명령", JOptionPane.ERROR_MESSAGE);
    }
    @Override
    public void flipVertically(){
        JOptionPane.showMessageDialog(null, "이 레이어는 반전 기능을 지원하지 않습니다.", "지원하지 않는 명령", JOptionPane.ERROR_MESSAGE);
    }
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g.setFont(new Font(fontName, fontStyle, fontSize)); // 폰트 설정
        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getCentralPoint().getX(), getCentralPoint().getY());
        Rectangle2D textBackgroundRectangle = g.getFontMetrics().getStringBounds(getName(), g);
        setSize(new Point((int)textBackgroundRectangle.getWidth(), (int)textBackgroundRectangle.getHeight())); // 크기 새로 설정
        if (getBackgroundType() == BackgroundType.FILL) { // 텍스트의 배경색 그리기
            g.setColor(getBackgroundColor());
            g.fillRect((int)getPosition().getX(), (int)getPosition().getY(), (int)textBackgroundRectangle.getWidth(), (int)textBackgroundRectangle.getHeight());
        }
        g.setColor(getBorderColor());
        g.drawString(getName(), (int)getPosition().getX(), (int)getPosition().getY() + g.getFontMetrics().getAscent());
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
    @Override public String getIconFileName() { return "text.png"; } ;
    @Override public ShapeLayer getWireframe() {
        TextLayer wireframe = (TextLayer)ShapeLayerFactory.createClone(ShapeType.TEXT, this);
        wireframe.setBackgroundType(BackgroundType.EMPTY);
        wireframe.setBorderColor(Color.GRAY);
        return wireframe;
    }
    public void setFont(Font font) {
        this.fontName = font.getName();
        this.fontStyle = font.getStyle();
        this.fontSize = font.getSize();
    }
    public void setFontName(String fontName) { this.fontName = fontName; }
    public void setFontStyle(int fontStyle) { this.fontStyle = fontStyle; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }
    
}
