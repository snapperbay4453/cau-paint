
package caupaint.model;
import java.awt.*;

public class Shape {
    private Point position;
    private Point size;
    private Color color;
    private int degree;
    
    /*
    ** 생성자
    */
    public Shape(Point position, Point size, Color color, int degree) {
        this.position = position;
        this.size = size;
        this.color = color;
        this.degree = degree;
    }
    public Shape(Point position, Point size) {
        this.position = position;
        this.size = size;
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }
    public Shape() {
        this.position = new Point(0, 0);
        this.size =  new Point(100, 100);
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }
    
    /*
    ** getter, setter
    */
    public Point getPosition() {
        return position;
    }
    public int getPositionX() { // int 반환 함수로 남겨둠
        return (int)position.getX();
    }
    public int getPositionY() { // int 반환 함수로 남겨둠
        return (int)position.getY();
    }
    public Point getSize() {
        return size;
    }
    public int getSizeX() { // int 반환 함수로 남겨둠
        return (int)size.getX();
    }
    public int getSizeY() { // int 반환 함수로 남겨둠
        return (int)size.getY();
    }
    public Color getColor() {
        return color;
    }
    public int getDegree() {
        return degree;
    }
    
    public void setPosition(Point position) {
        this.position.setLocation(position);
    }
    public void setPositionX(int x) {
        position.setLocation(x, this.getPositionY());
    }
    public void setPositionY(int y) {
        position.setLocation(this.getPositionX(), y);
    }
    public void setSize(Point size) {
        this.size.setLocation(size);
    }
    public void setSizeX(int x) {
        size.setLocation(x, this.getPositionY());
    }
    public void setSizeY(int y) {
        size.setLocation(this.getPositionX(), y);
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {

    }
}
