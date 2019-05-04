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
        LayerListScrollPane.setMinimumSize(new Dimension(200, 300));
        LayerListScrollPane.setPreferredSize(new Dimension(200,1000)); // LayerList의 크기 지정
        layerList.setCellRenderer(new LayerListRenderer());
        
        // 툴바 설정
        moveSelectedLayerFrontButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/up_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));   
        moveSelectedLayerFrontButton.setToolTipText("선택한 레이어를 한 칸 위로 올립니다.");
        moveSelectedLayerFrontButton.setPreferredSize(new Dimension(28, 28)); 
        moveSelectedLayerBackButton = new JButton(new ImageIcon(new ImageIcon("src/caupaint/source/icon/down_arrow.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
        moveSelectedLayerBackButton.setToolTipText("선택한 레이어를 한 칸 아래로 내립니다.");   
        moveSelectedLayerBackButton.setPreferredSize(new Dimension(28, 28));
        sidebarToolBarPanel.add(moveSelectedLayerFrontButton);
        sidebarToolBarPanel.add(moveSelectedLayerBackButton);

        // 리스너에 등록함
        moveSelectedLayerFrontButton.addActionListener((ActionListener) new ButtonClickedActionListener());
        moveSelectedLayerBackButton.addActionListener(new ButtonClickedActionListener());

        LayerListScrollPane.add(layerList);

        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // 안쪽 여백 설정

        addGrid(layerListLabel, 0, 0);
        addGrid(sidebarToolBarPanel, 1, 0);
        addGrid(LayerListScrollPane, 2, GridBagConstraints.BOTH);
        
        layerListSelectionListener = new LayerListSelectionListener();
        layerList.addListSelectionListener(layerListSelectionListener);
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
        }
    }
    
    /*
    ** 그리드 레이아웃 관련 메소드
    */
    private void addGrid(Component c, int gridy, int weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = weighty;
        Gbag.setConstraints(c, gbc);
        add(c);
    }
    
}
