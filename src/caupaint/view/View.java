
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
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
    private JMenuItem exitMenuItem;
    private JMenu modifyMenu;
    private JMenuItem canvasSizeSettingMenuItem;
    private JMenuItem canvasBackgroundColorSettingMenuItem;
                    
    
    private JButton idleButton;
    private JButton drawLineButton;
    private JButton drawRectangleButton;
    private JButton drawEllipseButton;
    private JButton drawTriangleButton;
    private JButton drawRhombusButton;
    private JButton moveShapeButton;
    private JButton resizeShapeButton;
    private JButton rotateShapeButton;
    private JButton deleteShapeButton;
    private JButton clearButton;
    private JButton chooseColorButton;
    private JButton emptyBackgroundTypeButton;
    private JButton fillBackgroundTypeButton;

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
        canvas = new Canvas(layerContainer, variable, controller); // 도형이 그려지는 Panel
        canvasInnerContainerPanel = new JPanel();
        canvasContainerScrollPane = new JScrollPane(canvas); // canvas가 스크롤이 가능하도록 함

        sidebar = new Sidebar(layerContainer, controller);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //메뉴바 생성
        menuBar = new JMenuBar();
        fileMenu = new JMenu("파일");
        exitMenuItem = new JMenuItem("종료(구현하지 않음)");
        modifyMenu = new JMenu("편집");
        canvasSizeSettingMenuItem = new JMenuItem("캔버스 크기 설정");
        canvasBackgroundColorSettingMenuItem = new JMenuItem("캔버스 배경색 설정");
        
        fileMenu.add(exitMenuItem);
        modifyMenu.add(canvasSizeSettingMenuItem);
        modifyMenu.addSeparator();
        modifyMenu.add(canvasBackgroundColorSettingMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(modifyMenu);
        
        // 버튼 생성
        idleButton = new JButton(new ImageIcon("src/caupaint/source/icon/cursor.png"));   
        idleButton.setToolTipText("어떠한 입력에도 반응하지 않고 대기합니다.");
        drawLineButton = new JButton(new ImageIcon("src/caupaint/source/icon/line.png"));
        drawLineButton.setToolTipText("마우스로 드래그하여 직선을 그립니다.");
        drawRectangleButton = new JButton(new ImageIcon("src/caupaint/source/icon/rectangle.png"));
        drawRectangleButton.setToolTipText("마우스로 드래그하여 직사각형을 그립니다.");
        drawEllipseButton = new JButton(new ImageIcon("src/caupaint/source/icon/ellipse.png"));
        drawEllipseButton.setToolTipText("마우스로 드래그하여 타원을 그립니다.");
        drawTriangleButton = new JButton(new ImageIcon("src/caupaint/source/icon/triangle.png"));
        drawTriangleButton.setToolTipText("마우스로 드래그하여 삼각형을 그립니다.");
        drawRhombusButton = new JButton(new ImageIcon("src/caupaint/source/icon/rhombus.png"));
        drawRhombusButton.setToolTipText("마우스로 드래그하여 마름모를 그립니다.");
        moveShapeButton =  new JButton(new ImageIcon("src/caupaint/source/icon/move.png"));
        moveShapeButton.setToolTipText("선택한 도형을 이동합니다.");
        resizeShapeButton =  new JButton(new ImageIcon("src/caupaint/source/icon/resize.png"));
        resizeShapeButton.setToolTipText("선택한 도형의 크기를 변경합니다.");
        rotateShapeButton = new JButton(new ImageIcon("src/caupaint/source/icon/rotate.png"));
        rotateShapeButton.setToolTipText("선택한 도형을 회전시킵니다.");
        deleteShapeButton = new JButton(new ImageIcon("src/caupaint/source/icon/delete.png"));
        deleteShapeButton.setToolTipText("선택한 도형을 삭제합니다.");
        clearButton = new JButton(new ImageIcon("src/caupaint/source/icon/clear.png"));
        clearButton.setToolTipText("모든 도형을 삭제합니다.");
        chooseColorButton = new JButton(new ImageIcon("src/caupaint/source/icon/bgcolor.png"));
        chooseColorButton.setToolTipText("색상을 설정합니다.");
        chooseColorButton.setBackground(variable.getColor());
        emptyBackgroundTypeButton = new JButton(new ImageIcon("src/caupaint/source/icon/background_empty.png"));
        emptyBackgroundTypeButton.setToolTipText("도형의 배경이 비어있도록 설정합니다.");
        fillBackgroundTypeButton = new JButton(new ImageIcon("src/caupaint/source/icon/background_fill.png"));
        fillBackgroundTypeButton.setToolTipText("도형의 배경이 선택한 색상으로 채워지도록 설정합니다.");
        
        // 툴바 생성
        toolBar = new JToolBar();
        
        // 버튼을 툴바에 추가함
        toolBar.add(idleButton);
        toolBar.addSeparator();
        toolBar.add(drawLineButton);
        toolBar.add(drawRectangleButton);
        toolBar.add(drawEllipseButton);
        toolBar.add(drawTriangleButton);
        toolBar.add(drawRhombusButton);    
        toolBar.addSeparator();
        toolBar.add(moveShapeButton);
        toolBar.add(resizeShapeButton);
        toolBar.add(rotateShapeButton);
        toolBar.addSeparator();
        toolBar.add(deleteShapeButton);
        toolBar.addSeparator();
        toolBar.add(clearButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("색상 "));
        toolBar.add(chooseColorButton);
        toolBar.addSeparator();
        toolBar.add(emptyBackgroundTypeButton);
        toolBar.add(fillBackgroundTypeButton);
        
        // 메뉴를 리스너에 등록함
        exitMenuItem.addActionListener(new MenuBarClickedActionListener());
        canvasSizeSettingMenuItem.addActionListener(new MenuBarClickedActionListener());
        canvasBackgroundColorSettingMenuItem.addActionListener(new MenuBarClickedActionListener());
                
        // 버튼을 리스너에 등록함
        idleButton.addActionListener(new ButtonClickedActionListener());
        drawLineButton.addActionListener(new ButtonClickedActionListener());
        drawRectangleButton.addActionListener(new ButtonClickedActionListener());
        drawEllipseButton.addActionListener(new ButtonClickedActionListener());
        drawTriangleButton.addActionListener(new ButtonClickedActionListener());
        drawRhombusButton.addActionListener(new ButtonClickedActionListener());
        moveShapeButton.addActionListener(new ButtonClickedActionListener());
        resizeShapeButton.addActionListener(new ButtonClickedActionListener());
        rotateShapeButton.addActionListener(new ButtonClickedActionListener());
        deleteShapeButton.addActionListener(new ButtonClickedActionListener());
        clearButton.addActionListener(new ButtonClickedActionListener());
        chooseColorButton.addActionListener(new ButtonClickedActionListener());
        emptyBackgroundTypeButton.addActionListener(new ButtonClickedActionListener());
        fillBackgroundTypeButton.addActionListener(new ButtonClickedActionListener()); 
        
        // 레이아웃 지정
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        frame.getContentPane().add(sidebar, BorderLayout.EAST);
        canvasInnerContainerPanel.add(canvas);
        canvasContainerScrollPane.setViewportView(canvasInnerContainerPanel);
        frame.getContentPane().add(canvasContainerScrollPane, BorderLayout.CENTER);
        
        // 프레임 설정
        frame.setTitle("CauPaint");
        frame.setSize(1280,720);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);    
    }
    
    /*
    ** 리스너 관련 메소드
    */
    class MenuBarClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == exitMenuItem) {

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
            if (event.getSource() == idleButton){
                variable.setFunctionType(FunctionType.IDLE);
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
            else if (event.getSource() == moveShapeButton){
                variable.setFunctionType(FunctionType.MOVE);
            }
            else if (event.getSource() == resizeShapeButton){
                variable.setFunctionType(FunctionType.RESIZE);
            }
            else if (event.getSource() == rotateShapeButton){
                variable.setFunctionType(FunctionType.ROTATE);
            }
            else if (event.getSource() == deleteShapeButton) controller.deleteShapeLayer(sidebar.getLayerListSelectedIndex());
            else if (event.getSource() == clearButton) controller.clearLayer();
            else if (event.getSource() == chooseColorButton) controller.chooseColor();
            else if (event.getSource() == emptyBackgroundTypeButton) variable.setBackgroundType(BackgroundType.EMPTY);
            else if (event.getSource() == fillBackgroundTypeButton) variable.setBackgroundType(BackgroundType.FILL);
        }
    }

   /*
    ** 옵저버 관련 메소드
    */
    public void updateLayerContainer() {
        sidebar.refreshLayerList();
        frame.repaint();
    }
    public void updateVariable() {
        chooseColorButton.setBackground(variable.getColor()); // chooseColorButton의 배경색 새로고침
        canvasInnerContainerPanel.revalidate(); // canvasInnerContainerPanel 새로고침
        frame.repaint();
    }
    
}
