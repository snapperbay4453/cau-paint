package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.observer.*;
import java.awt.Dimension;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;

public class Canvas extends JPanel implements LayerContainerObserver{
    
    private LayerContainer layerContainer;
    private Controller controller;
    
    private MouseAdapter canvasMouseAdapter;
    
    /*
    ** 생성자
    */
    public Canvas(LayerContainer layerContainer, Controller controller) {
        this.setBackground(controller.getCanvasBackgroundColor());
        this.layerContainer = layerContainer;
        this.controller = controller;
        
        layerContainer.registerLayerContainerObserver(this); // LayerContainerObserver를 구현하는 클래스에 옵저버로 등록
        
        this.setPreferredSize(new Dimension((int)controller.getCanvasSize().getX(), (int)controller.getCanvasSize().getY()));
        
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

    public void updateLayerContainer() {
        this.setPreferredSize(new Dimension((int)controller.getCanvasSize().getX(), (int)controller.getCanvasSize().getY()));
        this.setBackground(controller.getCanvasBackgroundColor());
        this.repaint();
    }
    
}
