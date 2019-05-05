
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class View implements LayerContainerObserver, VariableObserver{
    
    private LayerContainer layerContainer;
    private Variable variable;
    private Controller controller;
    
    private JFrame frame;
    private Canvas canvas;
    private JPanel canvasInnerContainerPanel;
    private JScrollPane canvasContainerScrollPane;
    private Sidebar sidebar;
    
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
    
    private JButton createNewCanvasButton;
    private JButton loadFromFileButton;
    private JButton saveToFileButton;
    private JButton drawLineButton;
    private JButton drawRectangleButton;
    private JButton drawEllipseButton;
    private JButton drawTriangleButton;
    private JButton drawRhombusButton;
    private JButton idleButton;
    private JButton moveShapeButton;
    private JButton resizeShapeButton;
    private JButton rotateShapeButton;
    private JButton clearButton;
    private JButton chooseColorButton;
    private JButton emptyBackgroundTypeButton;
    private JButton fillBackgroundTypeButton;
    private ArrayList<JButton> shapeButtonsArrayList; // 도형 관련 버튼들을 모은 ArrayList
    private ArrayList<JButton> functionButtonsArrayList; // 기능 관련 버튼들을 모두 ArrayList
    private ArrayList<JButton> backgroundTypeButtonsArrayList; // 배경 타입 관련 버튼들을 모두 ArrayList
    
    
    private JToolBar toolBar;
    
    /*
    ** 생성자
    */
    public View(LayerContainer layerContainer, Variable variable, Controller controller) {
        this.layerContainer= layerContainer;
        this.variable = variable;
        this.controller = controller;
        
        layerContainer.registerLayerContainerObserver(this); // LayerContainerObserver를 구현하는 클래스에 옵저버로 등록
        variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록        
    }
    
    /*
    ** 윈도우 생성
    */
    public void createView() {
        // 프레임 및 기본 구성요소 생성
        frame = new JFrame("View");
        canvas = new Canvas(layerContainer, controller); // 도형이 그려지는 Panel
        canvasInnerContainerPanel = new JPanel();
        canvasContainerScrollPane = new JScrollPane(canvas); // canvas가 스크롤이 가능하도록 함
        sidebar = new Sidebar(layerContainer, controller);
	// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        //메뉴바 생성
        menuBar = new JMenuBar();
        //메뉴바에 메뉴를 추가함
        fileMenu = new JMenu("파일");
        menuBar.add(fileMenu);
        modifyMenu = new JMenu("편집");
        menuBar.add(modifyMenu);
        //메뉴바에 아이템을 추가함, 아이템을 리스너에 등록함
        createNewCanvasMenuItem = new JMenuItem("새 캔버스");
        fileMenu.add(createNewCanvasMenuItem);
        createNewCanvasMenuItem.addActionListener(new MenuBarClickedActionListener());
        loadFromFileMenuItem = new JMenuItem("불러오기");
        fileMenu.add(loadFromFileMenuItem);
        loadFromFileMenuItem.addActionListener(new MenuBarClickedActionListener());
        saveToFileMenuItem = new JMenuItem("저장");
        fileMenu.add(saveToFileMenuItem);
        saveToFileMenuItem.addActionListener(new MenuBarClickedActionListener());
        saveAsToFileMenuItem = new JMenuItem("다른 이름으로 저장");
        fileMenu.add(saveAsToFileMenuItem);
        saveAsToFileMenuItem.addActionListener(new MenuBarClickedActionListener());
                fileMenu.addSeparator();
        exitMenuItem = new JMenuItem("종료");
        fileMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new MenuBarClickedActionListener());
        canvasSizeSettingMenuItem = new JMenuItem("캔버스 크기 설정");
        modifyMenu.add(canvasSizeSettingMenuItem);
        canvasSizeSettingMenuItem.addActionListener(new MenuBarClickedActionListener());
                modifyMenu.addSeparator();
        canvasBackgroundColorSettingMenuItem = new JMenuItem("캔버스 배경색 설정");
        modifyMenu.add(canvasBackgroundColorSettingMenuItem);
        canvasBackgroundColorSettingMenuItem.addActionListener(new MenuBarClickedActionListener());


        // 툴바 생성
        toolBar = new JToolBar();
        
        shapeButtonsArrayList = new ArrayList<JButton>(); // 도형 관련 버튼들을 모은 ArrayList
        functionButtonsArrayList = new ArrayList<JButton>(); // 기능 관련 버튼들을 모두 ArrayList
        backgroundTypeButtonsArrayList = new ArrayList<JButton>(); // 배경 타입 관련 버튼들을 모두 ArrayList
        
        // 버튼 생성, 툴바에 추가 및 리스너에 등록
        createNewCanvasButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "new.png"));   
        createNewCanvasButton.setToolTipText("새 캔버스를 엽니다.");
        toolBar.add(createNewCanvasButton);
        createNewCanvasButton.addActionListener(new ButtonClickedActionListener());
        
        loadFromFileButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "load.png"));
        loadFromFileButton.setToolTipText("저장된 파일로부터 캔버스를 불러옵니다.");
        toolBar.add(loadFromFileButton);
        loadFromFileButton.addActionListener(new ButtonClickedActionListener());
        
        saveToFileButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "save.png"));
        saveToFileButton.setToolTipText("캔버스를 파일로 저장합니다.");     
        toolBar.add(saveToFileButton);
        saveToFileButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();    
                
        drawLineButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "line.png"));
        drawLineButton.setToolTipText("마우스로 드래그하여 직선을 그립니다.");
        toolBar.add(drawLineButton);
        shapeButtonsArrayList.add(drawLineButton);
        drawLineButton.addActionListener(new ButtonClickedActionListener());
        
        drawRectangleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rectangle.png"));
        drawRectangleButton.setToolTipText("마우스로 드래그하여 직사각형을 그립니다.");
        toolBar.add(drawRectangleButton);
        shapeButtonsArrayList.add(drawRectangleButton);
        drawRectangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawEllipseButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "ellipse.png"));
        drawEllipseButton.setToolTipText("마우스로 드래그하여 타원을 그립니다.");
        toolBar.add(drawEllipseButton);
        shapeButtonsArrayList.add(drawEllipseButton);
        drawEllipseButton.addActionListener(new ButtonClickedActionListener());
        
        drawTriangleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "triangle.png"));
        drawTriangleButton.setToolTipText("마우스로 드래그하여 삼각형을 그립니다.");
        toolBar.add(drawTriangleButton);
        shapeButtonsArrayList.add(drawTriangleButton);
        drawTriangleButton.addActionListener(new ButtonClickedActionListener());
        
        drawRhombusButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rhombus.png"));
        drawRhombusButton.setToolTipText("마우스로 드래그하여 마름모를 그립니다.");
        toolBar.add(drawRhombusButton); 
        shapeButtonsArrayList.add(drawRhombusButton);
        drawRhombusButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
        
        idleButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "cursor.png"));   
        idleButton.setToolTipText("어떠한 입력에도 반응하지 않고 대기합니다.");
        toolBar.add(idleButton);
        functionButtonsArrayList.add(idleButton);
        idleButton.addActionListener(new ButtonClickedActionListener());     
                
        moveShapeButton =  new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "move.png"));
        moveShapeButton.setToolTipText("선택한 도형을 이동합니다.");
        toolBar.add(moveShapeButton);
        functionButtonsArrayList.add(moveShapeButton);
        moveShapeButton.addActionListener(new ButtonClickedActionListener());
        
        resizeShapeButton =  new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "resize.png"));
        resizeShapeButton.setToolTipText("선택한 도형의 크기를 변경합니다.");
        toolBar.add(resizeShapeButton);
        functionButtonsArrayList.add(resizeShapeButton);
        resizeShapeButton.addActionListener(new ButtonClickedActionListener());
        
        rotateShapeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "rotate.png"));
        rotateShapeButton.setToolTipText("선택한 도형을 회전시킵니다.");
        toolBar.add(rotateShapeButton);
        functionButtonsArrayList.add(rotateShapeButton);
        rotateShapeButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        clearButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "clear.png"));
        clearButton.setToolTipText("캔버스를 초기화합니다.");
        toolBar.add(clearButton);
        clearButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        toolBar.add(new JLabel("색상 "));
        chooseColorButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "bgcolor.png"));
        chooseColorButton.setToolTipText("색상을 설정합니다.");
        chooseColorButton.setBackground(variable.getColor());
        toolBar.add(chooseColorButton);
        chooseColorButton.addActionListener(new ButtonClickedActionListener());
        
                toolBar.addSeparator();
                
        emptyBackgroundTypeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_empty.png"));
        emptyBackgroundTypeButton.setToolTipText("도형의 배경이 비어있도록 설정합니다.");
        toolBar.add(emptyBackgroundTypeButton);
        backgroundTypeButtonsArrayList.add(emptyBackgroundTypeButton);
        emptyBackgroundTypeButton.addActionListener(new ButtonClickedActionListener());
        
        fillBackgroundTypeButton = new JButton(new ImageIcon(Constant.defaultIconDirectoryPath + "background_fill.png"));
        fillBackgroundTypeButton.setToolTipText("도형의 배경이 선택한 색상으로 채워지도록 설정합니다.");
        toolBar.add(fillBackgroundTypeButton);
        backgroundTypeButtonsArrayList.add(fillBackgroundTypeButton);
        fillBackgroundTypeButton.addActionListener(new ButtonClickedActionListener()); 

        setBackgroundOnlySelectedButton(); // 초기 선택값을 버튼에 적용함
        
        // 종료 기능을 리스너에 등록함
        frame.addWindowListener(new WindowActionListener());
        
        // 레이아웃 지정
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.getContentPane().add(sidebar, BorderLayout.EAST);
        canvasInnerContainerPanel.add(canvas);
        canvasContainerScrollPane.setViewportView(canvasInnerContainerPanel);
        frame.getContentPane().add(canvasContainerScrollPane, BorderLayout.CENTER);
        
        // 프레임 설정
        frame.setTitle(controller.generateViewTitle());
        frame.setSize(Constant.defaultWindowSize);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
    
    /*
    ** 리스너 관련 메소드
    */

    class MenuBarClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == createNewCanvasMenuItem) {
                controller.createNewCanvas();
            }
            if (event.getSource() == loadFromFileMenuItem) {
                try {
                    controller.loadLayersFromFile(controller.getFilePathToOpen());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (event.getSource() == saveToFileMenuItem) {
                try {
                    controller.saveLayersToFile(controller.getFilePathToSave());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (event.getSource() == saveAsToFileMenuItem) {
                try {
                    controller.saveLayersToFile(controller.getNewFilePathToSave());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (event.getSource() == exitMenuItem) {
                controller.checkExit();
            }
            else if (event.getSource() == canvasSizeSettingMenuItem) {
                controller.setCanvasSize();
            }
            else if (event.getSource() == canvasBackgroundColorSettingMenuItem) {
                controller.setCanvasBackgroundColor();
            }
        }
    }
    class ButtonClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == createNewCanvasButton){
                controller.createNewCanvas();
            }
            else if (event.getSource() == loadFromFileButton){
                try {
                    controller.loadLayersFromFile(controller.getFilePathToOpen());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (event.getSource() == saveToFileButton){
                try {
                    controller.saveLayersToFile(controller.getFilePathToSave());
                } catch (IOException ex) {
                    Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (event.getSource() == drawLineButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.LINE);
            }
            else if (event.getSource() == drawRectangleButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.RECTANGLE);
            }
            else if (event.getSource() == drawEllipseButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.ELLIPSE);
            }
            else if (event.getSource() == drawTriangleButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.TRIANGLE);
            }
            else if (event.getSource() == drawRhombusButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.RHOMBUS);
            }
            else if (event.getSource() == idleButton){
                variable.setFunctionType(FunctionType.IDLE);
            }
            else if (event.getSource() == moveShapeButton){
                variable.setFunctionType(FunctionType.MOVE);
            }
            else if (event.getSource() == resizeShapeButton){
                variable.setFunctionType(FunctionType.RESIZE);
            }
            else if (event.getSource() == rotateShapeButton){
                variable.setFunctionType(FunctionType.ROTATE);
            }
            else if (event.getSource() == clearButton) controller.clearLayer();
            else if (event.getSource() == chooseColorButton) controller.chooseColor();
            else if (event.getSource() == emptyBackgroundTypeButton) {
                variable.setBackgroundType(BackgroundType.EMPTY);
                controller.changeShapeLayerBackground(controller.getSelectedLayerIndex());
            }
            else if (event.getSource() == fillBackgroundTypeButton) {
                variable.setBackgroundType(BackgroundType.FILL);
                controller.changeShapeLayerBackground(controller.getSelectedLayerIndex());
            }
        }
    }
    class WindowActionListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            controller.checkExit();
        }
    }
    
    /*
    ** 버튼 배경색 지정 메소드
    */
    public void changeBackgroundOnlySelectedButton(ArrayList<JButton> buttonsArrayList, JButton selectedButton){
        for (JButton button : buttonsArrayList) {
           if(selectedButton != null && button.equals(selectedButton)) button.setBackground(Color.CYAN);
           else button.setBackground(Color.WHITE);
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
    public void updateLayerContainer() {
        canvasInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
        sidebar.refreshLayerList();
        frame.repaint();
    }
    public void updateVariable() {
        setBackgroundOnlySelectedButton();
        chooseColorButton.setBackground(variable.getColor()); // chooseColorButton의 배경색 새로고침
        frame.setTitle(controller.generateViewTitle());
        frame.repaint();
    }
    
}
