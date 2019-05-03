
package caupaint.model;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class EllipseLayer extends ShapeLayer{
   
    static final String iconName = "oval";
    
    public EllipseLayer(Point position, Point size, Color color, int degree) {
        super(position, size, color, degree);
        setShape(new Ellipse2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public EllipseLayer(Point position, Point size) {
        super(position, size);
        setShape(new Ellipse2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public EllipseLayer() {
        super();
        setShape(new Ellipse2D.Double(0, 0, 0, 0));
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
            affineTransform.scale(((getWidth() * 0.5) + -tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX() < (tempX + tempWidth * 0.5) && currentMousePosition.getY() < (tempY + tempHeight * 0.5)) { // 3사분면
            affineTransform.scale(((getWidth() * 0.5) + -tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + -ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX()  >= (tempX + tempWidth * 0.5) && currentMousePosition.getY() < (tempY + tempHeight * 0.5)) { // 4사분면
            affineTransform.scale(((getWidth() * 0.5) + tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + -ty)/(getHeight() * 0.5));
        }
        Shape path2d = affineTransform.createTransformedShape(getShape());
        setX(tempX + (tempWidth - (path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX())) * 0.5);
        setY(tempY + (tempHeight - (path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY())) * 0.5);
        setWidth(path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX());
        setHeight(path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY()); 
    }
    
    
    public double getX(){
        return ((Ellipse2D)getShape()).getX();
    }
    public double getY(){
        return ((Ellipse2D)getShape()).getY();
    }
    public double getWidth(){
        return ((Ellipse2D)getShape()).getWidth();
    }
    public double getHeight(){
        return ((Ellipse2D)getShape()).getHeight();
    }
    
    public String getIconName() {
        return "ellipse";
    }
    
        public void setX(double x){
        ((Ellipse2D)getShape()).setFrame(x, getY(), getWidth(), getHeight());
    }
    public void setY(double y){
        ((Ellipse2D)getShape()).setFrame(getX(), y, getWidth(), getHeight());
    }
    public void setWidth(double width){
        ((Ellipse2D)getShape()).setFrame(getX(), getY(), width, getHeight());
    }
    public void setHeight(double height){
        ((Ellipse2D)getShape()).setFrame(getX(), getY(), getWidth(), height);
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        
    }
}
