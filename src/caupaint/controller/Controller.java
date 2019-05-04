
package caupaint.controller;

import caupaint.model.*;
import caupaint.view.*;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class Controller{
    
    private LayerContainer layerContainer;
    private Variable variable;
    private View view;
    
    final static String filenameExtension = ".caupaint"; // 저장되는 파일이 가지는 확장자
    
    /*
    ** 생성자
    */
    public Controller() {
        layerContainer = new LayerContainer();
        variable = new Variable(this);
        view = new View(layerContainer, variable, this);
        view.createView();
    }
    
    /*
    ** Layer, Shape 관련 메소드
    */
    public void addShapeLayer(ShapeLayer shapeLayer) {
        layerContainer.addShapeLayer(shapeLayer);
    }
    public void createShapeLayer(Point mousePosition) {
        layerContainer.createShapeLayer(mousePosition);
    }
    public void moveShapeLayer(int index, Point point) {
        layerContainer.moveShapeLayer(index, point);
    }
    public void resizeShapeLayer(int index, Point point) {
        layerContainer.resizeShapeLayer(index, point);
    }
    public void rotateShapeLayer(int index, Point point) {
        layerContainer.rotateShapeLayer(index, point);
    }
    public void swapShapeLayer(int sourceIndex, int destinationIndex) {
        if (sourceIndex <= 0 && destinationIndex <= 0) JOptionPane.showMessageDialog(null, "첫 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else if (sourceIndex >= layerContainer.getArrayList().size() - 1 && destinationIndex >= layerContainer.getArrayList().size() - 1) JOptionPane.showMessageDialog(null, "마지막 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
        else layerContainer.swapShapeLayer(sourceIndex, destinationIndex);
    }
    public void deleteShapeLayer(int index) {
        layerContainer.deleteShapeLayer(index);
    }
    public void clearLayer() {
        layerContainer.clear();
    }
    public Vector<ShapeLayer> getLayerArrayListToVector(){ // 사이드바에 Layer의 정보를 표시하기 위해 ArrayList를 Vector로 바꿔 반환하는 함수
        return layerContainer.getVector();
    }
    
    /*
    ** Variable 관련 메소드
    */
    public void chooseColor() {
        variable.chooseColor();
    }
    public String generateViewTitle(){ // 파일 주소 존재 여부에 따라 프로그램의 제목 표시줄을 다르게 설정
        if (variable.getFilePath() == null) return ("제목 없음 - CauPaint");
        else return(variable.getFilePath() + " - CauPaint");
    }
    public String getLoadedFilePath(){
        return variable.getFilePath();
    }
    public void setPointStart(Point point){
        variable.setPointStart(point);
    }
    public void setPointEnd(Point point){
        variable.setPointEnd(point);
    }
    public void setLastSelectedLayerIndex(int index) {
        variable.setLastSelectedLayerIndex(index);
    }
    public void setFilePath(String filePath){
        variable.setFilePath(filePath);
    }
    
    /*
    ** Canvas 관련 메소드
    */
    public void setCanvasSize(){
        layerContainer.showSetCanvasSizeDialogBox();
    }
    public void setCanvasBackgroundColor(){
        layerContainer.showSetCanvasBackgroundColorDialogBox();
    }
    public Point getCanvasSize(){
        return layerContainer.getCanvasSize();
    }
    public Color getCanvasBackgroundColor(){
        return layerContainer.getCanvasBackgroundColor();
    }
    
    
    public void CanvasMousePressed(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                switch(variable.getShapeType()) {
                     case LINE:
                        addShapeLayer(new LineLayer(mousePosition, (new Point((int)mousePosition.getX() + 1, (int)mousePosition.getY()+ 1)), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                    case RECTANGLE:
                        addShapeLayer(new RectangleLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case ELLIPSE:
                        addShapeLayer(new EllipseLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case TRIANGLE:
                        addShapeLayer(new TriangleLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                     case RHOMBUS:
                        addShapeLayer(new RhombusLayer(new Point((int)mousePosition.getX(), (int)mousePosition.getY()), new Point(1,1), variable.getColor(), variable.getBackgroundType(), 0));
                        break;
                }
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case MOVE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case RESIZE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            case ROTATE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            default:
                break;
        }
    }
    public void CanvasMouseReleased(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
            case MOVE:
            case RESIZE:
            case ROTATE:
                layerContainer.setRecentMousePosition(mousePosition);
                break;
            default:
                break;
        }
    }
    public void CanvasMouseDragged(Point mousePosition) {
        switch(variable.getFunctionType()) {
            case IDLE:
                break;
            case DRAW:
                createShapeLayer(mousePosition);
                break;
            case MOVE:
                moveShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            case RESIZE:
                resizeShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            case ROTATE:
                rotateShapeLayer(variable.getLastSelectedLayerIndex(), mousePosition);
                break;
            default:
                break;
        }
    }
    
    /*
    ** I/O 관련 메소드
    */
    public String getFilePathToOpen() {
        JFileChooser fileChooser = new JFileChooser();
        
        if (!layerContainer.getArrayList().isEmpty()) {
            if (JOptionPane.showConfirmDialog
                (null, "파일을 불러오기 전 캔버스의 모든 도형을 삭제해야 합니다.\n계속하시겠습니까?", "모든 도형 삭제",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return null; // 파일 불러오기를 취소한 경우
        }
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            layerContainer.getArrayList().clear(); // 불러오기 전에 현재 레이어를 모두 지움
            layerContainer.notifyLayerContainerObservers();
            return fileChooser.getSelectedFile().getPath();
        } // 대화상자를 불러온 후 파일 불러오기에 성공한 경우, 그 절대 주소를 반환함
        else return null; // 대화상자를 불러온 후 파일 불러오기에 실패한 경우
    }
    public void loadLayersFromFile(String filePath) throws IOException, ClassNotFoundException {
        if (filePath == null) return;

        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension 을 붙이지 않음
            LayerContainer loadedLayerContainer = (LayerContainer)is.readObject();
            is.close();
            
            for (int i = 0; i < loadedLayerContainer.getArrayList().size(); i++) {
                layerContainer.addShapeLayer(loadedLayerContainer.getShapeLayer(i));
            }
            layerContainer.setCanvasSize(loadedLayerContainer.getCanvasSize());
            layerContainer.setCanvasBackgroundColor(loadedLayerContainer.getCanvasBackgroundColor());
            
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
        //fileFilter = new CauPaintFileFilter(".docx", "Microsoft Word Documents");
        //FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("JPEG file", "jpg", "jpeg");

        //fileChooser.addChoosableFileFilter(fileFilter);

        if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) return fileChooser.getSelectedFile().getPath(); // 대화상자를 불러온 후 파일 저장 위치 확인에 성공한 경우, 그 절대 주소를 반환함
        else return null; // 대화상자를 불러온 후 파일 저장 위치 확인에 실패한 경우
    }
    public void saveLayersToFile(String filePath) throws IOException {
        if (filePath == null) {
            if (variable.getFilePath() == null) return; // variable
        }
        
        ObjectOutputStream os;
        try {
            if (filePath.endsWith(filenameExtension)) os = new ObjectOutputStream(new FileOutputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension을 붙이지 않음
            else os = new ObjectOutputStream(new FileOutputStream(filePath + filenameExtension)); // filePath에 확장자가 포함되어 있지 않기 때문에 filenameExtension을 붙임
            os.writeObject(layerContainer);
            JOptionPane.showMessageDialog(null, "파일을 저장하였습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            os.close();
            if (filePath.endsWith(filenameExtension)) variable.setFilePath(filePath);
            else variable.setFilePath(filePath + filenameExtension);
            view.updateLayerContainer();
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 저장에 실패하였습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
    ** 설정 관련 메소드
    */
    public void checkExit() {
        if (layerContainer.getArrayList().isEmpty() == false) { // 레이어가 하나라도 남아있을 경우
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

    private static class FileNameExtensionFilter {

        public FileNameExtensionFilter(String jpg__gif_Images, String jpg, String gif) {
        }
    }
    
}
