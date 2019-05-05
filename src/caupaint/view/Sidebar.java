package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;

public class Sidebar extends JPanel {
    
    private LayerContainer layerContainer;
    private Controller controller;
    
    private GridBagLayout Gbag;
    
    private JPanel sidebarToolBarPanel;
    private JButton moveSelectedLayerFrontButton;
    private JButton moveSelectedLayerBackButton;
    private JButton renameSelectedLayerButton;
    private JButton copySelectedLayerButton;
    private JButton deleteSelectedLayerButton;
    private JLabel layerListLabel;
    private ScrollPane LayerListScrollPane;

    private JList<ShapeLayer> layerList;
    
    private LayerListSelectionListener layerListSelectionListener;

    /*
    ** 생성자
    */
    public Sidebar(LayerContainer layerContainer, Controller controller) {
        this.layerContainer = layerContainer;
        this.controller = controller;
        
        layerListLabel = new JLabel();
        sidebarToolBarPanel = new JPanel(); 
        LayerListScrollPane = new ScrollPane();
        layerList = new JList();
        
        // 그리드백 레아아웃 초기 설정
        Gbag = new GridBagLayout();
        this.setLayout(Gbag);
        
        layerListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        refreshLayerList(); // layerList에 Vector 형식 데이터 입력
        LayerListScrollPane.setMinimumSize(new Dimension(300, 300));
        LayerListScrollPane.setPreferredSize(new Dimension(300,1000)); // LayerList의 크기 지정

        layerList.setCellRenderer(new LayerListRenderer());
        
        // 툴바 설정
        moveSelectedLayerFrontButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/up_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));   
        moveSelectedLayerFrontButton.setToolTipText("선택한 레이어를 한 칸 위로 올립니다.");
        moveSelectedLayerFrontButton.setPreferredSize(new Dimension(28, 28)); 
        moveSelectedLayerBackButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/down_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        moveSelectedLayerBackButton.setToolTipText("선택한 레이어를 한 칸 아래로 내립니다.");   
        moveSelectedLayerBackButton.setPreferredSize(new Dimension(28, 28));
        renameSelectedLayerButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/rename.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        renameSelectedLayerButton.setToolTipText("선택한 레이어의 이름을 변경합니다."); 
        renameSelectedLayerButton.setPreferredSize(new Dimension(28, 28));
        copySelectedLayerButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/copy.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        copySelectedLayerButton.setToolTipText("선택한 레이어를 복제합니다.");
        copySelectedLayerButton.setPreferredSize(new Dimension(28, 28));
        deleteSelectedLayerButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/delete.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        deleteSelectedLayerButton.setToolTipText("선택한 레이어를 삭제합니다.");
        deleteSelectedLayerButton.setPreferredSize(new Dimension(28, 28));
        sidebarToolBarPanel.add(moveSelectedLayerFrontButton);
        sidebarToolBarPanel.add(moveSelectedLayerBackButton);
        sidebarToolBarPanel.add(renameSelectedLayerButton);
        sidebarToolBarPanel.add(copySelectedLayerButton);
        sidebarToolBarPanel.add(deleteSelectedLayerButton);
        
        // 리스너에 등록함
        moveSelectedLayerFrontButton.addActionListener((ActionListener) new ButtonClickedActionListener());
        moveSelectedLayerBackButton.addActionListener(new ButtonClickedActionListener());
        renameSelectedLayerButton.addActionListener(new ButtonClickedActionListener());
        copySelectedLayerButton.addActionListener(new ButtonClickedActionListener());
        deleteSelectedLayerButton.addActionListener(new ButtonClickedActionListener());
                
        LayerListScrollPane.add(layerList);

        //this.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35)); // 안쪽 여백 설정
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 그리드백에 배치함
        addGrid(layerListLabel, 0, 0);
        addGrid(sidebarToolBarPanel, 1, 0);
        addGrid(LayerListScrollPane, 2, GridBagConstraints.BOTH);
        
        layerList.addListSelectionListener(new LayerListSelectionListener());
    }
    
    public int getLayerListSelectedIndex() {
        return layerList.getSelectedIndex();
    }
    public void refreshLayerList() {
        layerList.setListData(controller.getLayerArrayListToVector());
        layerListLabel.setText("총 " + layerContainer.getVector().size() + "개의 레이어");
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
            else if (event.getSource() == renameSelectedLayerButton) controller.renameShapeLayer(layerList.getSelectedIndex());
            else if (event.getSource() == copySelectedLayerButton) controller.copyShapeLayer(layerList.getSelectedIndex());
            else if (event.getSource() == deleteSelectedLayerButton) controller.deleteShapeLayer(layerList.getSelectedIndex());
        }
    }
    
    /*
    ** 그리드 레이아웃 관련 메소드
    */
    private void addGrid(Component c, int gridY, int weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = weightY;
        Gbag.setConstraints(c, gbc);
        add(c);
    }
    
}
