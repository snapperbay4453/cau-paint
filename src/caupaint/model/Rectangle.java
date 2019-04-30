
package caupaint.model;
import java.awt.*;

public class Rectangle extends Shape{
   
    public Rectangle(Point position, Point size, Color color, int degree) {
        super(position, size, color, degree);
    }
    public Rectangle(Point position, Point size) {
        super(position, size);
    }
    public Rectangle() {
        super();
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getPositionX(), getPositionY(), getSizeX(), getSizeY());
    }
}
