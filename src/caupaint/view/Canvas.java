package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.Color;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;

public class Canvas extends JPanel {
    
    private Layer layer;
    private Variable variable;
    private Controller controller;
    
    private MouseAdapter canvasMouseAdapter;
    
    /*
    ** 생성자
    */
    public Canvas(Layer layer, Variable variable, Controller controller) {
        this.setBackground(Color.white);
        this.layer = layer;
        this.variable = variable;
        this.controller = controller;
        
        canvasMouseAdapter = new CanvasMouseAdapter();
    }
    
    /*
    ** 마우스 리스너 활성화/비활성화 메소드
    */
    public void activateCanvasMouseAdapter(){
        this.addMouseListener(canvasMouseAdapter);
        this.addMouseMotionListener(canvasMouseAdapter);
    }
    public void deactivateCanvasMouseAdapter(){
        this.removeMouseListener(canvasMouseAdapter);
        this.removeMouseMotionListener(canvasMouseAdapter);
    }
    
    
    /*
    ** 그래픽 관련 메소드
    */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if (!layer.getArrayList().isEmpty()) {
            for(int i = 0; i <= layer.getArrayList().size() - 1; i++){
                layer.getShape(i).draw(g);
            }
        }
        if (variable.getTempShape() != null) {
            variable.getTempShape().draw(g);
        }
        
    }
        
    class CanvasMouseAdapter extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            //controller.addShape(new Point(e.getX(), e.getY()), new Point(0,0));
            variable.setPointStart(new Point(e.getX(), e.getY()));
            variable.setPointEnd(new Point(e.getX(), e.getY()));
            variable.makeTempShape();
        }
        public void mouseReleased(MouseEvent e) {
            //controller.modifyShape(new Point(e.getX(), e.getY()));
            variable.finalizeTempShape();
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {
            //controller.modifyShape(new Point(e.getX(), e.getY()));
            variable.setPointEnd(new Point(e.getX(), e.getY()));
            variable.refreshTempShape();
        }
    }

}
