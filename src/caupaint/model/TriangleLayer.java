
package caupaint.model;
import caupaint.model.Enum.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class TriangleLayer extends PlaneBasedShapeLayer{

    private Point[] point = new Point[3];
    private Point centerPoint = new Point();
    
    public TriangleLayer(Point position, Point size, String name, Color color, BackgroundType backgroundType, int radianAngle, boolean isVisible) {
        super(name, color, backgroundType, radianAngle, isVisible);
        setPoint(0, position);  setPoint(1, position);  setPoint(2, position);
        setCenterPoint(position);
        setShape(new Rectangle2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public TriangleLayer(Point position, Point size) {
        super();
        setShape(new Rectangle2D.Double(position.getX(), position.getY(), size.getX(), size.getY()));
    }
    public TriangleLayer() {
        super();
        setPoint(0, new Point(0,0));  setPoint(1,new Point(0,0));  setPoint(2, new Point(0,0));
        setCenterPoint(new Point(0,0));
        setShape(new Rectangle2D.Double(0, 0, 0, 0));
    }
    public TriangleLayer(TriangleLayer source) { // 복제 생성자
        super(source);
        setShape(new Rectangle2D.Double(source.getX(), source.getY(), source.getWidth(), source.getHeight()));
    }
    
    public void create(Point recentMousePosition, Point currentMousePosition) {
        double tx = currentMousePosition.getX() - recentMousePosition.getX();   double ty = currentMousePosition.getY() - recentMousePosition.getY();
        setWidth(getWidth() + tx);
        setHeight(getHeight() + ty);
        refreshPoints();
    }
    public void translate(double tx, double ty) {
        setCenterPoint(new Point((int)(getCenterPoint().getX() + tx), (int)(getCenterPoint().getY() + ty)));
        refreshPoints();
    }
    public void scale(Point recentMousePosition, Point currentMousePosition) {
        double tx = currentMousePosition.getX() - recentMousePosition.getX();   double ty = currentMousePosition.getY() - recentMousePosition.getY();
        double tempX = getX();  double tempY = getY();
        double tempWidth = getWidth();  double tempHeight = getHeight();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(-(getX() + (getWidth()) * 0.5), -(getY() + (getHeight() * 0.5))); // 도형을 (0,0)으로 이동
        if (currentMousePosition.getX() >= (getCenterPoint().getX()) && currentMousePosition.getY() >= (getCenterPoint().getY())) { // 1사분면
            affineTransform.scale(((getWidth() * 0.5) + tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX() < (getCenterPoint().getX()) && currentMousePosition.getY() >= (getCenterPoint().getY())) { // 2사분면
           affineTransform.scale(((getWidth() * 0.5) - tx)/(getWidth() * 0.5), ((getHeight() * 0.5) + ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX() < (getCenterPoint().getX()) && currentMousePosition.getY() < (getCenterPoint().getY())) { // 3사분면
            affineTransform.scale(((getWidth() * 0.5) - tx)/(getWidth() * 0.5), ((getHeight() * 0.5) - ty)/(getHeight() * 0.5));
        }
        else if (currentMousePosition.getX()  >= (getCenterPoint().getX()) && currentMousePosition.getY() < (getCenterPoint().getY())) { // 4사분면
            affineTransform.scale(((getWidth() * 0.5) + tx)/(getWidth() * 0.5), ((getHeight() * 0.5) - ty)/(getHeight() * 0.5));
        }
        Shape path2d = affineTransform.createTransformedShape(getShape());
        setX(tempX + (tempWidth - (path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX())) * 0.5);
        setY(tempY + (tempHeight - (path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY())) * 0.5);
        setWidth(path2d.getBounds2D().getMaxX() - path2d.getBounds2D().getMinX());
        setHeight(path2d.getBounds2D().getMaxY() - path2d.getBounds2D().getMinY()); 
        refreshPoints();
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
    public Point getPoint(int index) {
        return point[index];
    }
    public Point getCenterPoint() {
        return centerPoint; 
    }
    
    public ShapeType getRealShapeType() {
        return ShapeType.TRIANGLE;
    }
    
    public void setX(double x){
        ((Rectangle2D)getShape()).setRect(x, getY(), getWidth(), getHeight());
    }
    public void setY(double y){
        ((Rectangle2D)getShape()).setRect(getX(), y, getWidth(), getHeight());
    }
    public void setWidth(double x){
        ((Rectangle2D)getShape()).setRect(getX(), getY(), x, getHeight());
        setPoint(0, new Point((int)(getCenterPoint().getX()), (int)((getCenterPoint().getY() - getHeight() * 0.5))));        
        setPoint(1, new Point((int)(getCenterPoint().getX() + getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
        setPoint(2, new Point((int)(getCenterPoint().getX() - getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
    }
    public void setWidth(){
    //    ((Rectangle2D)getShape()).setRect(getX(), getY(), getPoint(1).getX() - getPoint(2).getX(), getHeight()); // 넓이 자동처리
    }
    public void setHeight(double y){
        ((Rectangle2D)getShape()).setRect(getX(), getY(), getWidth(), y);
        setPoint(0, new Point((int)(getCenterPoint().getX()), (int)((getCenterPoint().getY() - getHeight() * 0.5))));        
        setPoint(1, new Point((int)(getCenterPoint().getX() + getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
        setPoint(2, new Point((int)(getCenterPoint().getX() - getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
    }
    public void setHeight(){
    //    ((Rectangle2D)getShape()).setRect(getX(), getY(), getWidth(), getPoint(1).getY() - getPoint(0).getY()); // 높이 자동처리
    }
    public void setPoint(int index, Point point) {
        this.point[index] = point; 
    }
    public void setCenterPoint(Point point) {
        this.centerPoint = point; 
    }
    public void refreshPoints() { // point들의 값을 centerPoint와 width, height의 값에 맞추어 새로고침
        setPoint(0, new Point((int)(getCenterPoint().getX()), (int)((getCenterPoint().getY() - getHeight() * 0.5))));        
        setPoint(1, new Point((int)(getCenterPoint().getX() + getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
        setPoint(2, new Point((int)(getCenterPoint().getX() - getWidth() * 0.5), (int)((getCenterPoint().getY() + getHeight() * 0.5))));
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
        //g2d.rotate(getRadianAngle(), getCenterPoint().getX(), getCenterPoint().getY()); // centerPoint를 중심으로 한 회전
        g2d.rotate(getRadianAngle(), getCenterPoint().getX(), (getPoint(0).getY() / 3) + (getPoint(1).getY() / 3 * 2)); // 중점을 중심으로 한 회전
        if (getBackgroundType() == BackgroundType.EMPTY) g.drawPolygon(new int[] {(int)point[0].getX(), (int)point[1].getX(), (int)point[2].getX()}, new int[] {(int)point[0].getY(), (int)point[1].getY(), (int)point[2].getY()}, 3);
        else if (getBackgroundType() == BackgroundType.FILL) g.fillPolygon(new int[] {(int)point[0].getX(), (int)point[1].getX(), (int)point[2].getX()}, new int[] {(int)point[0].getY(), (int)point[1].getY(), (int)point[2].getY()}, 3);
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
}
