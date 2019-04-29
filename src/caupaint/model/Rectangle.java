
package caupaint.model;
import java.awt.*;

public class Rectangle extends Shape{
    
private Point position;
private Point size;
private Color color;
private int degree;
    
    /*
    ** 그래픽 관련 메소드
    */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getPositionX(), getPositionY(), getSizeX(), getSizeY());
    }
}
