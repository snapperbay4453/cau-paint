
package caupaint.controller;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.view.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

public class Controller{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    private CanvasView canvasView;
    private SidebarView sidebarView;
    private MainView mainView;
    
    /*
    ** 생성자
    */
    public Controller() {
        canvasContainer = new CanvasContainer();
        variable = new Variable(this);
        canvasView = new CanvasView(canvasContainer, variable, this);
        sidebarView = new SidebarView(canvasContainer, variable, this);
        mainView = new MainView(canvasContainer, variable, canvasView, sidebarView, this);
        mainView.createView();
    }
    
    /*
    ** Layer 관련 메소드
    */
    public void renameLayer(int index) {
        canvasContainer.renameLayer(index);
    }
    public void changeLayerBackground(int index) {
        if (variable.getSelectedLayerIndex() != -1) {
            canvasContainer.getShapeLayerArrayList().get(index).setBackgroundType(variable.getBackgroundType()); // 도형의 배경 채우기 옵션 설정
            canvasContainer.getShapeLayerArrayList().get(index).setColor(variable.getColor()); // 도형의 color 변경
        }
        else return;
    }
    
    /*
    ** Variable 관련 메소드
    */
    public String getMainViewWindowTitle(){ // 파일 주소 존재 여부에 따라 프로그램의 제목 표시줄을 다르게 설정
        return variable.generateMainViewWindowTitle();
    }

    /*
    ** Canvas 관련 메소드
    */
    public Point getCanvasSize(){
        return canvasContainer.getCanvasSize();
    }
    public Color getCanvasBackgroundColor(){
        return canvasContainer.getCanvasBackgroundColor();
    }
    public void createNewCanvas() { // 캔버스와 레이어를 모두 초기화
        canvasContainer.deleteAllLayers();
        variable.setFilePath(Constant.defaultFilePath);
        canvasContainer.clearCanvas();
    }
    
    /*
    ** I/O 관련 메소드
    */
    public String getFilePathToOpen() {
        JFileChooser fileChooser = new JFileChooser();
        
        if (!canvasContainer.getShapeLayerArrayList().isEmpty()) {
            if (JOptionPane.showConfirmDialog
                (null, "파일을 불러오기 전 캔버스의 모든 도형을 삭제해야 합니다.\n계속하시겠습니까?", "모든 도형 삭제",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return null; // 파일 불러오기를 취소한 경우
        }
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            canvasContainer.getShapeLayerArrayList().clear(); // 불러오기 전에 현재 레이어를 모두 지움
            canvasContainer.notifyCanvasContainerObservers();
            return fileChooser.getSelectedFile().getPath();
        } // 대화상자를 불러온 후 파일 불러오기에 성공한 경우, 그 절대 주소를 반환함
        else return null; // 대화상자를 불러온 후 파일 불러오기에 실패한 경우
    }
    public void loadLayersFromFile(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null) return;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension 을 붙이지 않음
            CanvasContainer loadedCanvasContainer = (CanvasContainer)is.readObject();
            is.close();
            
            for (int i = 0; i < loadedCanvasContainer.getShapeLayerArrayList().size(); i++) {
                canvasContainer.addLayerToArrayList(loadedCanvasContainer.getShapeLayerArrayList().get(i));
            }
            canvasContainer.setCanvasSize(loadedCanvasContainer.getCanvasSize());
            canvasContainer.setCanvasBackgroundColor(loadedCanvasContainer.getCanvasBackgroundColor());
            
            variable.setFilePath(filePath);
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 불러오기에 실패하였습니다.", "불러오기 실패", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getFilePathToSave() { // 파일을 저장할 경로를 반환하는 메서드
        if (variable.getFilePath() != null) return variable.getFilePath(); // variable에 이미 기존의 파일 경로가 저장되어 있을 경우, 기존의 경로를 반환함
        else {
            JFileChooser jFileChooser = new JFileChooser();
            if(jFileChooser.showSaveDialog(null) == 0) return jFileChooser.getSelectedFile().getPath(); // 대화상자를 불러온 후 파일 저장 위치 확인에 성공한 경우, 그 절대 주소를 반환함
            else return null; // 대화상자를 불러온 후 파일 저장 위치 확인에 실패한 경우
        }
    }
    public String getNewFilePathToSave() { // 파일을 다른 이름으로 저장할 경로를 반환하는 메서드, variable에 경로가 저장되어 있는지는 관계 없음
        JFileChooser fileChooser = new JFileChooser();

        if(fileChooser.showSaveDialog(null) == 0) return fileChooser.getSelectedFile().getPath(); // 대화상자를 불러온 후 파일 저장 위치 확인에 성공한 경우, 그 절대 주소를 반환함
        else return null; // 대화상자를 불러온 후 파일 저장 위치 확인에 실패한 경우
    }
    public void saveLayersToFile(String filePath) throws IOException {
        if (filePath == null) {
            if (variable.getFilePath() == null) return; // 매개변수와 variable 모두 파일 경로가 지정되어 있지 않은 경우
        }
        
        ObjectOutputStream os;
        try {
            if (filePath.endsWith(Constant.defaultFilenameExtension)) os = new ObjectOutputStream(new FileOutputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension을 붙이지 않음
            else os = new ObjectOutputStream(new FileOutputStream(filePath + Constant.defaultFilenameExtension)); // filePath에 확장자가 포함되어 있지 않기 때문에 filenameExtension을 붙임
            os.writeObject(canvasContainer);
            JOptionPane.showMessageDialog(null, "파일을 저장하였습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            os.close();
            if (filePath.endsWith(Constant.defaultFilenameExtension)) variable.setFilePath(filePath);
            else variable.setFilePath(filePath + Constant.defaultFilenameExtension);
            mainView.updateCanvasContainer();
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 저장에 실패하였습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();
        }
    }
    
    /*
    ** 설정 관련 메소드
    */
    public void checkExit() {
        if (canvasContainer.getShapeLayerArrayList().isEmpty() == false) { // 레이어가 하나라도 남아있을 경우
            switch ((JOptionPane.showConfirmDialog(null, "캔버스에 도형이 남아있습니다.\n정말 종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE))) {
            case JOptionPane.YES_OPTION:
                System.exit(0);
                break;
            case JOptionPane.NO_OPTION:
                break;
            }
               }
        else System.exit(0); // 레이어가 비어있을 경우
    }

    
    /*
    ** MainView 이벤트 리스너 관련 메소드
    */
    
    // 이벤트 리스너: mainView.MenuBarClickedActionListener
    public void MainViewMenuBarClickedEventHandler(ActionEvent event) {
        if (event.getActionCommand() == "createNewCanvas") {
            createNewCanvas();
        }
        if (event.getActionCommand() == "loadFromFile") {
            try {
                loadLayersFromFile(getFilePathToOpen());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveToFile") {
            try {
                saveLayersToFile(getFilePathToSave());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveAsToFile") {
            try {
                saveLayersToFile(getNewFilePathToSave());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "exit") {
            checkExit();
        }
        else if (event.getActionCommand() == "canvasSizeSetting") {
            canvasContainer.showSetCanvasSizeDialogBox();
        }
        else if (event.getActionCommand() == "canvasBackgroundColorSetting") {
            canvasContainer.showSetCanvasBackgroundColorDialogBox();
        }
    }
    
    // 이벤트 리스너: mainView.ButtonClickedActionListener
    public void MainViewButtonClickedEventHandler(ActionEvent event) {
        if (event.getActionCommand() == "createNewCanvas"){
            createNewCanvas();
        }
        else if (event.getActionCommand() == "loadFromFile"){
            try {
                loadLayersFromFile(getFilePathToOpen());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveToFile"){
            try {
                saveLayersToFile(getFilePathToSave());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "drawLine"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.LINE);
        }
        else if (event.getActionCommand() == "drawPolyline"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.POLYLINE);
        }
        else if (event.getActionCommand() == "drawRectangle"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.RECTANGLE);
        }
        else if (event.getActionCommand() == "drawEllipse"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.ELLIPSE);
        }
        else if (event.getActionCommand() == "drawTriangle"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.TRIANGLE);
        }
        else if (event.getActionCommand() == "drawRhombus"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.RHOMBUS);
        }
        else if (event.getActionCommand() == "drawText"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.TEXT);
        }
        else if (event.getActionCommand() == "idle"){
            variable.setFunctionType(FunctionType.IDLE);
        }
        else if (event.getActionCommand() == "moveShape"){
            variable.setFunctionType(FunctionType.MOVE);
        }
        else if (event.getActionCommand() == "resizeShape"){
            variable.setFunctionType(FunctionType.RESIZE);
        }
        else if (event.getActionCommand() == "rotateShape"){
            variable.setFunctionType(FunctionType.ROTATE);
        }
        else if (event.getActionCommand() == "chooseColor") variable.chooseColor();
        else if (event.getActionCommand() == "emptyBackgroundType") {
            variable.setBackgroundType(BackgroundType.EMPTY);
            changeLayerBackground(variable.getSelectedLayerIndex());
        }
        else if (event.getActionCommand() == "fillBackgroundType") {
            variable.setBackgroundType(BackgroundType.FILL);
            changeLayerBackground(variable.getSelectedLayerIndex());
        }
    }
    // 이벤트 리스너: mainView.StrokeTypeItemChangeActionListener
    public void MainViewStrokeTypeComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {

            if (event.getItem().toString() == "실선") { variable.setStrokeDash(Constant.defaultSolidLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "점선") { variable.setStrokeDash(Constant.defaultDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "파선") { variable.setStrokeDash(Constant.defaultDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "긴파선") { variable.setStrokeDash(Constant.defaultLongDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "1점 쇄선") { variable.setStrokeDash(Constant.defaultDashSingleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "2점 쇄선") { variable.setStrokeDash(Constant.defaultDashDoubleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }

            if (variable.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(variable.getSelectedLayerIndex(), variable.getStroke());
       }
    }       
    // 이벤트 리스너: mainView.StrokeWidthSpinnerChangeActionListener
    public void MainViewStrokeWidthSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       if (spinnerValue <= 0 || spinnerValue >= 100) return;
       else {
           variable.setStrokeWidth(spinnerValue);
           if (variable.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(variable.getSelectedLayerIndex(), variable.getStroke());
       }
    }
    // 이벤트 리스너: mainView.FontNameComboBoxItemChangeActionListener
    public void MainViewFontNameComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
            variable.setFontName(event.getItem().toString());
            if ((variable.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(variable.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
               canvasContainer.setLayerFont(variable.getSelectedLayerIndex(), variable.getFont());
            }
       }
    }       
    // 이벤트 리스너: mainView.FontSizeSpinnerStateChangeActionListener
    public void MainViewFontSizeSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       if (spinnerValue <= 0 || spinnerValue >= 500) return;
       else {
           variable.setFontSize(spinnerValue);
           if ((variable.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(variable.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
                canvasContainer.setLayerFont(variable.getSelectedLayerIndex(), variable.getFont());
           }
       }
    }
    // 이벤트 리스너: mainView.WindowActionListener
    public void MainViewWindowClosingEventHandler(WindowEvent e) {
        checkExit();
    }

    
    /*
    ** Canvas 이벤트 리스너 관련 메소드
    */
    
    // 이벤트 리스너: canvasView.CanvasMouseAdapter
    public void CanvasViewMousePressedEventHandler(MouseEvent event) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                switch(variable.getShapeType()) {
                    case LINE:
                    case RECTANGLE:
                    case ELLIPSE:
                    case TRIANGLE:
                    case RHOMBUS:
                    case TEXT:
                        canvasContainer.createLayer(variable.getShapeType(), event.getPoint(), variable.getColor(), variable.getStroke(), variable.getFont(), variable.getBackgroundType());
                        canvasContainer.initializeLayer(event.getPoint());
                        break;
                    case POLYLINE:
                        if ((canvasContainer.getShapeLayerArrayList().isEmpty() == false) // 1. ShapeLayerArrayList가 비어있지 않고
                                && (canvasContainer.getShapeLayerArrayList().get(canvasContainer.getShapeLayerArrayList().size() - 1).getRealShapeType() == ShapeType.POLYLINE) // 2. ShapeLayerArrayList의 가장 마지막 레이어가 Polyline 타입이고
                                && (((PolylineLayer)(canvasContainer.getShapeLayerArrayList().get(canvasContainer.getShapeLayerArrayList().size() - 1))).getIsFinishedInitializing() == false )) { // 3. 그 레이어의 생성이 완료되었다고 표시되었을 시
                            canvasContainer.initializeLayer(event.getPoint());
                        }
                        else {
                            canvasContainer.createLayer(variable.getShapeType(), event.getPoint(), variable.getColor(), variable.getStroke(), variable.getFont(), variable.getBackgroundType());
                            canvasContainer.initializeLayer(event.getPoint());
                        }
                        break;
                }
                break;
            case MOVE:
            case RESIZE:
            case ROTATE:
                break;
            default:
                break;
        }
    variable.setRecentlyPressedMousePosition(event.getPoint());
    variable.setRecentlyDraggedMousePosition(event.getPoint());
    }
    public void CanvasViewMouseDraggedEventHandler(MouseEvent event) {
        switch(variable.getFunctionType()) {
            case IDLE:      break;
            case DRAW:      canvasContainer.keepInitializingLayer(variable.getRecentlyPressedMousePosition(), event.getPoint());                              break;
            case MOVE:      canvasContainer.moveLayer(variable.getLastSelectedLayerIndex(), variable.getRecentlyDraggedMousePosition(), event.getPoint());    break;
            case RESIZE:    canvasContainer.resizeLayer(variable.getLastSelectedLayerIndex(), variable.getRecentlyDraggedMousePosition(), event.getPoint());  break;
            case ROTATE:    canvasContainer.rotateLayer(variable.getLastSelectedLayerIndex(), variable.getRecentlyDraggedMousePosition(), event.getPoint());  break;
            default:        break;
        }
    variable.setRecentlyDraggedMousePosition(event.getPoint());
    }
    public void CanvasViewMouseReleasedEventHandler(MouseEvent event) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                canvasContainer.finishInitializingLayer();
                break;
            case MOVE:
            case RESIZE:
            case ROTATE:
                break;
            default:
                break;
        }
    }
    
    /*
    ** Sidebar 리스너 관련 메소드
    */
    
    // 이벤트 리스너: sidebarView.LayerListSelectionListener
        public void SidebarValueChangedEventHandler(ListSelectionEvent event, int index){
            variable.setSelectedLayerIndex(index);
        }
    // 이벤트 리스너: sidebarView.ButtonClickedActionListener
        public void SidebarActionPerformedEventHandler(ActionEvent event, int index) {
            if (event.getActionCommand() == "toggleSelectedLayerVisible") canvasContainer.toggleLayerIsVisible(index);
            else if (event.getActionCommand()== "moveSelectedLayerFront") canvasContainer.swapNearLayers(index, index - 1);
            else if (event.getActionCommand() == "moveSelectedLayerBack") canvasContainer.swapNearLayers(index, index + 1);
            else if (event.getActionCommand() == "renameSelectedLayer") canvasContainer.renameLayer(index);
            else if (event.getActionCommand() == "copySelectedLayer") canvasContainer.copyLayer(index);
            else if (event.getActionCommand() == "deleteSelectedLayer") canvasContainer.deleteLayer(index);
            else if (event.getActionCommand() == "deleteAllLayer") canvasContainer.deleteAllLayers();
        }
    
}
