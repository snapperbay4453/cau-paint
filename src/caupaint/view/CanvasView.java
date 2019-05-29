
package caupaint.view;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.Color;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class CanvasView extends JPanel implements CanvasContainerObserver{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    
    /*
    ** 생성자
    */
    public CanvasView(CanvasContainer canvasContainer, Variable variable) {
        this.setBackground(canvasContainer.getCanvasBackgroundColor());
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        
        //canvasContainer.registerCanvasContainerObserver(this); // CayerContainerObserver를 구현하는 클래스에 옵저버로 등록
        
        this.setPreferredSize(new Dimension((int)canvasContainer.getCanvasSize().getX(), (int)canvasContainer.getCanvasSize().getY())); // Controller를 통해 CanvasContainer에 저장된 Canvas 크기 정보를 불러옴
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
        
        if (canvasContainer.getTempShapeLayer() != null) { // tempShapeLayer가 있다면
            canvasContainer.getTempShapeLayer().draw(g);
        }
        
        if ((variable.getFunctionType() == FunctionType.SELECT || variable.getFunctionType() == FunctionType.MOVE || variable.getFunctionType() == FunctionType.RESIZE || variable.getFunctionType() == FunctionType.ROTATE) 
        && !canvasContainer.getShapeLayerArrayList().isEmpty() && canvasContainer.getSelectedLayerIndex() != -1) { // 도형 선택 또는 변형 기능을 선택했고, ShapeLayerArrayList가 비어있으며, 선택한 레이어가 있을 경우
        // 레이어 변형 시 경계 박스를 표시함
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
            g.setColor(Color.GRAY);
            g2d.rotate(canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getRadianAngle(),
                    canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getCentralPoint().getX(),
                    canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getCentralPoint().getY());
            g2d.setStroke(Constant.defaultLayerSelectedLineBasicStroke);
            g.drawRect(
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getX(), 
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getY(),
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getWidth(),
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getHeight() // 외곽선 그리기
            );
            g.fillOval(
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getCenterX() - 5, 
                    (int)canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getBoundingBox().getCenterY() - 5,
                    10,
                    10 // 중심점 그리기
            );
            g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
        }
    }
    
    /*
    ** 옵저버 관련 메소드 - 사용하지 않음
    */
    @Override
    public void updateCanvasContainer() {
        /*
        this.setPreferredSize(new Dimension((int)canvasContainer.getCanvasSize().getX(), (int)canvasContainer.getCanvasSize().getY()));
        this.setBackground(canvasContainer.getCanvasBackgroundColor());
        this.repaint();
        */
    }
    
}
