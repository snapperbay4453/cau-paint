
package caupaint.view;
import caupaint.model.*;
import caupaint.observer.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class SidebarView extends JPanel implements CanvasContainerObserver, VariableObserver{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    
    private GridBagLayout Gbag;

    private JPanel sidebarToolBarPanel;
    
    ArrayList<AbstractButton> buttonArrayList = new ArrayList<AbstractButton>();

    private JLabel layerListLabel;
    private ScrollPane LayerListScrollPane;

    private JList<ShapeLayer> layerList;
    
    /*
    ** 생성자
    */
    public SidebarView(CanvasContainer canvasContainer, Variable variable) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        
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
        
        //layerList.addListSelectionListener(new LayerListSelectionListener());
    }
    
    /*
    ** 윈도우 생성 관련 메소드
    */
    private void createToolBar() { // 툴바에 아이콘을 추가하고 리스너에 등록함
        sidebarToolBarPanel = new JPanel();
        addButtonToSidebarToolBarPanel("invisible.png", "선택한 레이어를 가리거나 다시 보입니다.", "toggleSelectedLayerVisible");   
        addButtonToSidebarToolBarPanel("up_arrow.png", "선택한 레이어를 한 칸 위로 올립니다.", "moveSelectedLayerFront");   
        addButtonToSidebarToolBarPanel("down_arrow.png", "선택한 레이어를 한 칸 아래로 내립니다.", "moveSelectedLayerBack");   
        addButtonToSidebarToolBarPanel("rename.png", "선택한 레이어의 이름을 변경합니다.", "renameSelectedLayer");   
        addButtonToSidebarToolBarPanel("copy.png", "선택한 레이어를 복제합니다.", "copySelectedLayer");   
        addButtonToSidebarToolBarPanel("delete.png", "선택한 레이어를 삭제합니다.", "deleteSelectedLayer");   
        addButtonToSidebarToolBarPanel("clear.png", "모든 레이어를 삭제합니다.", "deleteAllLayer");   
    }
    
    /*
    ** LayerList 새로고침 메소드
    */
    public void refreshLayerList() {
        layerList.setListData(canvasContainer.getShapeLayerArrayListToVector());
        layerListLabel.setText("총 " + canvasContainer.getShapeLayerArrayListToVector().size() + "개의 레이어");
    }
    
    private void addButtonToSidebarToolBarPanel (String iconPath, String toolTipText, String actionCommand) {
        JButton button = new JButton(new ImageIcon(new ImageIcon(Constant.defaultIconDirectoryPath + iconPath).getImage().getScaledInstance((int)Constant.defaultToolBarButtonImageSize.getWidth(), (int)Constant.defaultToolBarButtonImageSize.getHeight(), java.awt.Image.SCALE_SMOOTH)));   
        button.setMinimumSize(Constant.defaultToolBarButtonSize);
        button.setPreferredSize(Constant.defaultToolBarButtonSize); // LayerList의 크기 지정
        button.setToolTipText(toolTipText);
        button.setActionCommand(actionCommand);
        sidebarToolBarPanel.add(button);
        buttonArrayList.add(button);
        // actionListener로의 등록은 controller에서 함
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
    ** getter
    */
    public ArrayList<AbstractButton> getButtonArrayList() { return buttonArrayList; }
    public JList getLayerList() { return layerList;  }
    
    /*
    ** 옵저버 관련 메소드 - 사용하지 않음
    */
    @Override public void updateCanvasContainer() { /*this.repaint();*/ }
    @Override public void updateVariable() { /*this.repaint();*/ }
}
