
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

public class ImageLayer extends RectangleLayer{

    private ImageIcon imageIcon; // 이미지 파일을 저장
    
    /*
    ** 생성자
    */
    public ImageLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible, String imagePath) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isFlipped, isVisible);
        setImageIcon(imagePath);
        setSize(new Point(imageIcon.getIconWidth(), imageIcon.getIconHeight())); // imageIcon으로부터 크기 정보 추출
    }
    public ImageLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        super();
        setImageIcon(null);
    }
    public ImageLayer(ImageLayer source) { // 복제 생성자
        super(source);
        this.imageIcon = source.getImageIcon();
    }

    /*
    ** Builder 메소드
    */
    public static class Builder extends ShapeLayer.Builder { 
        public String getDefaultName() { return "새 이미지"; }
        public ImageLayer build() {
            BasicStroke tempStroke = new BasicStroke(strokeWidth, Constant.defaultSolidLineBasicStroke.getEndCap(), Constant.defaultSolidLineBasicStroke.getLineJoin(), Constant.defaultSolidLineBasicStroke.getMiterLimit(), strokeDash, strokeDashPhase);
            return new ImageLayer(name, position, size, borderColor, backgroundColor, tempStroke, backgroundType, radianAngle, isFlipped, isVisible, imagePath);
        }
    }
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        ImageIcon scaledImageIcon = new ImageIcon(getImageIcon().getImage().getScaledInstance((int)getSize().getX(), (int)getSize().getY(), java.awt.Image.SCALE_SMOOTH));
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장

        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getCentralPoint().getX(), getCentralPoint().getY());
        switch(getIsFlipped()) {
            case 0x0: // 대칭 없음
                g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY(), null);
                break;
            case Constant.isFlippedHorizontallyFlag: // 가로 대칭
                g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX() + (int)getSize().getX(), (int)getPosition().getY(), -(int)getSize().getX(), (int)getSize().getY(), null);
                break;
            case Constant.isFlippedVerticallyFlag: // 세로 대칭
                g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX(), (int)getPosition().getY() + (int)getSize().getY(), (int)getSize().getX(), -(int)getSize().getY(), null);
                break; 
            case Constant.isFlippedHorizontallyFlag | Constant.isFlippedVerticallyFlag: // 가로 및 세로 대칭
                g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX() + (int)getSize().getX(), (int)getPosition().getY() + (int)getSize().getY(), -(int)getSize().getX(), -(int)getSize().getY(), null);
                break;
        }
        //g.drawImage(scaledImageIcon.getImage(), (int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY(), null);
        if (getBackgroundType().equals(BackgroundType.FILL)) {
            g.setColor(getBackgroundColor());
            g.drawRect((int)getPosition().getX(), (int)getPosition().getY(), (int)getSize().getX(), (int)getSize().getY());
        }
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.IMAGE; }
    @Override public String getIconFileName() { return "image.png"; }
    public ImageIcon getImageIcon() { return imageIcon; }
    public void setImageIcon(String imagePath) { imageIcon = new ImageIcon(imagePath); }

}
