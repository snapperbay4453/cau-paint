package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.*;
import javax.swing.event.*;

public class Sidebar extends JPanel {
    
    private Layer layer;
    private Controller controller;
    
    private JLabel layerListLabel;
    private ScrollPane scrollableLayerListPane;
    private JList<Shape> layerList;
    
    private LayerListSelectionListener layerListSelectionListener;

    /*
    ** 생성자
    */
    public Sidebar(Layer layer, Controller controller) {
        this.layer = layer;
        this.controller = controller;
        
        layerListLabel = new JLabel();
        scrollableLayerListPane = new ScrollPane();
        layerList = new JList();
        
        layerListLabel.setText("현재 도형");
        
        scrollableLayerListPane.setPreferredSize(new Dimension(200,550));
        refreshLayerListData(); // layerList에 Vector 형식 데이터 입력
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
    public void refreshLayerListData() {
        layerList.setListData(controller.getLayerArrayListToVector());
        layerListLabel.setText("현재 도형 (" + layer.getVector().size() + "개)");
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class LayerListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e){
            controller.setLastSelectedIndex(layerList.getSelectedIndex());
        }
    }
}
