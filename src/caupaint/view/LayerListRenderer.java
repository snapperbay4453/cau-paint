package caupaint.view;
import caupaint.model.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.*;

public class LayerListRenderer extends JPanel implements ListCellRenderer<ShapeLayer> {

    private JLabel indexLabel;
    private JLabel iconLabel;
    private JLabel nameLabel;
    
    public LayerListRenderer() {
        indexLabel = new JLabel();
        iconLabel = new JLabel();
        nameLabel = new JLabel();
        add(indexLabel, BorderLayout.WEST);
        add(iconLabel, BorderLayout.WEST);
        add(nameLabel, BorderLayout.CENTER);
    
    }
    
    public Component getListCellRendererComponent(JList<? extends ShapeLayer> list, ShapeLayer shapeLayer, int index, boolean isSelected, boolean cellHasFocus) {
        indexLabel.setText(Integer.toString(index));
        switch(shapeLayer.getRealShapeType()) {
            case LINE:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/line.png"));
                break;
            case RECTANGLE:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/rectangle.png"));
                break;
            case ELLIPSE:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/ellipse.png"));
                break;
            case TRIANGLE:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/triangle.png"));
                break;
            case RHOMBUS:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/rhombus.png"));
                break;
            default:
                iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/shape.png"));
                break;
        }
        
        nameLabel.setText(shapeLayer.getName());
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        if(isSelected) setBackground(list.getSelectionBackground());
        else setBackground(list.getBackground());
        
        return this;
    }
}
