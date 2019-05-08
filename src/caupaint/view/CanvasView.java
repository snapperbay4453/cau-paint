
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.Color;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class CanvasView extends JPanel implements CanvasContainerObserver{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    private Controller controller;
    
    private MouseAdapter canvasViewMouseAdapter;
    
    /*
    ** 생성자
    */
    public CanvasView(CanvasContainer canvasContainer, Variable variable, Controller controller) {
        this.setBackground(controller.getCanvasBackgroundColor());
        this.canvasContainer = canvasContainer;
        this.variable = variable;
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
            if ((variable.getFunctionType() == FunctionType.MOVE || variable.getFunctionType() == FunctionType.RESIZE || variable.getFunctionType() == FunctionType.ROTATE) 
            && variable.getLastSelectedLayerIndex() != -1 ) {
            // 레이어 변형 시 경계 박스를 표시함
                Graphics2D g2d = (Graphics2D)g;
                AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
                g.setColor(Color.GRAY);
                g2d.setStroke(Constant.defaultLayerSelectedLineBasicStroke);
                g.drawRect(
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getX(), 
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getY(),
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getWidth(),
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getHeight() // 외곽선 그리기
                );
                g.fillOval(
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getCenterX() - 5, 
                        (int)canvasContainer.getShapeLayerArrayList().get(variable.getLastSelectedLayerIndex()).getBoundingBox().getCenterY() - 5,
                        10,
                        10 // 중심점 그리기
                );
                g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
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
