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
        this.add(isVisibleIconLabel, BorderLayout.WEST);
        this.add(indexLabel, BorderLayout.WEST);
        this.add(shapeIconLabel, BorderLayout.WEST);
        this.add(nameLabel, BorderLayout.CENTER);
        
    }
    
    public Component getListCellRendererComponent(JList<? extends ShapeLayer> list, ShapeLayer shapeLayer, int index, boolean isSelected, boolean cellHasFocus) {
        
        // 레이어 숨김 여부 표시
        if (shapeLayer.getIsVisible() == false) isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "invisible.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        else isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "null.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        
        indexLabel.setText(Integer.toString(index));
        switch(shapeLayer.getRealShapeType()) { // 레이어의 도형 종류 표시
            case LINE:       shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "line.png"));      break;
            case RECTANGLE:  shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png")); break;
            case ELLIPSE:    shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));   break;
            case TRIANGLE:   shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));  break;
            case RHOMBUS:    shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));   break;
            default:         shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "shape.png"));     break;
        }
        
        nameLabel.setText(shapeLayer.getName());
        nameLabel.setMaximumSize(new Dimension(190, 40));
        nameLabel.setPreferredSize(new Dimension(190, 40));
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        if(isSelected) setBackground(list.getSelectionBackground()); // 선택된 리스트를 강조 표시함
        else setBackground(list.getBackground());
        
        return this;
    }
}
