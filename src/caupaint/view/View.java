
package caupaint.view;
import caupaint.model.*;
import caupaint.controller.*;
import caupaint.model.Enum.*;
import caupaint.observer.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

public class View implements LayerObserver, VariableObserver{
    
    private Layer layer;
    private Variable variable;
    private Controller controller;
    
    private JFrame frame;
    private Canvas canvas;
    private Sidebar sidebar;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem exitMenuItem;
    
    private JButton idleButton;
    private JButton drawRectangleButton;
    private JButton drawOvalButton;
    private JButton moveShapeButton;
    private JButton resizeShapeButton;
    private JButton deleteShapeButton;
    private JButton clearButton;
    private JButton chooseColorButton;

    private JToolBar toolBar;
    
    /*
    ** 생성자
    */
    public View(Layer layer, Variable variable, Controller controller) {
        this.layer = layer;
        this.variable = variable;
        this.controller = controller;
        
        layer.registerLayerObserver(this); // LayerObserver를 구현하는 클래스에 옵저버로 등록
        variable.registerVariableObserver(this); // VariableObserver를 구현하는 클래스에 옵저버로 등록        
    }
    
    /*
    ** 윈도우 생성
    */
    public void createView() {
        // 프레임 및 기본 구성요소 생성
        frame = new JFrame("View");
        canvas = new Canvas(layer, controller);
        sidebar = new Sidebar(layer, controller);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //메뉴바 생성
        menuBar = new JMenuBar();
        fileMenu = new JMenu("파일");
        exitMenuItem = new JMenuItem("종료(구현하지 않음)");
        
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        // 버튼 생성
        idleButton = new JButton(new ImageIcon("src/caupaint/source/icon/cursor.png"));   
        idleButton.setToolTipText("어떠한 입력에도 반응하지 않고 대기합니다.");
        drawRectangleButton = new JButton(new ImageIcon("src/caupaint/source/icon/rectangle.png"));
        drawRectangleButton.setToolTipText("마우스로 드래그하여 직사각형을 그립니다.");
        drawOvalButton = new JButton(new ImageIcon("src/caupaint/source/icon/oval.png"));
        drawOvalButton.setToolTipText("마우스로 드래그하여 타원을 그립니다.");
        moveShapeButton =  new JButton(new ImageIcon("src/caupaint/source/icon/move.png"));
        moveShapeButton.setToolTipText("선택한 도형을 이동합니다.");
        resizeShapeButton =  new JButton(new ImageIcon("src/caupaint/source/icon/resize.png"));
        resizeShapeButton.setToolTipText("선택한 도형의 크기를 변경합니다.");
        deleteShapeButton = new JButton(new ImageIcon("src/caupaint/source/icon/delete.png"));
        deleteShapeButton.setToolTipText("선택한 도형을 삭제합니다.");
        clearButton = new JButton(new ImageIcon("src/caupaint/source/icon/clear.png"));
        clearButton.setToolTipText("모든 도형을 삭제합니다.");
        chooseColorButton = new JButton(new ImageIcon("src/caupaint/source/icon/bgcolor.png"));
        chooseColorButton.setToolTipText("색상을 설정합니다.");
        chooseColorButton.setBackground(variable.getColor());
        
        // 툴바 생성
        toolBar = new JToolBar();
        
        // 버튼을 툴바에 추가함
        toolBar.add(idleButton);
        toolBar.add(drawRectangleButton);
        toolBar.add(drawOvalButton);
        toolBar.add(moveShapeButton);
        //toolBar.add(resizeShapeButton);
        toolBar.addSeparator();
        toolBar.add(deleteShapeButton);
        toolBar.addSeparator();
        toolBar.add(clearButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("색상 "));
        toolBar.add(chooseColorButton);
        
        // 메뉴를 리스너에 등록함
        exitMenuItem.addActionListener(new MenuBarClickedActionListener());
        
        // 버튼을 리스너에 등록함
        idleButton.addActionListener(new ButtonClickedActionListener());
        drawRectangleButton.addActionListener(new ButtonClickedActionListener());
        drawOvalButton.addActionListener(new ButtonClickedActionListener());
        moveShapeButton.addActionListener(new ButtonClickedActionListener());
        resizeShapeButton.addActionListener(new ButtonClickedActionListener());
        deleteShapeButton.addActionListener(new ButtonClickedActionListener());
        clearButton.addActionListener(new ButtonClickedActionListener());
        chooseColorButton.addActionListener(new ButtonClickedActionListener());
        
        // 레이아웃 지정
        frame.getContentPane().add(BorderLayout.NORTH, toolBar);
        frame.getContentPane().add(BorderLayout.EAST, sidebar);
        frame.getContentPane().add(BorderLayout.CENTER, canvas);
        
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
            if (event.getSource() == exitMenuItem) {};
        }
    }
    class ButtonClickedActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == idleButton){
                variable.setFunctionType(FunctionType.IDLE);
                //canvas.deactivateCanvasMouseAdapter();
            }
            else if (event.getSource() == drawRectangleButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.RECTANGLE);
                //canvas.activateCanvasMouseAdapter();
            }
            else if (event.getSource() == drawOvalButton){
                variable.setFunctionType(FunctionType.DRAW);
                variable.setShapeType(ShapeType.OVAL);
                //canvas.activateCanvasMouseAdapter();
            }
            else if (event.getSource() == moveShapeButton){
                variable.setFunctionType(FunctionType.MOVE);
            }
            else if (event.getSource() == resizeShapeButton){
                variable.setFunctionType(FunctionType.RESIZE);
            }
            else if (event.getSource() == deleteShapeButton) controller.deleteShape(sidebar.getLayerListSelectedIndex());
            else if (event.getSource() == clearButton) controller.clearLayer();
            else if (event.getSource() == chooseColorButton) controller.chooseColor();
        }
    }

   /*
    ** 옵저버 관련 메소드
    */
    public void updateLayer() {
        sidebar.refreshLayerListData();
        frame.repaint();
    }
    public void updateVariable() {
        chooseColorButton.setBackground(variable.getColor());
        frame.repaint();
    }
    
}
