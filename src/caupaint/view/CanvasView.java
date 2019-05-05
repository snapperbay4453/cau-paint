
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;

public class CanvasView extends JPanel implements CanvasContainerObserver{
    
    private CanvasContainer canvasContainer;
    private Controller controller;
    
    private MouseAdapter canvasViewMouseAdapter;
    
    /*
    ** 생성자
    */
    public CanvasView(CanvasContainer canvasContainer, Controller controller) {
        this.setBackground(controller.getCanvasBackgroundColor());
        this.canvasContainer = canvasContainer;
        this.controller = controller;
        
        canvasContainer.registerCanvasContainerObserver(this); // CayerContainerObserver를 구현하는 클래스에 옵저버로 등록
        
        this.setPreferredSize(new Dimension((int)controller.getCanvasSize().getX(), (int)controller.getCanvasSize().getY())); // Controller를 통해 CanvasContainer에 저장된 Canvas 크기 정보를 불러옴
        
        canvasViewMouseAdapter = new CanvasViewMouseAdapter();
        this.addMouseListener(canvasViewMouseAdapter);
        this.addMouseMotionListener(canvasViewMouseAdapter);
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        if (!canvasContainer.getShapeLayerArrayList().isEmpty()) {
            for(int i = 0; i <= canvasContainer.getShapeLayerArrayList().size() - 1; i++){
                if (canvasContainer.getShapeLayerArrayList().get(i).getIsVisible() == true) canvasContainer.getShapeLayerArrayList().get(i).draw(g); // isVisible 값이 true일 때만 도형을 나타냄
            }
        }
    }
        
    class CanvasViewMouseAdapter extends MouseAdapter{
        public void mousePressed(MouseEvent event) { controller.CanvasViewMousePressedEventHandler(event); }
        public void mouseReleased(MouseEvent event) { controller.CanvasViewMouseReleasedEventHandler(event); }
        public void mouseDragged(MouseEvent event) { controller.CanvasViewMouseDraggedEventHandler(event); }
    }
    
    /*
    ** 옵저버 관련 메소드
    */
    @Override
    public void updateCanvasContainer() {
        this.setPreferredSize(new Dimension((int)controller.getCanvasSize().getX(), (int)controller.getCanvasSize().getY()));
        this.setBackground(controller.getCanvasBackgroundColor());
        this.repaint();
    }
    
}
