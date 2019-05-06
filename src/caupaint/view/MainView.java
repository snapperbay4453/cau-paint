
package caupaint.view;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainView implements CanvasContainerObserver, VariableObserver{

    private CanvasContainer canvasContainer;
    private Variable variable;
    private CanvasView canvasView;
    private SidebarView sidebarView;
    private Controller controller;

    private JFrame frame;
    private JPanel canvasViewInnerContainerPanel;
    private JScrollPane canvasViewContainerScrollPane;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem createNewCanvasMenuItem;
    private JMenuItem loadFromFileMenuItem;
    private JMenuItem saveToFileMenuItem;
    private JMenuItem saveAsToFileMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu modifyMenu;
    private JMenuItem canvasSizeSettingMenuItem;
    private JMenuItem canvasBackgroundColorSettingMenuItem;

    private JToolBar toolBar;
    private JButton createNewCanvasButton;
    private JButton loadFromFileButton;
    private JButton saveToFileButton;
    private JButton drawLineButton;
    private JButton drawRectangleButton;
    private JButton drawEllipseButton;
    private JButton drawTriangleButton;
    private JButton drawRhombusButton;
    private ArrayList<JButton> shapeButtonsArrayList; // 도형 관련 버튼들을 모은 ArrayList
    private JButton idleButton;
    private JButton moveShapeButton;
    private JButton resizeShapeButton;
    private JButton rotateShapeButton;
    private ArrayList<JButton> functionButtonsArrayList; // 기능 관련 버튼들을 모두 ArrayList
    private JButton chooseColorButton;
    private JButton emptyBackgroundTypeButton;
    private JButton fillBackgroundTypeButton;
    private ArrayList<JButton> backgroundTypeButtonsArrayList; // 배경 타입 관련 버튼들을 모두 ArrayList
    private JComboBox strokeTypeComboBox;
    private JSpinner strokeThicknessSpinner;

    /*
    ** 생성자
    */
    public MainView(CanvasContainer canvasContainer, Variable variable, SidebarView sidebarView, Controller controller) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.sidebarView = sidebarView;
        this.controller = controller;
        
        canvasContainer.registerCanvasContainerObserver(this); // CanvasContainerObserver를 구현하는 클래스에 옵저버로 등록
        variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록        
    }
    
    /*
    ** 윈도우 생성 관련 메소드
    */
    public void createView() {
        // 프레임 및 기본 구성요소 생성
        frame = new JFrame("View");
        canvasView = new CanvasView(canvasContainer, controller); // 도형이 그려지는 Panel
        canvasViewInnerContainerPanel = new JPanel();
        canvasViewContainerScrollPane = new JScrollPane(canvasView); // canvas가 스크롤이 가능하도록 함
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        createMenuBar(); //메뉴바를 생성하고 리스너에 등록함
        createToolBar(); //툴바를 생성하고 리스너에 등록함
        
        // 종료 기능을 리스너에 등록함
        frame.addWindowListener(new WindowActionListener());

        // 레이아웃 지정
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.getContentPane().add(sidebarView, BorderLayout.EAST);
        canvasViewInnerContainerPanel.add(canvasView);
        canvasViewContainerScrollPane.setViewportView(canvasViewInnerContainerPanel);
        frame.getContentPane().add(canvasViewContainerScrollPane, BorderLayout.CENTER);
        
        // 프레임 설정
        frame.setTitle(controller.getMainViewWindowTitle());
        frame.setSize(Constant.defaultWindowSize);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
    private void createMenuBar() { //메뉴바를 생성하고 리스너에 등록함
        menuBar = new JMenuBar();
        
        //메뉴바에 메뉴를 추가함
        fileMenu = new JMenu("파일");
        menuBar.add(fileMenu);
        modifyMenu = new JMenu("편집");
        menuBar.add(modifyMenu);
        
        //메뉴바에 아이템을 추가함, 아이템을 리스너에 등록함
        createNewCanvasMenuItem = new JMenuItem("새 캔버스 만들기");
        createNewCanvasMenuItem.setActionCommand("createNewCanvas");
        fileMenu.add(createNewCanvasMenuItem);
        createNewCanvasMenuItem.addActionListener(new MenuBarClickedActionListener());
        loadFromFileMenuItem = new JMenuItem("불러오기");
        loadFromFileMenuItem.setActionCommand("loadFromFile");
        fileMenu.add(loadFromFileMenuItem);
        loadFromFileMenuItem.addActionListener(new MenuBarClickedActionListener());
        saveToFileMenuItem = new JMenuItem("저장");
        saveToFileMenuItem.setActionCommand("saveToFile");
        fileMenu.add(saveToFileMenuItem);
        saveToFileMenuItem.addActionListener(new MenuBarClickedActionListener());
        saveAsToFileMenuItem = new JMenuItem("다른 이름으로 저장");
        saveAsToFileMenuItem.setActionCommand("saveAsToFile");
        fileMenu.add(saveAsToFileMenuItem);
        saveAsToFileMenuItem.addActionListener(new MenuBarClickedActionListener());
                fileMenu.addSeparator();
        exitMenuItem = new JMenuItem("종료");
        exitMenuItem.setActionCommand("exit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new MenuBarClickedActionListener());
        canvasSizeSettingMenuItem = new JMenuItem("캔버스 크기 설정");
        canvasSizeSettingMenuItem.setActionCommand("canvasSizeSetting");
        modifyMenu.add(canvasSizeSettingMenuItem);
        canvasSizeSettingMenuItem.addActionListener(new MenuBarClickedActionListener());
                modifyMenu.addSeparator();
        canvasBackgroundColorSettingMenuItem = new JMenuItem("캔버스 배경색 설정");
        canvasBackgroundColorSettingMenuItem.setActionCommand("canvasBackgroundColorSetting");
        modifyMenu.add(canvasBackgroundColorSettingMenuItem);
        canvasBackgroundColorSettingMenuItem.addActionListener(new MenuBarClickedActionListener());
    }
    private void createToolBar() { // 툴바를 생성하고 리스너에 등록함
        toolBar = new JToolBar();
        shapeButtonsArrayList = new ArrayList<JButton>(); // 도형 관련 버튼들을 모은 ArrayList
        functionButtonsArrayList = new ArrayList<JButton>(); // 기능 관련 버튼들을 모두 ArrayList
        backgroundTypeButtonsArrayList = new ArrayList<JButton>(); // 배경 타입 관련 버튼들을 모두 ArrayList
        
        // 버튼 생성, 툴바에 추가 및 리스너에 등록
        createNewCanvasButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "new.png"));   
        createNewCanvasButton.setToolTipText("새 캔버스를 만듭니다.");
        createNewCanvasButton.setActionCommand("createNewCanvas");
        toolBar.add(createNewCanvasButton);
        createNewCanvasButton.addActionListener(new ButtonClickedActionListener());
        
        loadFromFileButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "load.png"));
        loadFromFileButton.setToolTipText("저장된 파일로부터 캔버스를 불러옵니다.");
        loadFromFileButton.setActionCommand("loadFromFile");
        toolBar.add(loadFromFileButton);
        loadFromFileButton.addActionListener(new ButtonClickedActionListener());
        
        saveToFileButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "save.png"));
        saveToFileButton.setToolTipText("캔버스를 파일로 저장합니다.");
        saveToFileButton.setActionCommand("saveToFile");
        toolBar.add(saveToFileButton);
        saveToFileButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();    
                
        drawLineButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "line.png"));
        drawLineButton.setToolTipText("마우스로 드래그하여 직선을 그립니다.");
        drawLineButton.setActionCommand("drawLine");
        toolBar.add(drawLineButton);
        shapeButtonsArrayList.add(drawLineButton);
        drawLineButton.addActionListener(new ButtonClickedActionListener());
        
        drawRectangleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png"));
        drawRectangleButton.setToolTipText("마우스로 드래그하여 직사각형을 그립니다.");
        drawRectangleButton.setActionCommand("drawRectangle");
        toolBar.add(drawRectangleButton);
        shapeButtonsArrayList.add(drawRectangleButton);
        drawRectangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawEllipseButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));
        drawEllipseButton.setToolTipText("마우스로 드래그하여 타원을 그립니다.");
        drawEllipseButton.setActionCommand("drawEllipse");
        toolBar.add(drawEllipseButton);
        shapeButtonsArrayList.add(drawEllipseButton);
        drawEllipseButton.addActionListener(new ButtonClickedActionListener());
        
        drawTriangleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));
        drawTriangleButton.setToolTipText("마우스로 드래그하여 삼각형을 그립니다.");
        drawTriangleButton.setActionCommand("drawTriangle");
        toolBar.add(drawTriangleButton);
        shapeButtonsArrayList.add(drawTriangleButton);
        drawTriangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawRhombusButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));
        drawRhombusButton.setToolTipText("마우스로 드래그하여 마름모를 그립니다.");
        drawRhombusButton.setActionCommand("drawRhombus");
        toolBar.add(drawRhombusButton); 
        shapeButtonsArrayList.add(drawRhombusButton);
        drawRhombusButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
        
        idleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "cursor.png"));   
        idleButton.setToolTipText("어떠한 입력에도 반응하지 않고 대기합니다.");
        idleButton.setActionCommand("idle");
        toolBar.add(idleButton);
        functionButtonsArrayList.add(idleButton);
        idleButton.addActionListener(new ButtonClickedActionListener());     
                
        moveShapeButton =  new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "move.png"));
        moveShapeButton.setToolTipText("선택한 도형을 이동합니다.");
        moveShapeButton.setActionCommand("moveShape");
        toolBar.add(moveShapeButton);
        functionButtonsArrayList.add(moveShapeButton);
        moveShapeButton.addActionListener(new ButtonClickedActionListener());
        
        resizeShapeButton =  new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "resize.png"));
        resizeShapeButton.setToolTipText("선택한 도형의 크기를 변경합니다.");
        resizeShapeButton.setActionCommand("resizeShape");
        toolBar.add(resizeShapeButton);
        functionButtonsArrayList.add(resizeShapeButton);
        resizeShapeButton.addActionListener(new ButtonClickedActionListener());
        
        rotateShapeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rotate.png"));
        rotateShapeButton.setToolTipText("선택한 도형을 회전시킵니다.");
        rotateShapeButton.setActionCommand("rotateShape");
        toolBar.add(rotateShapeButton);
        functionButtonsArrayList.add(rotateShapeButton);
        rotateShapeButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        toolBar.add(new JLabel("색상 "));
        chooseColorButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "bgcolor.png"));
        chooseColorButton.setToolTipText("색상을 설정합니다.");
        chooseColorButton.setActionCommand("chooseColor");
        chooseColorButton.setBackground(variable.getColor());
        toolBar.add(chooseColorButton);
        chooseColorButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        emptyBackgroundTypeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_empty.png"));
        emptyBackgroundTypeButton.setToolTipText("도형의 배경이 비어있도록 설정합니다.");
        emptyBackgroundTypeButton.setActionCommand("emptyBackgroundType");
        toolBar.add(emptyBackgroundTypeButton);
        backgroundTypeButtonsArrayList.add(emptyBackgroundTypeButton);
        emptyBackgroundTypeButton.addActionListener(new ButtonClickedActionListener());
        
        fillBackgroundTypeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_fill.png"));
        fillBackgroundTypeButton.setToolTipText("도형의 배경이 선택한 색상으로 채워지도록 설정합니다.");
        fillBackgroundTypeButton.setActionCommand("fillBackgroundType");
        toolBar.add(fillBackgroundTypeButton);
        backgroundTypeButtonsArrayList.add(fillBackgroundTypeButton);
        fillBackgroundTypeButton.addActionListener(new ButtonClickedActionListener()); 

        setBackgroundOnlySelectedButton(); // 초기 선택값을 버튼에 적용함
        
        // 선 속성 선택 기능
        toolBar.add(new JLabel("  선 속성 "));
        strokeTypeComboBox = new JComboBox();
        strokeTypeComboBox.setSize(30, 10);
        strokeTypeComboBox.setMaximumSize(new Dimension(160, 42));
        strokeTypeComboBox.setPreferredSize(new Dimension(160, 42));
        strokeTypeComboBox.addItem("실선");
        strokeTypeComboBox.addItem("점선");
        strokeTypeComboBox.addItem("파선");
        strokeTypeComboBox.addItem("긴파선");
        strokeTypeComboBox.addItem("1점 쇄선");
        strokeTypeComboBox.addItem("2점 쇄선");
        strokeTypeComboBox.setActionCommand("strokeType");
        toolBar.add(strokeTypeComboBox);
        strokeTypeComboBox.addItemListener(new StrokeTypeComboBoxItemChangeActionListener());
        
        strokeThicknessSpinner = new JSpinner();
        strokeThicknessSpinner.setModel(new SpinnerNumberModel(5, 1, 100, 1));
        strokeThicknessSpinner.setEditor(new JSpinner.NumberEditor(strokeThicknessSpinner, "0"));
        strokeThicknessSpinner.setMaximumSize(new Dimension(50, 42));
        strokeThicknessSpinner.setPreferredSize(new Dimension(50, 42));
        toolBar.add(strokeThicknessSpinner);
        strokeThicknessSpinner.addChangeListener(new SpinnerChangeActionListener());
    }
    
    /*
    ** 리스너 관련 메소드
    */
   class MenuBarClickedActionListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent event) { controller.MainViewMenuBarClickedEventHandler(event); }
    }
    class ButtonClickedActionListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent event) { controller.MainViewButtonClickedEventHandler(event); }
    }
    class StrokeTypeComboBoxItemChangeActionListener implements ItemListener{
        @Override public void itemStateChanged(ItemEvent event) { controller.MainViewStrokeTypeComboBoxItemStateChangedEventHandler(event); }       
    }
    class SpinnerChangeActionListener implements ChangeListener{
        @Override public void stateChanged(ChangeEvent event) { controller.MainViewSpinnerStateChangedEventHandler(event, (int)strokeThicknessSpinner.getValue()); }       
    }
    class WindowActionListener extends WindowAdapter {
        @Override public void windowClosing(WindowEvent event) { controller.MainViewWindowClosingEventHandler(event); }
    }
    
    /*
    ** 버튼 배경색 지정 메소드
    */
    public void changeBackgroundOnlySelectedButton(ArrayList<JButton> buttonsArrayList, JButton selectedButton){
        for (JButton button : buttonsArrayList) {
           if(selectedButton != null && button.equals(selectedButton)) button.setBackground(Constant.defaultChoosedButtonColor);
           else button.setBackground(Constant.defaultUnchoosedButtonColor);
        }
    }

    public void setBackgroundOnlySelectedButton(){
        switch(variable.getFunctionType()) {
            case IDLE:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, idleButton);   break;
            case DRAW:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, null);   break;
            case MOVE:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, moveShapeButton);   break;
            case RESIZE:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, resizeShapeButton);   break;
            case ROTATE:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, rotateShapeButton);   break;
        }
        if (variable.getFunctionType() == FunctionType.DRAW) switch(variable.getShapeType()) {
            case LINE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawLineButton);   break;
            case RECTANGLE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawRectangleButton);   break;
            case ELLIPSE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawEllipseButton);   break;
            case TRIANGLE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawTriangleButton);   break;
            case RHOMBUS:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawRhombusButton);   break;
        } else changeBackgroundOnlySelectedButton(shapeButtonsArrayList, null); // 그리기 모드가 아닐 경우
        switch(variable.getBackgroundType()) {
            case EMPTY:
                changeBackgroundOnlySelectedButton(backgroundTypeButtonsArrayList, emptyBackgroundTypeButton);   break;
            case FILL:
                changeBackgroundOnlySelectedButton(backgroundTypeButtonsArrayList, fillBackgroundTypeButton);   break;
        }
    }
    
    /*
    ** 옵저버 관련 메소드
    */
    @Override
    public void updateCanvasContainer() {
        canvasViewInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
        sidebarView.refreshLayerList();
        frame.repaint();
    }
    @Override
    public void updateVariable() {
        setBackgroundOnlySelectedButton();
        chooseColorButton.setBackground(variable.getColor()); // chooseColorButton의 배경색 새로고침
        frame.setTitle(controller.getMainViewWindowTitle());
        frame.repaint();
    }
    
}
