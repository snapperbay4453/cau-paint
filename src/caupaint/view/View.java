
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class View implements LayerObserver{
    
    private Layer layer;
    private Controller controller;
    
    private JFrame frame;
    private Canvas canvas;
    private JPanel header;
    private JButton addShapeButton;
    private JButton deleteLastShapeButton;
    private JButton clearButton;
    
    
    public View(Controller controller, Layer layer) {
        this.controller = controller;
        this.layer = layer;
        
        layer.registerLayerObserver(this);
    }
    
    public void createView() {
        frame = new JFrame("View");
        canvas = new Canvas(layer);
        header = new JPanel();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(new Dimension(600, 600));
        
        addShapeButton = new JButton("도형 추가");
        deleteLastShapeButton = new JButton("마지막 도형 삭제");
        clearButton = new JButton("전체 삭제");
        addShapeButton.addActionListener(new AddShapeListener());
        deleteLastShapeButton.addActionListener(new DeleteLastShapeListener());
        clearButton.addActionListener(new ClearListener());
        
        header.add(addShapeButton);
        header.add(deleteLastShapeButton);
        header.add(clearButton);
        
        frame.getContentPane().add(BorderLayout.NORTH, header);
        frame.getContentPane().add(BorderLayout.CENTER, canvas); 
    
        frame.setTitle("CauPaint");
        frame.setSize(600,600);
        frame.setVisible(true);    
    }
    
    class AddShapeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            controller.addShape(new Rectangle());
        }
    }
    class DeleteLastShapeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            controller.deleteLastShape();
        }
    }
    class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            controller.clearLayer();
        }
    }
    
    public void updateLayer() {
        frame.repaint();
    }
    
}
