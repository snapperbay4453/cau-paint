
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;

public class SidebarView extends JPanel implements CanvasContainerObserver, VariableObserver{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    private Controller controller;
    
    private GridBagLayout Gbag;
    
    private JPanel sidebarToolBarPanel;
    private JButton toggleSelectedLayerVisibleButton;
    private JButton moveSelectedLayerFrontButton;
    private JButton moveSelectedLayerBackButton;
    private JButton renameSelectedLayerButton;
    private JButton copySelectedLayerButton;
    private JButton deleteSelectedLayerButton;
    private JButton deleteAllLayerButton;
    private JLabel layerListLabel;
    private ScrollPane LayerListScrollPane;

    private JList<ShapeLayer> layerList;
    
    /*
    ** 생성자
    */
    public SidebarView(CanvasContainer canvasContainer, Variable variable, Controller controller) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.controller = controller;
        
        canvasContainer.registerCanvasContainerObserver(this); // CanvasContainerObserver를 구현하는 클래스에 옵저버로 등록
        variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록  
        
        layerListLabel = new JLabel();
        LayerListScrollPane = new ScrollPane();
        layerList = new JList();

        // 그리드백 레아아웃 초기 설정
        Gbag = new GridBagLayout();
        this.setLayout(Gbag);
        
        layerListLabel.setHorizontalAlignment(SwingConstants.CENTER);
        refreshLayerList(); // layerList에 Vector 형식 데이터 입력
        LayerListScrollPane.setMinimumSize(Constant.defaultLayerListScrollPaneSize);
        LayerListScrollPane.setPreferredSize(Constant.defaultLayerListScrollPaneSize); // LayerList의 크기 지정

        layerList.setCellRenderer(new LayerListRenderer(canvasContainer));
        
        createToolBar(); // 툴바에 아이콘을 추가하고 리스너에 등록함
        
        LayerListScrollPane.add(layerList);
        
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 그리드백에 배치함
        addGrid(layerListLabel, 0, 0);
        addGrid(sidebarToolBarPanel, 1, 0);
        addGrid(LayerListScrollPane, 2, 1);
        
        layerList.addListSelectionListener(new LayerListSelectionListener());
    }
    
    /*
    ** 윈도우 생성 관련 메소드
    */
    private void createToolBar() { // 툴바에 아이콘을 추가하고 리스너에 등록함
        
        sidebarToolBarPanel = new JPanel();
        
        toggleSelectedLayerVisibleButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "invisible.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));   
        toggleSelectedLayerVisibleButton.setToolTipText("선택한 레이어를 가리거나 다시 보입니다.");
        toggleSelectedLayerVisibleButton.setActionCommand("toggleSelectedLayerVisible");
        toggleSelectedLayerVisibleButton.setPreferredSize(Constant.defaultToolBarButtonSize); 
        sidebarToolBarPanel.add(toggleSelectedLayerVisibleButton);
        toggleSelectedLayerVisibleButton.addActionListener((ActionListener) new ButtonClickedActionListener());
        
        moveSelectedLayerFrontButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "up_arrow.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));   
        moveSelectedLayerFrontButton.setToolTipText("선택한 레이어를 한 칸 위로 올립니다.");
        moveSelectedLayerFrontButton.setActionCommand("moveSelectedLayerFront");
        moveSelectedLayerFrontButton.setPreferredSize(Constant.defaultToolBarButtonSize); 
        sidebarToolBarPanel.add(moveSelectedLayerFrontButton);
        moveSelectedLayerFrontButton.addActionListener((ActionListener) new ButtonClickedActionListener());
        
        moveSelectedLayerBackButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "down_arrow.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        moveSelectedLayerBackButton.setToolTipText("선택한 레이어를 한 칸 아래로 내립니다.");   
        moveSelectedLayerBackButton.setActionCommand("moveSelectedLayerBack");
        moveSelectedLayerBackButton.setPreferredSize(Constant.defaultToolBarButtonSize);
        sidebarToolBarPanel.add(moveSelectedLayerBackButton);
        moveSelectedLayerBackButton.addActionListener(new ButtonClickedActionListener());
        
        renameSelectedLayerButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "rename.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        renameSelectedLayerButton.setToolTipText("선택한 레이어의 이름을 변경합니다."); 
        renameSelectedLayerButton.setActionCommand("renameSelectedLayer");
        renameSelectedLayerButton.setPreferredSize(Constant.defaultToolBarButtonSize);
        sidebarToolBarPanel.add(renameSelectedLayerButton);
        renameSelectedLayerButton.addActionListener(new ButtonClickedActionListener());
        
        copySelectedLayerButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "copy.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        copySelectedLayerButton.setToolTipText("선택한 레이어를 복제합니다.");
        copySelectedLayerButton.setActionCommand("copySelectedLayer");
        copySelectedLayerButton.setPreferredSize(Constant.defaultToolBarButtonSize);
        sidebarToolBarPanel.add(copySelectedLayerButton);
        copySelectedLayerButton.addActionListener(new ButtonClickedActionListener());
        
        deleteSelectedLayerButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "delete.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        deleteSelectedLayerButton.setToolTipText("선택한 레이어를 삭제합니다.");
        deleteSelectedLayerButton.setActionCommand("deleteSelectedLayer");
        deleteSelectedLayerButton.setPreferredSize(Constant.defaultToolBarButtonSize);
        sidebarToolBarPanel.add(deleteSelectedLayerButton);
        deleteSelectedLayerButton.addActionListener(new ButtonClickedActionListener());
        
        deleteAllLayerButton = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + "clear.png").getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));
        deleteAllLayerButton.setToolTipText("모든 레이어를 삭제합니다.");
        deleteAllLayerButton.setActionCommand("deleteAllLayer");
        deleteAllLayerButton.setPreferredSize(Constant.defaultToolBarButtonSize);
        sidebarToolBarPanel.add(deleteAllLayerButton);
        deleteAllLayerButton.addActionListener(new ButtonClickedActionListener());
    }
    
    /*
    ** LayerList 새로고침 메소드
    */
    public void refreshLayerList() {
        layerList.setListData(canvasContainer.getShapeLayerArrayListToVector());
        layerListLabel.setText("총 " + canvasContainer.getShapeLayerArrayListToVector().size() + "개의 레이어");
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class LayerListSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent event){ if (layerList.getSelectedIndex() != -1) controller.SidebarValueChangedEventHandler(event, layerList.getSelectedIndex()); }
    }
    class ButtonClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {  controller.SidebarActionPerformedEventHandler(event); }
    }
    
    /*
    ** 그리드백 레이아웃 관련 메소드
    */
    private void addGrid(Component c, int gridY, int weightY) { // 그리드백 레이아웃에 컴포넌트를 추가함
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        gbc.weighty = weightY;
        Gbag.setConstraints(c, gbc);
        add(c);
    }
    
    /*
    ** 옵저버 관련 메소드
    */
    @Override public void updateCanvasContainer() { this.repaint(); }
    @Override public void updateVariable() { this.repaint(); }
    
}
