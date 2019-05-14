
package caupaint.controller;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.model.command.CommandFactory;
import caupaint.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.*;
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
    ** I/O 관련 메소드
    */
    
    /*
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
    public String getImageFilePathToOpen() {
        JFileChooser fileChooser = new JFileChooser();
        
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
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
            
            canvasContainer.setFilePath(filePath);
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 불러오기에 실패하였습니다.", "불러오기 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
    public String getFilePathToSave() { // 파일을 저장할 경로를 반환하는 메서드
        if (canvasContainer.getFilePath() != null) return canvasContainer.getFilePath(); // variable에 이미 기존의 파일 경로가 저장되어 있을 경우, 기존의 경로를 반환함
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
            if (canvasContainer.getFilePath() == null) return; // 매개변수와 variable 모두 파일 경로가 지정되어 있지 않은 경우
        }
        ObjectOutputStream os;
        try {
            if (filePath.endsWith(Constant.defaultFilenameExtension)) os = new ObjectOutputStream(new FileOutputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension을 붙이지 않음
            else os = new ObjectOutputStream(new FileOutputStream(filePath + Constant.defaultFilenameExtension)); // filePath에 확장자가 포함되어 있지 않기 때문에 filenameExtension을 붙임
            os.writeObject(canvasContainer);
            JOptionPane.showMessageDialog(null, "파일을 저장하였습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            os.close();
            if (filePath.endsWith(Constant.defaultFilenameExtension)) canvasContainer.setFilePath(filePath);
            else canvasContainer.setFilePath(filePath + Constant.defaultFilenameExtension);
            mainView.updateCanvasContainer();
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 저장에 실패하였습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();
        }
    }
    */
    
    /*
    ** 설정 관련 메소드
    */
    /*
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
*/
    
    /*
    ** MainView 이벤트 리스너 관련 메소드
    */
    // 이벤트 리스너: mainView.MenuBarClickedActionListener
    public void MainViewMenuBarClickedEventHandler(ActionEvent event) {

        CommandFactory.createCommand(event.getActionCommand(), canvasContainer, variable, event, null).execute();
        /*
        if (event.getActionCommand() == "exit") {
            checkExit();    return;
        }
        else if (event.getActionCommand() == "createNewCanvas") {
            canvasContainer.createNewCanvas();
        }
        else if (event.getActionCommand() == "loadFromFile") {
            try {
                canvasContainer.loadLayersFromFile(canvasContainer.getFilePathToOpen());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveToFile") {
            try {
                canvasContainer.saveLayersToFile(canvasContainer.getFilePathToSave());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveAsToFile") {
            try {
                canvasContainer.saveLayersToFile(canvasContainer.getNewFilePathToSave());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "exit") {
            checkExit();
        }
        else if (event.getActionCommand() == "setCanvasSize") {
            canvasContainer.showSetCanvasSizeDialogBox();
        }
        else if (event.getActionCommand() == "setCanvasBackgroundColor") {
            canvasContainer.showSetCanvasBackgroundColorDialogBox();
        }
        /*
        else if (event.getActionCommand() == "flipLayerHorizontally") {
            canvasContainer.flipLayerHorizontally(canvasContainer.getSelectedLayerIndex());
        }
        else if (event.getActionCommand() == "flipLayerVertically") {
            canvasContainer.flipLayerVertically(canvasContainer.getSelectedLayerIndex());
        }
        */
    }
    // 이벤트 리스너: mainView.ButtonClickedActionListener
    public void MainViewButtonClickedEventHandler(ActionEvent event) {
    CommandFactory.createCommand(event.getActionCommand(), canvasContainer, variable, event, null).execute();
    
        /*
        if (event.getActionCommand() == "createNewCanvas"){
            canvasContainer.createNewCanvas();
        }
        else if (event.getActionCommand() == "loadFromFile"){
            try {
                canvasContainer.loadLayersFromFile(canvasContainer.getFilePathToOpen());
            } catch (IOException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (event.getActionCommand() == "saveToFile"){
            try {
                canvasContainer.saveLayersToFile(canvasContainer.getFilePathToSave());
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
        else if (event.getActionCommand() == "drawPen"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.PEN);
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
        else if (event.getActionCommand() == "insertText"){
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.TEXT);
            canvasContainer.insertTextLayer(variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getBackgroundType(), variable.getFont());
        }
        else if (event.getActionCommand() == "insertImage"){
            //String imagePath = canvasContainer.getImageFilePathToOpen();
            //if (imagePath == null) return;
            variable.setFunctionType(FunctionType.DRAW);
            variable.setShapeType(ShapeType.IMAGE);
            canvasContainer.insertImageLayer();
            //canvasContainer.createNewLayer(variable.getShapeType(), new Point(0, 0), variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getFont(), variable.getBackgroundType(), imagePath);
        }
        else if (event.getActionCommand() == "selectShape"){
            variable.setFunctionType(FunctionType.SELECT);
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
        else if (event.getActionCommand() == "chooseBorderColor") variable.chooseBorderColor();
        else if (event.getActionCommand() == "chooseBackgroundColor") variable.chooseBackgroundColor();
        else if (event.getActionCommand() == "emptyBackgroundType") {
            variable.setBackgroundType(BackgroundType.EMPTY);
            if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.changeLayerBackgroundTypeAndColors(canvasContainer.getSelectedLayerIndex(), variable.getBackgroundType(), variable.getBorderColor(), variable.getBackgroundColor());
        }
        else if (event.getActionCommand() == "fillBackgroundType") {
            variable.setBackgroundType(BackgroundType.FILL);
            if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.changeLayerBackgroundTypeAndColors(canvasContainer.getSelectedLayerIndex(), variable.getBackgroundType(), variable.getBorderColor(), variable.getBackgroundColor());
        }
            */
    }
        
    // 이벤트 리스너: mainView.StrokeTypeItemChangeActionListener
    public void MainViewStrokeTypeComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
            CommandFactory.createCommand("setStrokeByName", canvasContainer, variable, event, event.getItem().toString()).execute();
                      /*  
            variable.setStrokeByName(event.getItem().toString());

            if (event.getItem().toString() == "실선") { variable.setStrokeDash(Constant.defaultSolidLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "점선") { variable.setStrokeDash(Constant.defaultDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "파선") { variable.setStrokeDash(Constant.defaultDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "긴파선") { variable.setStrokeDash(Constant.defaultLongDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "1점 쇄선") { variable.setStrokeDash(Constant.defaultDashSingleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
            else if (event.getItem().toString() == "2점 쇄선") { variable.setStrokeDash(Constant.defaultDashDoubleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }

            if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(canvasContainer.getSelectedLayerIndex(), variable.getStroke());
                      */  
       }
    }       
    // 이벤트 리스너: mainView.StrokeWidthSpinnerChangeActionListener
    public void MainViewStrokeWidthSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       CommandFactory.createCommand("setStrokeWidth", canvasContainer, variable, event, spinnerValue).execute();
       /*
       if (spinnerValue <= 0 || spinnerValue >= 100) return;
       else {
           variable.setStrokeWidth(spinnerValue);
           if (canvasContainer.getSelectedLayerIndex() != -1) canvasContainer.setLayerStroke(canvasContainer.getSelectedLayerIndex(), variable.getStroke());
       }
       */
    }
    // 이벤트 리스너: mainView.FontNameComboBoxItemChangeActionListener
    public void MainViewFontNameComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
           CommandFactory.createCommand("setFontName", canvasContainer, variable, event, event.getItem().toString()).execute();
           /*
            variable.setFontName(event.getItem().toString());
            if ((canvasContainer.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
               canvasContainer.setLayerFont(canvasContainer.getSelectedLayerIndex(), variable.getFont());
            }
           */
       }
    }       
    // 이벤트 리스너: mainView.FontSizeSpinnerStateChangeActionListener
    public void MainViewFontSizeSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       if (spinnerValue <= 0 || spinnerValue >= 500) return;
       else {
           CommandFactory.createCommand("setFontSize", canvasContainer, variable, event, spinnerValue).execute();
           /*
           variable.setFontSize(spinnerValue);
           if ((canvasContainer.getSelectedLayerIndex() != -1) && canvasContainer.getShapeLayerArrayList().get(canvasContainer.getSelectedLayerIndex()).getRealShapeType() == ShapeType.TEXT) { // 현재 선택된 레이어가 텍스트 레이어인 경우
                canvasContainer.setLayerFont(canvasContainer.getSelectedLayerIndex(), variable.getFont());
           }
                      */
       }
    }
    // 이벤트 리스너: mainView.WindowActionListener
    public void MainViewWindowClosingEventHandler(WindowEvent event) {
        CommandFactory.createCommand("checkExit", canvasContainer, variable, event, null).execute();
        //checkExit();
    }
    
    /*
    ** Canvas 이벤트 리스너 관련 메소드
    */
    // 이벤트 리스너: canvasView.CanvasMouseAdapter
    public void CanvasViewMousePressedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.PRESSED);
    variable.setRecentlyPressedMousePosition(event.getPoint());
    variable.setRecentlyDraggedMousePosition(event.getPoint());
        switch(variable.getFunctionType()) {
            case SELECT:
                CommandFactory.createCommand("selectLayerByMousePoint", canvasContainer, variable, event, null).execute();  break;
                //canvasContainer.setSelectedLayerIndex(canvasContainer.selectLayerByMousePoint(event.getPoint()));  break;
            case DRAW:      CommandFactory.createCommand("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            /*
                switch(variable.getShapeType()) {
                    case LINE:
                    case PEN:
                    case RECTANGLE:
                    case ELLIPSE:
                    case TRIANGLE:
                    case RHOMBUS:
                    case TEXT:
                        canvasContainer.addNewLayer(variable.getShapeType(), event.getPoint(), variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getFont(), variable.getBackgroundType(), null);
                        canvasContainer.initializeLayer(event.getPoint());
                        break;
                    case POLYLINE:
                        if ((canvasContainer.getShapeLayerArrayList().isEmpty() == false) // 1. ShapeLayerArrayList가 비어있지 않고
                                && (canvasContainer.getShapeLayerArrayList().get(canvasContainer.getShapeLayerArrayList().size() - 1).getRealShapeType() == ShapeType.POLYLINE) // 2. ShapeLayerArrayList의 가장 마지막 레이어가 Polyline 타입이고
                                && (((PolylineLayer)(canvasContainer.getShapeLayerArrayList().get(canvasContainer.getShapeLayerArrayList().size() - 1))).getIsFinishedInitializing() == false )) { // 3. 그 레이어의 생성이 완료되지 않았다고 표시되었을 시
                            canvasContainer.initializeLayer(event.getPoint());
                        }
                        else {
                            canvasContainer.addNewLayer(variable.getShapeType(), event.getPoint(), variable.getBorderColor(), variable.getBackgroundColor(), variable.getStroke(), variable.getFont(), variable.getBackgroundType(), null);
                            canvasContainer.initializeLayer(event.getPoint());
                        }
                        break;
                    case IMAGE:
                        break;
                }
                break;
                */
            case MOVE:      CommandFactory.createCommand("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.createCommand("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.createCommand("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                //canvasContainer.modifyLayer(canvasContainer.getSelectedLayerIndex(), variable.getFunctionType(), MouseActionType.PRESSED, variable.getRecentlyDraggedMousePosition(), event.getPoint());
                //break;
            default:
                break;
        }
    }
    public void CanvasViewMouseDraggedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.DRAGGED);
        switch(variable.getFunctionType()) {
            case SELECT:
                break;
            case DRAW:      CommandFactory.createCommand("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case MOVE:      CommandFactory.createCommand("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.createCommand("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.createCommand("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                //canvasContainer.modifyLayer(canvasContainer.getSelectedLayerIndex(), variable.getFunctionType(), MouseActionType.DRAGGED, variable.getRecentlyDraggedMousePosition(), event.getPoint());
                //break;
            default:
                break;
        }
    variable.setRecentlyDraggedMousePosition(event.getPoint());
    }
    public void CanvasViewMouseReleasedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.RELEASED);
        switch(variable.getFunctionType()) {
            case SELECT:
                break;
            case DRAW:      CommandFactory.createCommand("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case MOVE:      CommandFactory.createCommand("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.createCommand("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.createCommand("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                //canvasContainer.modifyLayer(canvasContainer.getSelectedLayerIndex(), variable.getFunctionType(), MouseActionType.RELEASED, variable.getRecentlyDraggedMousePosition(), event.getPoint());
                //break;
            default:
                break;
        }
    }
    
    /*
    ** Sidebar 리스너 관련 메소드
    */
    // 이벤트 리스너: sidebarView.LayerListSelectionListener
        public void SidebarValueChangedEventHandler(ListSelectionEvent event, int index){
            CommandFactory.createCommand("setSelectedLayerIndex", canvasContainer, variable, event, index).execute();
            //canvasContainer.setSelectedLayerIndex(index);
        }
    // 이벤트 리스너: sidebarView.ButtonClickedActionListener
        public void SidebarActionPerformedEventHandler(ActionEvent event) {
            CommandFactory.createCommand(event.getActionCommand(), canvasContainer, variable, event, null).execute();
            /*
            if (event.getActionCommand() == "toggleSelectedLayerVisible") canvasContainer.toggleLayerIsVisible(canvasContainer.getSelectedLayerIndex());
            else if (event.getActionCommand()== "moveSelectedLayerFront") {
                if (canvasContainer.swapNearLayers(canvasContainer.getSelectedLayerIndex(), canvasContainer.getSelectedLayerIndex() - 1) == 0) canvasContainer.setSelectedLayerIndex(canvasContainer.getSelectedLayerIndex() - 1);
            }
            else if (event.getActionCommand() == "moveSelectedLayerBack") {
                if (canvasContainer.swapNearLayers(canvasContainer.getSelectedLayerIndex(), canvasContainer.getSelectedLayerIndex() + 1) == 0) canvasContainer.setSelectedLayerIndex(canvasContainer.getSelectedLayerIndex() + 1);
            }
            else if (event.getActionCommand() == "renameSelectedLayer") canvasContainer.renameLayer(canvasContainer.getSelectedLayerIndex());
            else if (event.getActionCommand() == "copySelectedLayer") canvasContainer.copyLayer(canvasContainer.getSelectedLayerIndex());
            else if (event.getActionCommand() == "deleteSelectedLayer") canvasContainer.deleteLayer(canvasContainer.getSelectedLayerIndex());
            else if (event.getActionCommand() == "deleteAllLayer") canvasContainer.deleteAllLayers();
*/
        }
    
}
