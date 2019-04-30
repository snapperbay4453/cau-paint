
package caupaint.model;
import java.awt.*;

public class Rectangle extends Shape{
    
private Point position;
private Point size;
private Color color;
private int degree;
    
    public Rectangle(Point position, Point size, Color color, int degree) {
        super(position, size, color, degree);
    }
    public Rectangle(Point position, Point size) {
        super(position, size);
        this.position = position;
        this.size = size;
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }
    public Rectangle() {
        super();
        this.position = new Point(0, 0);
        this.size =  new Point(100, 100);
        this.color = new Color(0, 0, 0);
        this.degree = 0;
    }

    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getPositionX(), getPositionY(), getSizeX(), getSizeY());
    }
}
