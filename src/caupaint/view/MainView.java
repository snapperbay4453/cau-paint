
package caupaint.view;
import caupaint.model.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MainView implements Runnable, CanvasContainerObserver, VariableObserver{

    private CanvasContainer canvasContainer;
    private Variable variable;
    private CanvasView canvasView;
    private SidebarView sidebarView;

    private JFrame frame;
    private JPanel canvasViewInnerContainerPanel;
    private JScrollPane canvasViewContainerScrollPane;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu canvasMenu;
    private JMenu layerMenu;
    private ArrayList<JMenuItem> menuItemArrayList;
    
    private JToolBar toolBar;
    private ArrayList<AbstractButton> buttonArrayList;
    private ButtonGroup functionAndShapeTypeButtonGroup;
    private ButtonGroup backgroundTypeButtonGroup;
    
    private JComboBox strokeTypeComboBox;
    private JSpinner strokeWidthSpinner;
    private JComboBox fontNameComboBox;
    private JSpinner fontSizeSpinner;

    /*
    ** 생성자
    */
    public MainView(CanvasContainer canvasContainer, Variable variable, CanvasView canvasView, SidebarView sidebarView) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.canvasView = canvasView;
        this.sidebarView = sidebarView;
        
        menuItemArrayList = new ArrayList<JMenuItem>();
        buttonArrayList = new ArrayList<AbstractButton>();
        
        createView();

        //canvasContainer.registerCanvasContainerObserver(this); // CanvasContainerObserver를 구현하는 클래스에 옵저버로 등록
        //variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록        
    }

    /*
    ** 스레드 관련 메소드
    */    
    public void run() {
        
        while(true) {
            canvasViewInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
            sidebarView.refreshLayerList();
            frame.setTitle(canvasContainer.generateMainViewWindowTitle());
            
            canvasView.setPreferredSize(new Dimension((int)canvasContainer.getCanvasSize().getX(), (int)canvasContainer.getCanvasSize().getY()));
            canvasView.setBackground(canvasContainer.getCanvasBackgroundColor());
            canvasView.repaint();
            sidebarView.refreshLayerList();
            sidebarView.repaint();
            
            frame.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    
    /*
    ** 윈도우 생성 관련 메소드
    */
    public void createView() {
        // 프레임 및 기본 구성요소 생성
        frame = new JFrame("View");
        canvasViewInnerContainerPanel = new JPanel();
        canvasViewContainerScrollPane = new JScrollPane(canvasView); // canvas가 스크롤이 가능하도록 함
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        createMenuBar(); //메뉴바를 생성하고 리스너에 등록함
        createToolBar(); //툴바를 생성하고 리스너에 등록함

        // 레이아웃 지정
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.getContentPane().add(sidebarView, BorderLayout.EAST);
        canvasViewInnerContainerPanel.add(canvasView);
        canvasViewContainerScrollPane.setViewportView(canvasViewInnerContainerPanel);
        frame.getContentPane().add(canvasViewContainerScrollPane, BorderLayout.CENTER);
        
        // 프레임 설정
        frame.setTitle(canvasContainer.generateMainViewWindowTitle());
        frame.setSize(Constant.defaultWindowSize);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
    
    private void createMenuBar() { //메뉴바를 생성하고 리스너에 등록함
        menuBar = new JMenuBar();
        
        //메뉴바에 메뉴를 추가함
        fileMenu = new JMenu("파일");
        menuBar.add(fileMenu);
        canvasMenu = new JMenu("캔버스");
        menuBar.add(canvasMenu);
        layerMenu = new JMenu("레이어");
        menuBar.add(layerMenu);
        
        //메뉴바에 아이템을 추가함, 아이템을 리스너에 등록함
        addMenuItemToMenu("새 캔버스 만들기", "createNewCanvas", fileMenu);
        addMenuItemToMenu("불러오기", "loadFromFile", fileMenu);
        addMenuItemToMenu("저장", "saveToFile", fileMenu);
        addMenuItemToMenu("다른 이름으로 저장", "saveAsToFile", fileMenu);
        addMenuItemToMenu("종료", "checkExit", fileMenu);
        addMenuItemToMenu("캔버스 크기 설정", "setCanvasSize", canvasMenu);
        addMenuItemToMenu("캔버스 배경색 설정", "setCanvasBackgroundColor", canvasMenu);
        addMenuItemToMenu("레이어 가로 대칭", "flipLayerHorizontally", layerMenu);
        addMenuItemToMenu("레이어 세로 대칭", "flipLayerVertically", layerMenu);
    }
    private void createToolBar() { // 툴바를 생성하고 리스너에 등록함
        toolBar = new JToolBar();
        functionAndShapeTypeButtonGroup = new ButtonGroup();
        backgroundTypeButtonGroup = new ButtonGroup();
        
        // 버튼 생성, 툴바에 추가 및 리스너에 등록
        addButtonToToolBar("new.png", "새 캔버스를 만듭니다.", "createNewCanvas");
        addButtonToToolBar("load.png", "저장된 파일로부터 캔버스를 불러옵니다.", "loadFromFile");
        addButtonToToolBar("save.png", "캔버스를 파일로 저장합니다.", "saveToFile");
                toolBar.addSeparator();    
        addToggleButtonToToolBarAndButtonGroup("polyline.png", "마우스로 클릭하여 폴리선을 그립니다. 같은 곳을 두 번 클릭하여 폴리선을 완성합니다.", "drawPolyline", functionAndShapeTypeButtonGroup);
        addToggleButtonToToolBarAndButtonGroup("pen.png", "마우스로 드래그하여 자유곡선을 그립니다.", "drawPen", functionAndShapeTypeButtonGroup);
        addToggleButtonToToolBarAndButtonGroup("rectangle.png", "마우스로 드래그하여 직사각형을 그립니다.", "drawRectangle", functionAndShapeTypeButtonGroup);
        addToggleButtonToToolBarAndButtonGroup("ellipse.png", "마우스로 드래그하여 타원을 그립니다.", "drawEllipse", functionAndShapeTypeButtonGroup);
        addToggleButtonToToolBarAndButtonGroup("triangle.png", "마우스로 드래그하여 삼각형을 그립니다.", "drawTriangle", functionAndShapeTypeButtonGroup);
        addToggleButtonToToolBarAndButtonGroup("rhombus.png", "마우스로 드래그하여 마름모를 그립니다.", "drawRhombus", functionAndShapeTypeButtonGroup);
                toolBar.addSeparator();
        addButtonToToolBar("text.png", "텍스트를 삽입합니다.", "insertText");
        addButtonToToolBar("image.png", "이미지를 삽입합니다.", "insertImage");      
                toolBar.addSeparator();
        addToggleButtonToToolBarAndButtonGroup("select.png", "마우스로 클릭하여 도형을 선택합니다.", "selectShape", functionAndShapeTypeButtonGroup);      
        addToggleButtonToToolBarAndButtonGroup("move.png", "마우스로 드래그하여 선택한 도형을 이동시킵니다.", "moveShape", functionAndShapeTypeButtonGroup);      
        addToggleButtonToToolBarAndButtonGroup("resize.png", "마우스로 드래그하여 선택한 도형의 크기를 변경합니다.", "resizeShape", functionAndShapeTypeButtonGroup);      
        addToggleButtonToToolBarAndButtonGroup("rotate.png", "마우스로 드래그하여 선택한 도형을 회전시킵니다.", "rotateShape", functionAndShapeTypeButtonGroup);      
                toolBar.addSeparator();
        toolBar.add(new JLabel("색상 "));
        addButtonToToolBar("bgcolor.png", "외곽선 색상을 설정합니다.", "chooseBorderColor");      
        addButtonToToolBar("bgcolor.png", "배경 색상을 설정합니다.", "chooseBackgroundColor");      
                toolBar.addSeparator();
        addToggleButtonToToolBarAndButtonGroup("background_empty.png", "도형의 배경이 비어있도록 설정합니다.", "emptyBackgroundType", backgroundTypeButtonGroup);      
        addToggleButtonToToolBarAndButtonGroup("background_fill.png", "도형의 배경이 선택한 색상으로 채워지도록 설정합니다.", "fillBackgroundType", backgroundTypeButtonGroup);      
        
        //setBackgroundOnlySelectedButton(); // 초기 선택값을 버튼에 적용함
        
        // 선 속성 선택 기능
        toolBar.add(new JLabel("  선 속성 "));
        strokeTypeComboBox = new JComboBox();
        strokeTypeComboBox.setSize(30, 10);
        strokeTypeComboBox.setMaximumSize(new Dimension(100, 42));
        strokeTypeComboBox.setPreferredSize(new Dimension(100, 42));
        strokeTypeComboBox.addItem("실선");
        strokeTypeComboBox.addItem("점선");
        strokeTypeComboBox.addItem("파선");
        strokeTypeComboBox.addItem("긴파선");
        strokeTypeComboBox.addItem("1점 쇄선");
        strokeTypeComboBox.addItem("2점 쇄선");
        strokeTypeComboBox.setActionCommand("strokeType");
        toolBar.add(strokeTypeComboBox);
        //strokeTypeComboBox.addItemListener(new StrokeTypeComboBoxItemChangeActionListener());
        
        strokeWidthSpinner = new JSpinner();
        strokeWidthSpinner.setModel(new SpinnerNumberModel(5, 1, 100, 1));
        strokeWidthSpinner.setEditor(new JSpinner.NumberEditor(strokeWidthSpinner, "0"));
        strokeWidthSpinner.setMaximumSize(new Dimension(50, 42));
        strokeWidthSpinner.setPreferredSize(new Dimension(50, 42));
        toolBar.add(strokeWidthSpinner);
        //strokeWidthSpinner.addChangeListener(new StrokeWidthSpinnerStateChangeActionListener());
        
        // 글꼴 선택 기능
        toolBar.add(new JLabel("  글꼴 속성 "));
        fontNameComboBox = new JComboBox();
        fontNameComboBox.setSize(30, 10);
        fontNameComboBox.setMaximumSize(new Dimension(100, 42));
        fontNameComboBox.setPreferredSize(new Dimension(100, 42));
        fontNameComboBox.addItem("굴림");
        fontNameComboBox.addItem("궁서");
        fontNameComboBox.addItem("돋움");
        fontNameComboBox.addItem("맑은 고딕");
        fontNameComboBox.addItem("바탕");
        fontNameComboBox.setActionCommand("fontName");
        toolBar.add(fontNameComboBox);
        //fontNameComboBox.addItemListener(new FontNameComboBoxItemChangeActionListener());
        
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setModel(new SpinnerNumberModel(30, 1, 500, 1));
        fontSizeSpinner.setEditor(new JSpinner.NumberEditor(fontSizeSpinner, "0"));
        fontSizeSpinner.setMaximumSize(new Dimension(50, 42));
        fontSizeSpinner.setPreferredSize(new Dimension(50, 42));
        toolBar.add(fontSizeSpinner);
        //fontSizeSpinner.addChangeListener(new FontSizeSpinnerStateChangeActionListener());
        
    }
    
    
    private void addMenuItemToMenu (String text, String actionCommand, JMenu menu) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setActionCommand(actionCommand);
        menu.add(menuItem);
        menuItemArrayList.add(menuItem);
        // actionListener로의 등록은 controller에서 함
    }
    private void addButtonToToolBar (String iconPath, String toolTipText, String actionCommand) {
        JButton button = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + iconPath));
        button.setToolTipText(toolTipText);
        button.setActionCommand(actionCommand);
        toolBar.add(button);
        buttonArrayList.add(button);
        // actionListener로의 등록은 controller에서 함
    }
    private void addToggleButtonToToolBarAndButtonGroup (String iconPath, String toolTipText, String actionCommand, ButtonGroup buttonGroup) {
        JToggleButton toggleButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + iconPath));
        toggleButton.setToolTipText(toolTipText);
        toggleButton.setActionCommand(actionCommand);
        toolBar.add(toggleButton);
        if (buttonGroup != null) buttonGroup.add(toggleButton);
        buttonArrayList.add(toggleButton);
        // actionListener로의 등록은 controller에서 함
    }
    
    /*
    ** getter
    */
    public JFrame getFrame() { return frame;  }
    public ArrayList<JMenuItem> getMenuItemArrayList() { return menuItemArrayList; }
    public ArrayList<AbstractButton> getButtonArrayList() { return buttonArrayList; }
    public JComboBox getStrokeTypeComboBox() { return strokeTypeComboBox; }
    public JSpinner getStrokeWidthSpinner() { return strokeWidthSpinner; }
    public JComboBox getFontNameComboBox() { return fontNameComboBox; }
    public JSpinner getFontSizeSpinner() { return fontSizeSpinner; }
    
    /*
    ** 옵저버 관련 메소드 - 사용하지 않음
    */
    @Override
    public void updateCanvasContainer() {
        /*
        canvasViewInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
        sidebarView.refreshLayerList();
        frame.setTitle(generateMainViewWindowTitle());
        frame.repaint();
*/
    }
    @Override
    public void updateVariable() {
        /*
        setBackgroundOnlySelectedButton();
        chooseBorderColorButton.setBackground(variable.getBorderColor()); // chooseBorderColorButton의 배경색 새로고침
        chooseBackgroundColorButton.setBackground(variable.getBackgroundColor()); // chooseBackgroundColorButton의 배경색 새로고침
        frame.repaint();
*/
    }
    
}
