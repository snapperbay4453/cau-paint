
package caupaint.model;
import java.awt.*;

public class Shape {
    private Point position;
    private Point size;
    private Color color;
    private int degree;
    
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
    
    public int getPositionX() {
        return (int)position.getX();
    }
    public int getPositionY() {
        return (int)position.getY();
    }
    public int getSizeX() {
        return (int)size.getX();
    }
    public int getSizeY() {
        return (int)size.getY();
    }
    public Color getColor() {
        return color;
    }
    public int getDegree() {
        return degree;
    }
    
    public void setPositionX(int x) {
        position.setLocation(x, this.getPositionY());
    }
    public void setPositionY(int y) {
        position.setLocation(this.getPositionX(), y);
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
    
    public void draw(Graphics g) {

    }
}
