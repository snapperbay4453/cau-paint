package caupaint.view;
import caupaint.model.*;
import caupaint.model.Enum.ShapeType;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;

public class LayerListRenderer extends JPanel implements ListCellRenderer<ShapeLayer> {

    private JLabel indexLabel;
    private JLabel shapeIconLabel;
    private JLabel nameLabel;
    private JLabel sizeLabel;
    private JPanel nameAndSizePanel;
    private JLabel isVisibleIconLabel;
    
    public LayerListRenderer() {
        indexLabel = new JLabel();
        shapeIconLabel = new JLabel();
        nameAndSizePanel = new JPanel();
        nameLabel = new JLabel();
        sizeLabel = new JLabel();
        isVisibleIconLabel = new JLabel();
        
        this.add(isVisibleIconLabel, BorderLayout.WEST);
        this.add(indexLabel, BorderLayout.WEST);
        this.add(shapeIconLabel, BorderLayout.WEST);
        nameAndSizePanel.setOpaque(false);
        nameAndSizePanel.setLayout(new BoxLayout(nameAndSizePanel, BoxLayout.Y_AXIS));
        nameAndSizePanel.add(nameLabel, BorderLayout.NORTH);
        nameAndSizePanel.add(sizeLabel,  BorderLayout.CENTER);
        this.add(nameAndSizePanel, BorderLayout.CENTER);
        
    }
    
    public Component getListCellRendererComponent(JList<? extends ShapeLayer> list, ShapeLayer shapeLayer, int index, boolean isSelected, boolean cellHasFocus) {
        
        // 레이어 숨김 여부 표시
        if (shapeLayer.getIsVisible() == false) isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "invisible.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        else isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "null.png").getImage().getScaledInstance((int)new Dimension(14, 14).getWidth(), (int)new Dimension(14, 14).getHeight(), java.awt.Image.SCALE_SMOOTH)));
        
        indexLabel.setText(Integer.toString(index));
        switch(shapeLayer.getRealShapeType()) { // 레이어의 도형 종류 표시
            case LINE:       shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "line.png"));      break;
            case POLYLINE:   shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "polyline.png"));  break;
            case PEN:        shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "pen.png"));       break;
            case RECTANGLE:  shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png")); break;
            case ELLIPSE:    shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));   break;
            case TRIANGLE:   shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));  break;
            case RHOMBUS:    shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));   break;
            case TEXT:       shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "text.png"));      break;
            case IMAGE:      shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "image.png"));     break;
            default:         shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "shape.png"));     break;
        }

        nameLabel.setText(shapeLayer.getName());
        nameLabel.setMaximumSize(new Dimension(190, 20));
        nameLabel.setPreferredSize(new Dimension(190, 20));
        if  ((shapeLayer.getRealShapeType() == ShapeType.POLYLINE) && (((PolylineLayer)shapeLayer).getIsFinishedInitializing() == false)) {
            sizeLabel.setText((int)shapeLayer.getBoundingBox().getSize().getWidth() + " x " + (int)shapeLayer.getBoundingBox().getSize().getHeight() + " (그리는 중)"); // 현재 폴리선이 완전히 그려지지 않은 경우, 그리는 중이라고 표시함
            }
        else if (shapeLayer.getRealShapeType() == ShapeType.TEXT) sizeLabel.setText(((TextLayer)shapeLayer).getFontName() + ", " + ((TextLayer)shapeLayer).getFontSize() + "pt"); // 텍스트일 경우 폰트 크기를 표시
        else  sizeLabel.setText((int)shapeLayer.getBoundingBox().getSize().getWidth() + " x " + (int)shapeLayer.getBoundingBox().getSize().getHeight());
        sizeLabel.setMaximumSize(new Dimension(190, 20));
        sizeLabel.setPreferredSize(new Dimension(190, 20));
        
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        if(isSelected) setBackground(list.getSelectionBackground()); // 선택된 리스트를 강조 표시함
        else setBackground(list.getBackground());
        
        return this;
    }
}
