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
    private Controller controller;
    
    private MouseAdapter canvasMouseAdapter;
    private boolean isCanvasMouseAdapterActivated;
    
    /*
    ** 생성자
    */
    public Canvas(Layer layer, Controller controller) {
        this.setBackground(Color.white);
        this.layer = layer;
        this.controller = controller;
        
        canvasMouseAdapter = new CanvasMouseAdapter();
        isCanvasMouseAdapterActivated = false;
    }
    
    /*
    ** 마우스 리스너 활성화/비활성화 메소드
    */
    public void activateCanvasMouseAdapter(){
        if (isCanvasMouseAdapterActivated == false) {
            this.addMouseListener(canvasMouseAdapter);
            this.addMouseMotionListener(canvasMouseAdapter);
            isCanvasMouseAdapterActivated = true;
        }
    }
    public void deactivateCanvasMouseAdapter(){
        if (isCanvasMouseAdapterActivated == true) {
            this.removeMouseListener(canvasMouseAdapter);
            this.removeMouseMotionListener(canvasMouseAdapter);
            isCanvasMouseAdapterActivated = false;
        }
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
        if (controller.getTempShape() != null) {
            controller.getTempShape().draw(g);
        }
        
    }
        
    class CanvasMouseAdapter extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            controller.setPointStart(new Point(e.getX(), e.getY()));
            controller.setPointEnd(new Point(e.getX(), e.getY()));
            controller.makeTempShape();
        }
        public void mouseReleased(MouseEvent e) {
            controller.finalizeTempShape();
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {
            controller.setPointEnd(new Point(e.getX(), e.getY()));
            controller.refreshTempShape();
            
        }
    }

}
