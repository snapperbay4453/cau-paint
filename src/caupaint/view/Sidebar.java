package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.event.*;

public class Sidebar extends JPanel {
    
    private Layer layer;
    private Controller controller;
    
    private JList layerList;
    
    private LayerListSelectionListener layerListSelectionListener;

    /*
    ** 생성자
    */
    public Sidebar(Layer layer, Controller controller) {
        this.layer = layer;
        this.controller = controller;
        
        layerList = new JList();
        layerList.setListData(controller.getLayerArrayListToVector());
        this.add(layerList);
        this.setPreferredSize(new Dimension(240,500));
        
        layerListSelectionListener = new LayerListSelectionListener();
        layerList.addListSelectionListener(layerListSelectionListener);
    }
    
    public int getLayerListSelectedIndex() {
        return layerList.getSelectedIndex();
    }
    public void refreshLayerListData() {
        layerList.setListData(layer.getVector());
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class LayerListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent e){
            if (layerList.getSelectedIndex() != -1) JOptionPane.showMessageDialog(null, "선택된 Shape : " + layerList.getSelectedIndex(), "정보", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
