package caupaint.view;
import caupaint.model.*;
import caupaint.model.Enum.ShapeType;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;

public class LayerListRenderer extends JPanel implements ListCellRenderer<ShapeLayer> {

    private CanvasContainer canvasContainer;
    
    private JLabel indexLabel;
    private JLabel shapeIconLabel;
    private JLabel nameLabel;
    private JLabel sizeLabel;
    private JPanel nameAndSizePanel;
    private JLabel isVisibleIconLabel;
    
    public LayerListRenderer(CanvasContainer canvasContainer) {
        this.canvasContainer = canvasContainer;
        
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
        if (shapeLayer.getIsVisible() == false) isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "invisible.png").getImage().getScaledInstance((int)Constant.defaultToggleSelectedLayerVisibleButtonSize.getWidth(), (int)Constant.defaultToggleSelectedLayerVisibleButtonSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        else isVisibleIconLabel.setIcon(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "null.png").getImage().getScaledInstance((int)Constant.defaultToggleSelectedLayerVisibleButtonSize.getWidth(), (int)Constant.defaultToggleSelectedLayerVisibleButtonSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        
        indexLabel.setText(Integer.toString(index));
        shapeIconLabel.setIcon(new ImageIcon(Constant.defaultIconDirectoryPath + shapeLayer.getIconFileName()));
        
        nameLabel.setText(shapeLayer.getName());
        nameLabel.setMaximumSize(Constant.defaultLayerListLabelSize);
        nameLabel.setPreferredSize(Constant.defaultLayerListLabelSize);
        if (shapeLayer instanceof TextLayer) sizeLabel.setText(((TextLayer)shapeLayer).getFontName() + ", " + ((TextLayer)shapeLayer).getFontSize() + "pt"); // 텍스트일 경우 폰트 크기를 표시
        else  sizeLabel.setText((int)shapeLayer.getBoundingBox().getSize().getWidth() + " x " + (int)shapeLayer.getBoundingBox().getSize().getHeight());
        sizeLabel.setMaximumSize(Constant.defaultLayerListLabelSize);
        sizeLabel.setPreferredSize(Constant.defaultLayerListLabelSize);
        
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5)); // 여백 (10, 5)
        if(index == canvasContainer.getSelectedLayerIndex()) setBackground(list.getSelectionBackground()); // 선택된 리스트를 강조 표시함
        else setBackground(list.getBackground());
        
        return this;
    }
}
