package caupaint.view;
import caupaint.model.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class LayerListRenderer extends JPanel implements ListCellRenderer<ShapeLayer> {

    private JLabel indexLabel;
    private JLabel shapeIconLabel;
    private JLabel nameLabel;
    private JLabel isVisibleIconLabel;
    
    public LayerListRenderer() {
        indexLabel = new JLabel();
        shapeIconLabel = new JLabel();
        nameLabel = new JLabel();
        isVisibleIconLabel = new JLabel();
        add(isVisibleIconLabel, BorderLayout.WEST);
        add(indexLabel, BorderLayout.WEST);
        add(shapeIconLabel, BorderLayout.WEST);
        add(nameLabel, BorderLayout.CENTER);
        
    }
    
    public Component getListCellRendererComponent(JList<? extends ShapeLayer> list, ShapeLayer shapeLayer, int index, boolean isSelected, boolean cellHasFocus) {
        
        if (shapeLayer.getIsVisible() == false) isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "invisible.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        else isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "null.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        
        indexLabel.setText(Integer.toString(index));
        switch(shapeLayer.getRealShapeType()) {
            case LINE:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "line.png"));
                break;
            case RECTANGLE:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png"));
                break;
            case ELLIPSE:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));
                break;
            case TRIANGLE:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));
                break;
            case RHOMBUS:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));
                break;
            default:
                shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "shape.png"));
                break;
        }
        
        nameLabel.setText(shapeLayer.getName());

        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        if(isSelected) setBackground(list.getSelectionBackground());
        else setBackground(list.getBackground());
        
        return this;
    }
}
