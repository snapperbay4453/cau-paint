
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class View implements LayerObserver{
    
    private Layer layer;
    private Controller controller;
    
    private JFrame frame;
    private Canvas canvas;
    private JPanel header;
    private JButton idleButton;
    private JButton addShapeButton;
    private JButton drawRectangleButton;
    private JButton deleteLastShapeButton;
    private JButton clearButton;

    /*
    ** 생성자
    */
    public View(Controller controller, Layer layer) {
        this.controller = controller;
        this.layer = layer;
        
        layer.registerLayerObserver(this); // LayerObserver를 구현하는 클래스에 옵저버로 등록
    }
    
    /*
    ** 윈도우 생성
    */
    public void createView() {
        frame = new JFrame("View");
        canvas = new Canvas(layer, controller);
        header = new JPanel();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        idleButton = new JButton("대기");
        drawRectangleButton = new JButton("직사각형 그리기");
        deleteLastShapeButton = new JButton("마지막 도형 삭제");
        clearButton = new JButton("전체 삭제");
        
        idleButton.addActionListener(new ButtonClickedListener());
        drawRectangleButton.addActionListener(new ButtonClickedListener());
        deleteLastShapeButton.addActionListener(new ButtonClickedListener());
        clearButton.addActionListener(new ButtonClickedListener());
        
        header.add(idleButton);
        header.add(drawRectangleButton);
        header.add(deleteLastShapeButton);
        header.add(clearButton);
        
        frame.getContentPane().add(BorderLayout.NORTH, header);
        frame.getContentPane().add(BorderLayout.CENTER, canvas);
        
        frame.setTitle("CauPaint");
        frame.setSize(600,600);
        frame.setVisible(true);    
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class ButtonClickedListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == idleButton) canvas.deactivateCanvasMouseAdapter();
            else if (event.getSource() == drawRectangleButton) canvas.activateCanvasMouseAdapter();
            else if (event.getSource() == deleteLastShapeButton) controller.deleteLastShape();
            else if (event.getSource() == clearButton) controller.clearLayer();
        }
    }

   /*
    ** 옵저버 관련 메소드
    */
    public void updateLayer() {
        frame.repaint();
    }
    
}
