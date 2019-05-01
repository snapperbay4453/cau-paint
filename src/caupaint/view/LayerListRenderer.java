package caupaint.view;
import caupaint.model.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.*;

public class LayerListRenderer extends JPanel implements ListCellRenderer<Shape> {

    private JLabel indexLabel;
    private JLabel iconLabel;
    private JLabel sizeLabel;
    
    public LayerListRenderer() {
        indexLabel = new JLabel();
        iconLabel = new JLabel();
        sizeLabel = new JLabel();
        add(indexLabel, BorderLayout.WEST);
        add(iconLabel, BorderLayout.WEST);
        add(sizeLabel, BorderLayout.CENTER);
    
    }
    
    public Component getListCellRendererComponent(JList<? extends Shape> list, Shape shape, int index, boolean isSelected, boolean cellHasFocus) {
        indexLabel.setText(Integer.toString(index));
        iconLabel.setIcon(new ImageIcon("src/caupaint/source/icon/" + shape.getIconName() + ".png"));
        sizeLabel.setText((int)shape.getSize().getX() + " X " + (int)shape.getSize().getY());
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        if(isSelected) {
            setBackground(list.getSelectionBackground());
        }
        else {
            setBackground(list.getBackground());
        }
        
        return this;
    }
}
