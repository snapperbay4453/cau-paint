package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Sidebar extends JPanel {
    
    private LayerContainer layerContainer;
    private Controller controller;
    
    private JLabel layerListLabel;
    private JPanel sidebarToolBarPanel;
    private JButton moveSelectedLayerFrontButton;
    private JButton moveSelectedLayerBackButton;
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
        refreshLayerList(); // layerList에 Vector 형식 데이터 입력
        
        // 툴바
        sidebarToolBarPanel = new JPanel();  
        moveSelectedLayerFrontButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/up_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));   
        moveSelectedLayerFrontButton.setToolTipText("선택한 레이어를 한 칸 위로 올립니다.");
        moveSelectedLayerFrontButton.setPreferredSize(new Dimension(28, 28)); 
        moveSelectedLayerBackButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/down_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        moveSelectedLayerBackButton.setToolTipText("선택한 레이어를 한 칸 아래로 내립니다.");   
        moveSelectedLayerBackButton.setPreferredSize(new Dimension(28, 28));
        sidebarToolBarPanel.add(moveSelectedLayerFrontButton);
        sidebarToolBarPanel.add(moveSelectedLayerBackButton);
        
        scrollableLayerListPane.setPreferredSize(new Dimension(200,550));
        layerList.setCellRenderer(new LayerListRenderer());
        
        // 리스너에 등록함
        moveSelectedLayerFrontButton.addActionListener(new ButtonClickedActionListener());
        moveSelectedLayerBackButton.addActionListener(new ButtonClickedActionListener());
        
        this.add(layerListLabel, BorderLayout.NORTH);
        this.add(sidebarToolBarPanel, BorderLayout.NORTH);
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
    class ButtonClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == moveSelectedLayerFrontButton) controller.swapShapeLayer(layerList.getSelectedIndex(), layerList.getSelectedIndex() - 1);
            else if (event.getSource() == moveSelectedLayerBackButton) controller.swapShapeLayer(layerList.getSelectedIndex(), layerList.getSelectedIndex() + 1);
        }
    }
}
