
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class RectangleLayer extends PlaneBasedShapeLayer{

    public RectangleLayer(Point position, Point size, String name, Color color, BackgroundType backgroundType, int radianAngle) {
        super(name, color, backgroundType, radianAngle);
        setShape(new Rectangle2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public RectangleLayer(Point position, Point size) {
        super();
        setShape(new Rectangle2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public RectangleLayer() {
        super();
        setShape(new Rectangle2D.Double(0, 0, 0, 0));
    }
    public RectangleLayer(RectangleLayer source) { // 복제 생성자
        super(source);
        setShape(new Rectangle2D.Double(source.getX(), source.getY(), source.getWidth(), source.getHeight()));
    }
    
    public void create(Point recentMousePosition, Point currentMousePosition) {
        scale(recentMousePosition, currentMousePosition);
    }
    public void translate(double tx, double ty) {
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(tx, ty);
        Shape path2d = affineTransform.createTransformedShape(getShape());
        setX(path2d.getBounds2D().getMinX());
        setY(path2d.getBounds2D().getMinY());
        setWidth(path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX());
        setHeight(path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY());
    }
    
    public void scale(Point recentMousePosition, Point currentMousePosition) {
        double tx = currentMousePosition.getX() - recentMousePosition.getX();   double ty = currentMousePosition.getY() - recentMousePosition.getY();
        double tempX = getX();  double tempY = getY();
        double tempWidth = getWidth();  double tempHeight = getHeight();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(-(getX() + (getWidth()) * 0.5), -(getY() + (getHeight() * 0.5))); // 도형을 (0,0)으로 이동
        if (currentMousePosition.getX() >= (tempX + tempWidth * 0.5) && currentMousePosition.getY() >= (tempY + tempHeight * 0.5)) { // 1사분면
            affineTransform.scale(((getWidth() * 0.5) + tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX() < (tempX + tempWidth * 0.5) && currentMousePosition.getY() >= (tempY + tempHeight * 0.5)) { // 2사분면
            affineTransform.scale(((getWidth() * 0.5) - tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX() < (tempX + tempWidth * 0.5) && currentMousePosition.getY() < (tempY + tempHeight * 0.5)) { // 3사분면
            affineTransform.scale(((getWidth() * 0.5) - tx)/(getWidth() * 0.5), ((getHeight() * 0.5) - ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX()  >= (tempX + tempWidth * 0.5) && currentMousePosition.getY() < (tempY + tempHeight * 0.5)) { // 4사분면
            affineTransform.scale(((getWidth() * 0.5) + tx)/(getWidth() * 0.5), ((getHeight() * 0.5) - ty)/(getHeight() * 0.5));
        }
        Shape path2d = affineTransform.createTransformedShape(getShape());
        setX(tempX + (tempWidth - (path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX())) * 0.5);
        setY(tempY + (tempHeight - (path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY())) * 0.5);
        setWidth(path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX());
        setHeight(path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY()); 
    }
    public void rotate(Point currentMousePosition, Point recentMousePosition){
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate((Math.atan2(currentMousePosition.getY() - (getY() + getHeight() * 0.5), currentMousePosition.getX() - (getX() + getWidth() * 0.5))
                                - Math.atan2(recentMousePosition.getY() - (getY() + getHeight() * 0.5), recentMousePosition.getX() - (getX() + getWidth() * 0.5))),
                getWidth() * 0.5, getHeight() * 0.5);
        Shape path2d = affineTransform.createTransformedShape(getShape());
        setRadianAngle(getRadianAngle()
                    - (Math.atan2
                         (currentMousePosition.getY() - (getY() + getHeight() * 0.5), currentMousePosition.getX() - (getX() + getWidth() * 0.5))
                     - Math.atan2
                          (recentMousePosition.getY() - (getY() + getHeight() * 0.5), recentMousePosition.getX() - (getX() + getWidth() * 0.5))
                   )
                );
    }
        
    public double getX(){
        return ((Rectangle2D)getShape()).getX();
    }
    public double getY(){
        return ((Rectangle2D)getShape()).getY();
    }
    public double getWidth(){
        return ((Rectangle2D)getShape()).getWidth();
    }
    public double getHeight(){
        return ((Rectangle2D)getShape()).getHeight();
    }
    public ShapeType getRealShapeType() {
        return ShapeType.RECTANGLE;
    }
    
    public void setX(double x){
        ((Rectangle2D)getShape()).setRect(x, getY(), getWidth(), getHeight());
    }
    public void setY(double y){
        ((Rectangle2D)getShape()).setRect(getX(), y, getWidth(), getHeight());
    }
    public void setWidth(double width){
        ((Rectangle2D)getShape()).setRect(getX(), getY(), width, getHeight());
    }
    public void setHeight(double height){
        ((Rectangle2D)getShape()).setRect(getX(), getY(), getWidth(), height);
    }
    public void setShape(Shape shape) {
        super.setShape((Rectangle2D)shape);
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g.setColor(getColor());
        g2d.rotate(getRadianAngle(), getX() + getWidth() * 0.5, getY() + getHeight() * 0.5);
        if (getBackgroundType() == BackgroundType.EMPTY) g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        else if (getBackgroundType() == BackgroundType.FILL) g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
}
