
package caupaint.model;
import java.awt.*;

public class Oval extends Shape{
   
    public Oval(Point position, Point size, Color color, int degree) {
        super(position, size, color, degree);
    }
    public Oval(Point position, Point size) {
        super(position, size);
    }
    public Oval() {
        super();
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.fillOval(getPositionX(), getPositionY(), getSizeX(), getSizeY());
    }
}
