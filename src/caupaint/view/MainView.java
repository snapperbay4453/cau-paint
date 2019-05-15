
package caupaint.view;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainView implements Runnable, CanvasContainerObserver, VariableObserver{

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
    private JMenu canvasMenu;
    private JMenuItem setCanvasSizeMenuItem;
    private JMenuItem setCanvasBackgroundColorMenuItem;
    private JMenu layerMenu;
    private JMenuItem flipLayerHorizontallyMenuItem;
    private JMenuItem flipLayerVerticallyMenuItem;

    private JToolBar toolBar;
    private ButtonGroup functionAndShapeTypeButtonGroup;
    private JButton createNewCanvasButton;
    private JButton loadFromFileButton;
    private JButton saveToFileButton;
    private JToggleButton drawPolylineButton;
    private JToggleButton drawPenButton;
    private JToggleButton drawRectangleButton;
    private JToggleButton drawEllipseButton;
    private JToggleButton drawTriangleButton;
    private JToggleButton drawRhombusButton;
    private JButton insertTextButton;
    private JButton insertImageButton;
    //private ArrayList<JButton> shapeButtonsArrayList; // 도형 관련 버튼들을 모은 ArrayList
    private JToggleButton selectShapeButton;
    private JToggleButton moveShapeButton;
    private JToggleButton resizeShapeButton;
    private JToggleButton rotateShapeButton;
    //private ArrayList<JButton> functionButtonsArrayList; // 기능 관련 버튼들을 모은 ArrayList
    private JButton chooseBorderColorButton;
    private JButton chooseBackgroundColorButton;
    private ButtonGroup backgroundTypeButtonGroup;
    private JToggleButton emptyBackgroundTypeButton;
    private JToggleButton fillBackgroundTypeButton;
    //private ArrayList<JButton> backgroundTypeButtonsArrayList; // 배경 타입 관련 버튼들을 모은 ArrayList
    private JComboBox strokeTypeComboBox;
    private JSpinner strokeWidthSpinner;
    private JComboBox fontNameComboBox;
    private JSpinner fontSizeSpinner;

    /*
    ** 생성자
    */
    public MainView(CanvasContainer canvasContainer, Variable variable, CanvasView canvasView, SidebarView sidebarView, Controller controller) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.canvasView = canvasView;
        this.sidebarView = sidebarView;
        this.controller = controller;
        
        canvasContainer.registerCanvasContainerObserver(this); // CanvasContainerObserver를 구현하는 클래스에 옵저버로 등록
        variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록        
    }

    /*
    ** 스레드 관련 메소드
    */    
    public void run() {
        
        while(true) {
            canvasViewInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
            sidebarView.refreshLayerList();
            frame.setTitle(canvasContainer.generateMainViewWindowTitle());
            //setBackgroundOnlySelectedButton();
            chooseBorderColorButton.setBackground(variable.getBorderColor()); // chooseBorderColorButton의 배경색 새로고침
            chooseBackgroundColorButton.setBackground(variable.getBackgroundColor()); // chooseBackgroundColorButton의 배경색 새로고침
            frame.repaint();
            try {
                Thread.sleep(100);
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
        
        // 종료 기능을 리스너에 등록함
        frame.addWindowListener(new WindowActionListener());

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
        
        run();
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
        exitMenuItem.setActionCommand("checkExit");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new MenuBarClickedActionListener());
        
        setCanvasSizeMenuItem = new JMenuItem("캔버스 크기 설정");
        setCanvasSizeMenuItem.setActionCommand("setCanvasSize");
        canvasMenu.add(setCanvasSizeMenuItem);
        setCanvasSizeMenuItem.addActionListener(new MenuBarClickedActionListener());
        setCanvasBackgroundColorMenuItem = new JMenuItem("캔버스 배경색 설정");
        setCanvasBackgroundColorMenuItem.setActionCommand("setCanvasBackgroundColor");
        canvasMenu.add(setCanvasBackgroundColorMenuItem);
        setCanvasBackgroundColorMenuItem.addActionListener(new MenuBarClickedActionListener());
        
        flipLayerHorizontallyMenuItem = new JMenuItem("레이어 가로 대칭");
        flipLayerHorizontallyMenuItem.setActionCommand("flipLayerHorizontally");
        layerMenu.add(flipLayerHorizontallyMenuItem);
        flipLayerHorizontallyMenuItem.addActionListener(new MenuBarClickedActionListener());
        flipLayerVerticallyMenuItem = new JMenuItem("레이어 세로 대칭");
        flipLayerVerticallyMenuItem.setActionCommand("flipLayerVertically");
        layerMenu.add(flipLayerVerticallyMenuItem);
        flipLayerVerticallyMenuItem.addActionListener(new MenuBarClickedActionListener());
        
    }
    private void createToolBar() { // 툴바를 생성하고 리스너에 등록함
        toolBar = new JToolBar();
        //shapeButtonsArrayList = new ArrayList<JButton>(); // 도형 관련 버튼들을 모은 ArrayList
        //functionButtonsArrayList = new ArrayList<JButton>(); // 기능 관련 버튼들을 모두 ArrayList
        //backgroundTypeButtonsArrayList = new ArrayList<JButton>(); // 배경 타입 관련 버튼들을 모두 ArrayList
        
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
                
        functionAndShapeTypeButtonGroup = new ButtonGroup();

        drawPolylineButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "polyline.png"));
        drawPolylineButton.setToolTipText("마우스를 클릭하여 폴리선을 그립니다. 같은 곳을 두 번 클릭하여 폴리선을 완성합니다.");
        drawPolylineButton.setActionCommand("drawPolyline");
        functionAndShapeTypeButtonGroup.add(drawPolylineButton);
        toolBar.add(drawPolylineButton);
        //shapeButtonsArrayList.add(drawPolylineButton);
        drawPolylineButton.addActionListener(new ButtonClickedActionListener());
        
        drawPenButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "pen.png"));
        drawPenButton.setToolTipText("마우스를 드래그하여 자유곡선을 그립니다.");
        drawPenButton.setActionCommand("drawPen");
        functionAndShapeTypeButtonGroup.add(drawPenButton);
        toolBar.add(drawPenButton);
        //shapeButtonsArrayList.add(drawPenButton);
        drawPenButton.addActionListener(new ButtonClickedActionListener());
        
        drawRectangleButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png"));
        drawRectangleButton.setToolTipText("마우스로 드래그하여 직사각형을 그립니다.");
        drawRectangleButton.setActionCommand("drawRectangle");
        functionAndShapeTypeButtonGroup.add(drawRectangleButton);
        toolBar.add(drawRectangleButton);
        //shapeButtonsArrayList.add(drawRectangleButton);
        drawRectangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawEllipseButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));
        drawEllipseButton.setToolTipText("마우스로 드래그하여 타원을 그립니다.");
        drawEllipseButton.setActionCommand("drawEllipse");
        functionAndShapeTypeButtonGroup.add(drawEllipseButton);
        toolBar.add(drawEllipseButton);
        //shapeButtonsArrayList.add(drawEllipseButton);
        drawEllipseButton.addActionListener(new ButtonClickedActionListener());
        
        drawTriangleButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));
        drawTriangleButton.setToolTipText("마우스로 드래그하여 삼각형을 그립니다.");
        drawTriangleButton.setActionCommand("drawTriangle");
        functionAndShapeTypeButtonGroup.add(drawTriangleButton);
        toolBar.add(drawTriangleButton);
        //shapeButtonsArrayList.add(drawTriangleButton);
        drawTriangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawRhombusButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));
        drawRhombusButton.setToolTipText("마우스로 드래그하여 마름모를 그립니다.");
        drawRhombusButton.setActionCommand("drawRhombus");
        functionAndShapeTypeButtonGroup.add(drawRhombusButton);
        toolBar.add(drawRhombusButton); 
        //shapeButtonsArrayList.add(drawRhombusButton);
        drawRhombusButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
        
        insertTextButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "text.png"));
        insertTextButton.setToolTipText("텍스트를 삽입합니다.");
        insertTextButton.setActionCommand("insertText");
        toolBar.add(insertTextButton); 
        //shapeButtonsArrayList.add(insertTextButton);
        insertTextButton.addActionListener(new ButtonClickedActionListener());
        
        insertImageButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "image.png"));
        insertImageButton.setToolTipText("이미지를 삽입합니다.");
        insertImageButton.setActionCommand("insertImage");
        toolBar.add(insertImageButton); 
        //shapeButtonsArrayList.add(insertImageButton);
        insertImageButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        selectShapeButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "select.png"), true);   
        selectShapeButton.setToolTipText("마우스를 클릭하여 도형을 선택합니다.");
        selectShapeButton.setActionCommand("selectShape");
        functionAndShapeTypeButtonGroup.add(selectShapeButton);
        toolBar.add(selectShapeButton);
        //functionButtonsArrayList.add(selectShapeButton);
        selectShapeButton.addActionListener(new ButtonClickedActionListener());     
                
        moveShapeButton =  new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "move.png"));
        moveShapeButton.setToolTipText("선택한 도형의 위치 이동합니다.");
        moveShapeButton.setActionCommand("moveShape");
        functionAndShapeTypeButtonGroup.add(moveShapeButton);
        toolBar.add(moveShapeButton);
        //functionButtonsArrayList.add(moveShapeButton);
        moveShapeButton.addActionListener(new ButtonClickedActionListener());
        
        resizeShapeButton =  new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "resize.png"));
        resizeShapeButton.setToolTipText("선택한 도형의 크기를 변경합니다.");
        resizeShapeButton.setActionCommand("resizeShape");
        functionAndShapeTypeButtonGroup.add(resizeShapeButton);
        toolBar.add(resizeShapeButton);
        //functionButtonsArrayList.add(resizeShapeButton);
        resizeShapeButton.addActionListener(new ButtonClickedActionListener());
        
        rotateShapeButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rotate.png"));
        rotateShapeButton.setToolTipText("선택한 도형을 회전시킵니다.");
        rotateShapeButton.setActionCommand("rotateShape");
        functionAndShapeTypeButtonGroup.add(rotateShapeButton);
        toolBar.add(rotateShapeButton);
        //functionButtonsArrayList.add(rotateShapeButton);
        rotateShapeButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        toolBar.add(new JLabel("색상 "));
        chooseBorderColorButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "bgcolor.png"));
        chooseBorderColorButton.setToolTipText("외곽선 색상을 설정합니다.");
        chooseBorderColorButton.setActionCommand("chooseBorderColor");
        chooseBorderColorButton.setBackground(variable.getBorderColor());
        toolBar.add(chooseBorderColorButton);
        chooseBorderColorButton.addActionListener(new ButtonClickedActionListener());

        chooseBackgroundColorButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "bgcolor.png"));
        chooseBackgroundColorButton.setToolTipText("배경 색상을 설정합니다.");
        chooseBackgroundColorButton.setActionCommand("chooseBackgroundColor");
        chooseBackgroundColorButton.setBackground(variable.getBackgroundColor());
        toolBar.add(chooseBackgroundColorButton);
        chooseBackgroundColorButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        backgroundTypeButtonGroup = new ButtonGroup();
                
        emptyBackgroundTypeButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_empty.png"), true);
        emptyBackgroundTypeButton.setToolTipText("도형의 배경이 비어있도록 설정합니다.");
        emptyBackgroundTypeButton.setActionCommand("emptyBackgroundType");
        backgroundTypeButtonGroup.add(emptyBackgroundTypeButton);
        toolBar.add(emptyBackgroundTypeButton);
        //backgroundTypeButtonsArrayList.add(emptyBackgroundTypeButton);
        emptyBackgroundTypeButton.addActionListener(new ButtonClickedActionListener());
        
        fillBackgroundTypeButton = new JToggleButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_fill.png"));
        fillBackgroundTypeButton.setToolTipText("도형의 배경이 선택한 색상으로 채워지도록 설정합니다.");
        fillBackgroundTypeButton.setActionCommand("fillBackgroundType");
        backgroundTypeButtonGroup.add(fillBackgroundTypeButton);
        toolBar.add(fillBackgroundTypeButton);
        //backgroundTypeButtonsArrayList.add(fillBackgroundTypeButton);
        fillBackgroundTypeButton.addActionListener(new ButtonClickedActionListener()); 

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
        strokeTypeComboBox.addItemListener(new StrokeTypeComboBoxItemChangeActionListener());
        
        strokeWidthSpinner = new JSpinner();
        strokeWidthSpinner.setModel(new SpinnerNumberModel(5, 1, 100, 1));
        strokeWidthSpinner.setEditor(new JSpinner.NumberEditor(strokeWidthSpinner, "0"));
        strokeWidthSpinner.setMaximumSize(new Dimension(50, 42));
        strokeWidthSpinner.setPreferredSize(new Dimension(50, 42));
        toolBar.add(strokeWidthSpinner);
        strokeWidthSpinner.addChangeListener(new StrokeWidthSpinnerStateChangeActionListener());
        
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
        fontNameComboBox.addItemListener(new FontNameComboBoxItemChangeActionListener());
        
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setModel(new SpinnerNumberModel(30, 1, 500, 1));
        fontSizeSpinner.setEditor(new JSpinner.NumberEditor(fontSizeSpinner, "0"));
        fontSizeSpinner.setMaximumSize(new Dimension(50, 42));
        fontSizeSpinner.setPreferredSize(new Dimension(50, 42));
        toolBar.add(fontSizeSpinner);
        fontSizeSpinner.addChangeListener(new FontSizeSpinnerStateChangeActionListener());
        
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
    class StrokeWidthSpinnerStateChangeActionListener implements ChangeListener{
        @Override public void stateChanged(ChangeEvent event) { controller.MainViewStrokeWidthSpinnerStateChangedEventHandler(event, (int)strokeWidthSpinner.getValue()); }       
    }
    class FontNameComboBoxItemChangeActionListener implements ItemListener{
        @Override public void itemStateChanged(ItemEvent event) { controller.MainViewFontNameComboBoxItemStateChangedEventHandler(event); }       
    }
    class FontSizeSpinnerStateChangeActionListener implements ChangeListener{
        @Override public void stateChanged(ChangeEvent event) { controller.MainViewFontSizeSpinnerStateChangedEventHandler(event, (int)fontSizeSpinner.getValue()); }       
    }
    class WindowActionListener extends WindowAdapter {
        @Override public void windowClosing(WindowEvent event) { controller.MainViewWindowClosingEventHandler(event); }
    }

    /*
    ** 버튼 배경색 지정 메소드
    */
    
    /*
    public void changeBackgroundOnlySelectedButton(ArrayList<JButton> buttonsArrayList, JButton selectedButton){
        for (JButton button : buttonsArrayList) {
           if(selectedButton != null && button.equals(selectedButton)) button.setBackground(Constant.defaultChoosedButtonColor);
           else button.setBackground(Constant.defaultUnchoosedButtonColor);
        }
    }

    public void setBackgroundOnlySelectedButton(){
        switch(variable.getFunctionType()) {
            case SELECT:
                changeBackgroundOnlySelectedButton(functionButtonsArrayList, selectShapeButton);   break;
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
            case POLYLINE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawPolylineButton);   break;
            case PEN:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawPenButton);   break;
            case RECTANGLE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawRectangleButton);   break;
            case ELLIPSE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawEllipseButton);   break;
            case TRIANGLE:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawTriangleButton);   break;
            case RHOMBUS:
                changeBackgroundOnlySelectedButton(shapeButtonsArrayList, drawRhombusButton);   break;
            case TEXT: break;
            case IMAGE: break;
        } else changeBackgroundOnlySelectedButton(shapeButtonsArrayList, null); // 그리기 모드가 아닐 경우
        switch(variable.getBackgroundType()) {
            case EMPTY:
                changeBackgroundOnlySelectedButton(backgroundTypeButtonsArrayList, emptyBackgroundTypeButton);   break;
            case FILL:
                changeBackgroundOnlySelectedButton(backgroundTypeButtonsArrayList, fillBackgroundTypeButton);   break;
        }
    }
    
    */
    
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
