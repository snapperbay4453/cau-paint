package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.*;
import javax.swing.event.*;

public class Sidebar extends JPanel {
    
    private LayerContainer layerContainer;
    private Controller controller;
    
    private JLabel layerListLabel;
    private ScrollPane scrollableLayerListPane;
    private JList<ShapeLayer> layerList;
    
    private LayerListSelectionListener layerListSelectionListener;

    /*
    ** 생성자
    */
    public Sidebar(LayerContainer layerContainer, Controller controller) {
        this.layerContainer = layerContainer;
        this.controller = controller;
        
        layerListLabel = new JLabel();
        scrollableLayerListPane = new ScrollPane();
        layerList = new JList();
        
        layerListLabel.setText("현재 도형");
        
        scrollableLayerListPane.setPreferredSize(new Dimension(200,550));
        refreshLayerList(); // layerList에 Vector 형식 데이터 입력
        layerList.setCellRenderer(new LayerListRenderer());
        
        this.add(layerListLabel, BorderLayout.NORTH);
        scrollableLayerListPane.add(layerList, BorderLayout.CENTER);
        this.add(scrollableLayerListPane);
        this.setPreferredSize(new Dimension(240,550));
        
        layerListSelectionListener = new LayerListSelectionListener();
        layerList.addListSelectionListener(layerListSelectionListener);
    }
    
    public int getLayerListSelectedIndex() {
        return layerList.getSelectedIndex();
    }
    public void refreshLayerList() {
        layerList.setListData(controller.getLayerArrayListToVector());
        layerListLabel.setText("현재 도형 (" + layerContainer.getVector().size() + "개)");
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class LayerListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e){
            controller.setLastSelectedLayerIndex(layerList.getSelectedIndex());
        }
    }
}
