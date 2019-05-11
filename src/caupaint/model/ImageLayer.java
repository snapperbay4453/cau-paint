
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import javax.swing.ImageIcon;

public class ImageLayer extends RectangleLayer{

    private ImageIcon imageIcon; // 이미지 파일을 저장
    
    /*
    ** 생성자
    */
    public ImageLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible, String imagePath) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isVisible);
        setImageIcon(imagePath);
        setSize(new Point(imageIcon.getIconWidth(), imageIcon.getIconHeight())); // imageIcon으로부터 크기 정보 추출
    }
    public ImageLayer(Point position, Point size, String imagePath) { // 생성에 최소한으로 필요한 정보만 전달받음
        super(position, size);
        super.setName("새 이미지");
        setImageIcon(imagePath);
        setSize(new Point(imageIcon.getIconWidth(), imageIcon.getIconHeight())); // imageIcon으로부터 크기 정보 추출
    }
    public ImageLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
        super.setName("새 이미지");
        setImageIcon(null);
    }
    public ImageLayer(ImageLayer source) { // 복제 생성자
        super(source);
        this.imageIcon = source.getImageIcon();
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    /*
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
    */
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        ImageIcon scaledImageIcon = new ImageIcon(getImageIcon().getImage().getScaledInstance((int)getSize().getX(), (int)getSize().getY(), java.awt.Image.SCALE_SMOOTH));
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장

        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX(), (int)getPosition().getY(), null);
        g.setColor(getBorderColor());
        g.drawRect((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.IMAGE; }
    public ImageIcon getImageIcon() { return imageIcon; }
    public void setImageIcon(String imagePath) { imageIcon = new ImageIcon(imagePath); }

}
