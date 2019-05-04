package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;

public class Canvas extends JPanel {
    
    private LayerContainer layerContainer;
    private Controller controller;
    
    private MouseAdapter canvasMouseAdapter;
    
    /*
    ** 생성자
    */
    public Canvas(LayerContainer layerContainer, Controller controller) {
        this.setBackground(Color.white);
        this.layerContainer = layerContainer;
        this.controller = controller;
        
        this.setPreferredSize(new Dimension(640, 480));
        
        canvasMouseAdapter = new CanvasMouseAdapter();
        this.addMouseListener(canvasMouseAdapter);
        this.addMouseMotionListener(canvasMouseAdapter);
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if (!layerContainer.getArrayList().isEmpty()) {
            for(int i = 0; i <= layerContainer.getArrayList().size() - 1; i++){
                layerContainer.getShapeLayer(i).draw(g);
            }
        }
        /*
        if (controller.getTempShapeLayer() != null) {
            controller.getTempShapeLayer().draw(g);
        }
        */
    }
        
    class CanvasMouseAdapter extends MouseAdapter{
        public void mousePressed(MouseEvent e) {
            controller.CanvasMousePressed(new Point(e.getX(), e.getY()));
        }
        public void mouseReleased(MouseEvent e) {
            controller.CanvasMouseReleased(new Point(e.getX(), e.getY()));
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {
            controller.CanvasMouseDragged(new Point(e.getX(), e.getY()));
        }
    }

}
