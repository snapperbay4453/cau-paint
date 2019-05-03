
package caupaint.model;
import java.awt.*;
import java.awt.geom.*;

public class LineLayer extends LineBasedShapeLayer{
    
    public LineLayer(Point point1, Point point2, Color color, int degree) {
        super(point1, point2, color, degree);
        setShape(new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
    }
    public LineLayer(Point point1, Point point2) {
        super(point1, point2);
        setShape(new Line2D.Double(point1.getX(), point1.getY(), point2.getX(), point2.getY()));
    }
    public LineLayer() {
        super();
        setShape(new Line2D.Double(0, 0, 0, 0));
    }
    
    public void translate(double tx, double ty) {
        setX1(getX1() + tx);
        setY1(getY1() + ty);
        setX2(getX2() + tx);
        setY2(getY2() + ty);
    }
    
    public void create(Point recentMousePosition, Point currentMousePosition) {
        setX2(currentMousePosition.getX());
        setY2(currentMousePosition.getY());
    }
    public void scale(Point recentMousePosition, Point currentMousePosition) {
        double tx = currentMousePosition.getX() - recentMousePosition.getX();   double ty = currentMousePosition.getY() - recentMousePosition.getY();
        if (currentMousePosition.getX() >= ((getX1() + getX2()) / 2) && currentMousePosition.getY() >= ((getY1() + getY2()) / 2)) { // 1사분면
            if (getX1() <= getX2()){
                setX1(getX1() - tx);
                setX2(getX2() + tx);
            }
            else {
                setX1(getX1() + tx);
                setX2(getX2() - tx);
            }
            if (getY1() <= getY2()){
                setY1(getY1() - ty);
                setY2(getY2() + ty);
            }
            else {
                setY1(getY1() + ty);
                setY2(getY2() - ty);
            }
        }
        else if (currentMousePosition.getX() < ((getX1() + getX2()) / 2) && currentMousePosition.getY() >= ((getY1() + getY2()) / 2)) { // 2사분면
            if (getX1() < getX2()){
                setX1(getX1() + tx);
                setX2(getX2() - tx);
            }
            else {
                setX1(getX1() - tx);
                setX2(getX2() + tx);
            }
            if (getY1() <= getY2()){
                setY1(getY1() - ty);
                setY2(getY2() + ty);
            }
            else {
                setY1(getY1() + ty);
                setY2(getY2() - ty);
            }
        }
        else if (currentMousePosition.getX() < ((getX1() + getX2()) / 2) && currentMousePosition.getY() < ((getY1() + getY2()) / 2)) { // 3사분면
            if (getX1() <= getX2()){
                setX1(getX1() + tx);
                setX2(getX2() - tx);
            }
            else {
                setX1(getX1() - tx);
                setX2(getX2() + tx);
            }
            if (getY1() <= getY2()){
                setY1(getY1() + ty);
                setY2(getY2() - ty);
            }
            else {
                setY1(getY1() - ty);
                setY2(getY2() + ty);
            }
        }
        else if (currentMousePosition.getX() >= ((getX1() + getX2()) / 2) && currentMousePosition.getY() < ((getY1() + getY2()) / 2)) { // 4사분면
            if (getX1() <= getX2()){
                setX1(getX1() - tx);
                setX2(getX2() + tx);
            }
            else {
                setX1(getX1() + tx);
                setX2(getX2() - tx);
            }
            if (getY1() <= getY2()){
                setY1(getY1() + ty);
                setY2(getY2() - ty);
            }
            else {
                setY1(getY1() - ty);
                setY2(getY2() + ty);
            }
        }
    }
    public void rotate(Point currentMousePosition, Point recentMousePosition){
        setRadianAngle(getRadianAngle()
                    - (Math.atan2
                         (currentMousePosition.getY() - ((getY1() + getY2()) * 0.5), currentMousePosition.getX() - ((getX1() + getX2()) * 0.5))
                     - Math.atan2
                          (recentMousePosition.getY() - ((getY1() + getY2()) * 0.5), recentMousePosition.getX() - ((getX1() + getX2()) * 0.5))
                   )
                );
    }
    
    
    public double getX1(){
        return ((Line2D)getShape()).getX1();
    }
    public double getY1(){
        return ((Line2D)getShape()).getY1();
    }
    public double getX2(){
        return ((Line2D)getShape()).getX2();
    }
    public double getY2(){
        return ((Line2D)getShape()).getY2();
    }
    
    public String getIconName() {
        return "line";
    }
    
    public void setX1(double x){
        ((Line2D)getShape()).setLine(x, getY1(), getX2(), getY2());
    }
    public void setY1(double y){
        ((Line2D)getShape()).setLine(getX1(), y, getX2(), getY2());
    }
    public void setX2(double x){
        ((Line2D)getShape()).setLine(getX1(), getY1(), x, getY2());
    }
    public void setY2(double y){
        ((Line2D)getShape()).setLine(getX1(), getY1(), getX2(), y);
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g.setColor(getColor());
        g2d.rotate(getRadianAngle(), (getX1() + getX2()) * 0.5, (getY1() + getY2()) * 0.5);
        g.drawLine((int)getX1(), (int)getY1(), (int)getX2(), (int)getY2());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
}
