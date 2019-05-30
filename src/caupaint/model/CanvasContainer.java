
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.observer.*;
import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class CanvasContainer implements Serializable, CanvasContainerSubject{
    
    private ArrayList<ShapeLayer> shapeLayerArrayList; // Shape를 저장하는 ArrayList
    private Point canvasSize; // 캔버스의 너비와 높이를 저장
    private Color canvasBackgroundColor; // 캔버스의 배경색을 저장
    private int selectedLayerIndex; // 현재 선택된 레이어의 인덱스
    transient private ShapeLayerAnchorType shapeLayerAnchorType; // 현재 선택된 레이어의 앵커 타입
    
    transient private String filePath; // 불러온 파일의 절대 위치
    transient private ShapeLayer tempShapeLayer; // 레이어를 생성 또는 변형 시 임시로 정보를 저장하는 ShapeLayer
    transient private ArrayList<CanvasContainerObserver> CanvasContainerObserverArrayList = new ArrayList<CanvasContainerObserver>(); // CanvasContainer를 구독하는 옵저버들을 저장하는 ArrayList

    /*
    ** 생성자
    */
    public CanvasContainer() {
        shapeLayerArrayList = new ArrayList<ShapeLayer>(); // 레이어들의 정보를 담는 ArrayList
        canvasSize = Constant.defaultCanvasSize; // 캔버스의 크기
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor; // 캔버스의 배경색
        selectedLayerIndex = Constant.defaultSelectedLayerIndex;
        shapeLayerAnchorType = Constant.defaultShapeLayerAnchorType;
        filePath = Constant.defaultFilePath;
    }
    
    
    /*
    ** Canvas 관련 메소드
    */
    public void createNewCanvas() { // 캔버스와 레이어를 모두 초기화
        if (JOptionPane.showConfirmDialog(null, "캔버스를 초기화합니다.", "캔버스 초기화", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            shapeLayerArrayList.clear();
            setSelectedLayerIndex(-1);
            notifyCanvasContainerObservers();
            setFilePath(Constant.defaultFilePath);
            canvasSize = Constant.defaultCanvasSize;
            canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
            notifyCanvasContainerObservers();
        }
    }
    
    /*
    ** shapeLayerArrayList 조작 관련 메소드
    */
    public void addLayerToArrayList(ShapeLayer shapeLayer) { // 이미 생성된 레이어를 ArrayList에 추가
        shapeLayerArrayList.add(shapeLayer);
    }
    public int selectLayerByMousePoint(Point mousePoint) { // 마우스의 위치로 레이어를 선택하는 메소드
        for(int i = shapeLayerArrayList.size() - 1; i >= 0; i--) { // shapeLayerArrayList를 역순으로, 즉 가장 최근의 레이어부터 순회함
            if (shapeLayerArrayList.get(i).isOnLayer(mousePoint) == true) {
                setSelectedLayerIndex(i);
                return i;
            }
        }
        // 어떤 레이어도 선택되지 않았을 경우
        setSelectedLayerIndex(-1);
        return -1;
    }
    public void copyLayer(int index) throws IndexOutOfBoundsException{ // 선택한 레이어를 복제함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer copiedShapeLayer;
            copiedShapeLayer = ShapeLayerFactory.createClone(shapeLayerArrayList.get(index).getRealShapeType(), shapeLayerArrayList.get(index));
            copiedShapeLayer.setPosition(new Point((int)copiedShapeLayer.getPosition().getX() + 10, (int)copiedShapeLayer.getPosition().getY() + 10));// 위치를 일부로 약간 어긋나게 설정함
            shapeLayerArrayList.add(index + 1, copiedShapeLayer);
            shapeLayerArrayList.get(index + 1).setName(shapeLayerArrayList.get(index).getName() + Constant.defaultCopiedFileSuffix);
            setSelectedLayerIndex(getSelectedLayerIndex() + 1); // 복제된 레이어를 선택함
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public int swapLayers(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 두 레이어의 index를 서로 바꿈
        try {
            if (sourceIndex == -1 || destinationIndex == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            ShapeLayer tempShapeLayer = shapeLayerArrayList.get(sourceIndex); // sourceIndex의 레이어 정보를 임시로 저장
            shapeLayerArrayList.set(sourceIndex, shapeLayerArrayList.get(destinationIndex)); // sourceIndex에 destinationIndex의 레이어 정보를 저장
            shapeLayerArrayList.set(destinationIndex, tempShapeLayer); // destinationIndex에 임시로 저장했던 sourceIndex의 레이어 정보를 저장
            notifyCanvasContainerObservers();
            return 0;
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }
    public int swapNearLayers(int sourceIndex, int destinationIndex) throws IndexOutOfBoundsException{ // 인접한 두 레이어의 index를 서로 바꿈
        if (sourceIndex == -1) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        else if (sourceIndex <= 0 && destinationIndex <= 0) {
            JOptionPane.showMessageDialog(null, "첫 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        else if (sourceIndex >= getShapeLayerArrayList().size() - 1 && destinationIndex >= getShapeLayerArrayList().size() - 1) {
            JOptionPane.showMessageDialog(null, "마지막 레이어입니다.", "레이어 이동 불가", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        else return swapLayers(sourceIndex, destinationIndex);
    }
    public void deleteLayer(int index) throws ArrayIndexOutOfBoundsException { // 선택한 레이어를 삭제함
        try {
            if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
                JOptionPane.showMessageDialog(null, "삭제할 레이어가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (index == -1) throw new ArrayIndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            if (JOptionPane.showConfirmDialog(null, "현재 레이어를 삭제합니다.", "도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                shapeLayerArrayList.remove(index);
                if (shapeLayerArrayList.size() == 0) setSelectedLayerIndex(-1);
                else setSelectedLayerIndex(index - 1);
                notifyCanvasContainerObservers();
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteAllLayers() { // 모든 레이어를 삭제함
        if (shapeLayerArrayList.isEmpty()){ // 삭제할 도형이 없을 경우 예외 호출
            JOptionPane.showMessageDialog(null, "삭제할 레이어가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(null, "모든 레이어를 삭제합니다.", "모든 도형 삭제", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            shapeLayerArrayList.clear();
            setSelectedLayerIndex(-1);
            notifyCanvasContainerObservers();
        }
    }

    
    /*
    ** ShapeLayer 생성 관련 메소드
    */
    public void createNewLayer(ShapeType shapeType, MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point currentMousePosition, 
            Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType) { // 레이어 생성에 앞서 tempShapeLayer를 만들고, 초기화가 완료되면 이 객체를 shapeLayerArrayList의 마지막에 추가함
        if ((tempShapeLayer == null) && mouseActionType.equals(MouseActionType.PRESSED)){
            tempShapeLayer = ShapeLayerFactory.createBuilder(shapeType).setPosition(currentMousePosition).setBorderColor(Color.LIGHT_GRAY).build();
        }
        tempShapeLayer.initialize(mouseActionType, recentlyPressedMousePosition, currentMousePosition);
        
        // 마우스가 Released 상태여야 하지만, 초기화중인 Layer가 Polyline이면서 그리기가 끝나지 않은 상태여서는 안됨
        if ( mouseActionType.equals(MouseActionType.RELEASED) && !((tempShapeLayer instanceof PolylineLayer ) && (((PolylineLayer)tempShapeLayer).getIsFinishedInitializing() == false)) ) {
            tempShapeLayer.setBorderColor(borderColor);
            tempShapeLayer.setBackgroundColor(backgroundColor);
            tempShapeLayer.setStroke(stroke);
            tempShapeLayer.setBackgroundType(backgroundType);
            addLayerToArrayList(tempShapeLayer);
            tempShapeLayer = null;
            setSelectedLayerIndex(shapeLayerArrayList.size() - 1);
        }
        notifyCanvasContainerObservers();
    }
    public void insertTextLayer(Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, Font font) {
        String name = null;
        try {
            name = JOptionPane.showInputDialog(null, "텍스트를 입력하세요.", "새 텍스트");
            if (name == null) throw new IllegalArgumentException();
            shapeLayerArrayList.add(ShapeLayerFactory.createBuilder(ShapeType.TEXT).setName(name).setBorderColor(borderColor).setBackgroundColor(backgroundColor).setStroke(stroke).setBackgroundType(backgroundType).setFont(font).build());
            setSelectedLayerIndex(shapeLayerArrayList.size() - 1);
        } catch (IllegalArgumentException Creexp){}    // 취소 버튼을 누른 경우 catch문 발동
    }
    public void insertImageLayer(Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType) {
        String imagePath = getImageFilePathToOpen();
        if (imagePath == null) return;
        shapeLayerArrayList.add(ShapeLayerFactory.createBuilder(ShapeType.IMAGE).setImagePath(imagePath).setBackgroundColor(backgroundColor).setStroke(stroke).setBackgroundType(backgroundType).build());
        setSelectedLayerIndex(shapeLayerArrayList.size() - 1);
        notifyCanvasContainerObservers();
    }
    
    /*
    ** ShapeLayer 변수 조작 관련 메소드
    */
    public void renameLayer(int index, String name) { // 선택한 레이어의 name을 변경함
        try {
            shapeLayerArrayList.get(index).setName(name);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void changeLayerBackgroundTypeAndColors(int index, BackgroundType backgroundType, Color borderColor, Color backgroundColor) {
       getShapeLayerArrayList().get(index).setBackgroundType(backgroundType); // 도형의 배경 채우기 옵션 설정
       getShapeLayerArrayList().get(index).setBorderColor(borderColor); // 도형의 borderColor 변경
       getShapeLayerArrayList().get(index).setBackgroundColor(backgroundColor); // 도형의 backgroundColor 변경
    }
    public void setLayerStroke (int index, BasicStroke stroke) { // 선택한 레이어의 stroke를 변경함
        shapeLayerArrayList.get(index).setStroke(stroke);
        notifyCanvasContainerObservers();
    }
    public void setLayerFont (int index, Font font) { // 선택한 레이어의 font를 변경함
        ((TextLayer)shapeLayerArrayList.get(index)).setFont(font);
        notifyCanvasContainerObservers();
    }
    public void toggleLayerIsVisible(int index) throws IndexOutOfBoundsException{ // 선택한 레이어의 isVisible을 변경함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).setIsVisible( ! shapeLayerArrayList.get(index).getIsVisible()); // isVisible 토글
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*
    ** ShapeLayer 도형 변형 관련 메소드
    */
    public void moveLayer(int index, MouseActionType mouseActionType, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException {
        if (index == -1) {
            selectLayerByMousePoint(currentMousePosition);
            return;
        } 
        try {
            switch(mouseActionType) {
                case PRESSED:
                    tempShapeLayer = shapeLayerArrayList.get(index).getWireframe();
                    break;
                case DRAGGED:
                    tempShapeLayer.translate(recentlyDraggedMousePosition, currentMousePosition);
                    break;
                case RELEASED:
                    shapeLayerArrayList.get(index).setPosition(tempShapeLayer.getPosition());
                    tempShapeLayer = null;
                    break;
                default:
                    break;
            }
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resizeLayer(int index, MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException {
        if (index == -1) {
            selectLayerByMousePoint(currentMousePosition);
            return;
        } 
        try {
            switch(mouseActionType) {
                case PRESSED:
                    tempShapeLayer = shapeLayerArrayList.get(index).getWireframe();
                    setShapeLayerAnchorType(tempShapeLayer.getAnchorType(recentlyDraggedMousePosition));
                    break;
                case DRAGGED:
                    tempShapeLayer.scale(recentlyDraggedMousePosition, currentMousePosition, getShapeLayerAnchorType());
                        //tempShapeLayer.scale(shapeLayerArrayList.get(index).getPosition(), shapeLayerArrayList.get(index).getSize(), recentlyPressedMousePosition, currentMousePosition, getShapeLayerAnchorType());
                    break;
                case RELEASED:
                    shapeLayerArrayList.get(index).setPosition(tempShapeLayer.getPosition());
                    shapeLayerArrayList.get(index).setSize(tempShapeLayer.getSize());
                    tempShapeLayer = null;
                    break;
                default:
                    break;
            }
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void rotateLayer(int index, MouseActionType mouseActionType, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException {
        if (index == -1) {
            selectLayerByMousePoint(currentMousePosition);
            return;
        } 
        try {
            switch(mouseActionType) {
                case PRESSED:
                    tempShapeLayer = shapeLayerArrayList.get(index).getWireframe();
                    break;
                case DRAGGED:
                        tempShapeLayer.rotate(recentlyDraggedMousePosition, currentMousePosition);
                    break;
                case RELEASED:
                    shapeLayerArrayList.get(index).setRadianAngle(tempShapeLayer.getRadianAngle());
                    tempShapeLayer = null;
                    break;
                default:
                    break;
            }
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException exp) {
            //JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void flipLayerHorizontally(int index) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).flipHorizontally();
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void flipLayerVertically(int index) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).flipVertically();
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void freeTransformLayer(int index, MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException {
        if (mouseActionType == MouseActionType.PRESSED) tempShapeLayer = shapeLayerArrayList.get(index).getWireframe();
        switch(tempShapeLayer.getAnchorType(recentlyDraggedMousePosition)) {
            case CENTER:        moveLayer(index, mouseActionType, recentlyDraggedMousePosition, currentMousePosition);   break;
            case TOP:
            case TOP_RIGHT:
            case RIGHT:
            case BOTTOM_RIGHT:
            case BOTTOM:
            case BOTTOM_LEFT:
            case LEFT:
            case TOP_LEFT:      resizeLayer(index, mouseActionType, recentlyPressedMousePosition, recentlyDraggedMousePosition, currentMousePosition);    break;
            case UPPER_TOP:     rotateLayer(index, mouseActionType, recentlyDraggedMousePosition, currentMousePosition);   break;
            default:    break;
        }
    }

    /*
    ** Canvas 관련 메소드
    */
    public void showSetCanvasSizeDialogBox() {
        int tempWidth;  int tempHeight;
        try {
            tempWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 넓이를 입력하세요.",  (int)canvasSize.getX()));
            if (tempWidth <= 0) throw new IllegalArgumentException();
            tempHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 높이를 입력하세요.", (int)canvasSize.getY()));
            if (tempHeight <= 0) throw new IllegalArgumentException();
            setCanvasSize(new Point(tempWidth, tempHeight));
        } catch (NumberFormatException exp){
            JOptionPane.showMessageDialog(null, "잘못된 값이 입력되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exp){
            JOptionPane.showMessageDialog(null, "크기는 0보다 큰 수만 지정할 수 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        notifyCanvasContainerObservers();
    }
    public void showSetCanvasBackgroundColorDialogBox() {
        JColorChooser chooser=new JColorChooser();
        setCanvasBackgroundColor(chooser.showDialog(null,"Color",Color.YELLOW));
        notifyCanvasContainerObservers();
    }

    
    
    /*
    ** I/O 관련 메소드
    */
    public String getFilePathToOpen() {
        JFileChooser fileChooser = new JFileChooser();
        
        if (!getShapeLayerArrayList().isEmpty()) {
            if (JOptionPane.showConfirmDialog
                (null, "파일을 불러오기 전 캔버스의 모든 도형을 삭제해야 합니다.\n계속하시겠습니까?", "모든 도형 삭제",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) return null; // 파일 불러오기를 취소한 경우
        }
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            getShapeLayerArrayList().clear(); // 불러오기 전에 현재 레이어를 모두 지움
            notifyCanvasContainerObservers();
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
                addLayerToArrayList(loadedCanvasContainer.getShapeLayerArrayList().get(i));
            }
            setCanvasSize(loadedCanvasContainer.getCanvasSize());
            setCanvasBackgroundColor(loadedCanvasContainer.getCanvasBackgroundColor());
            
            setFilePath(filePath);
            notifyCanvasContainerObservers();
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 불러오기에 실패하였습니다.", "불러오기 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
    public String getFilePathToSave() { // 파일을 저장할 경로를 반환하는 메서드
        if (getFilePath() != null) return getFilePath(); // variable에 이미 기존의 파일 경로가 저장되어 있을 경우, 기존의 경로를 반환함
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
            if (getFilePath() == null) return; // 매개변수와 variable 모두 파일 경로가 지정되어 있지 않은 경우
        }
        ObjectOutputStream os;
        try {
            if (filePath.endsWith(Constant.defaultFilenameExtension)) os = new ObjectOutputStream(new FileOutputStream(filePath)); // 확장자가 이미 filePath에 포함되어 있기 때문에 filenameExtension을 붙이지 않음
            else os = new ObjectOutputStream(new FileOutputStream(filePath + Constant.defaultFilenameExtension)); // filePath에 확장자가 포함되어 있지 않기 때문에 filenameExtension을 붙임
            os.writeObject(this);
            JOptionPane.showMessageDialog(null, "파일을 저장하였습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            os.close();
            if (filePath.endsWith(Constant.defaultFilenameExtension)) setFilePath(filePath);
            else setFilePath(filePath + Constant.defaultFilenameExtension);
        } catch (IOException exp) {
            JOptionPane.showMessageDialog(null, "파일 저장에 실패하였습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
            exp.printStackTrace();
        }
    }
    public String generateMainViewWindowTitle(){ // 파일 주소 존재 여부에 따라 프로그램의 제목 표시줄 내용을 결정
        if (getFilePath() == null) return ("제목 없음 - CauPaint");
        else return(getFilePath() + " - CauPaint");
    }
    
    
    /*
    ** getter, setter
    */
    public ArrayList<ShapeLayer> getShapeLayerArrayList() { return this.shapeLayerArrayList; }
    public Vector<ShapeLayer> getShapeLayerArrayListToVector() {  // ArrayList를 Vector 형식으로 반환하는 메소드
        Vector<ShapeLayer> layerVector = new Vector<ShapeLayer>();
        for (ShapeLayer shapeLayer : shapeLayerArrayList) {
            layerVector.add(shapeLayer);
        }
        return layerVector;
    }
    public Point getCanvasSize() { return canvasSize; }
    public Color getCanvasBackgroundColor() { return canvasBackgroundColor; }
    public int getSelectedLayerIndex() { return selectedLayerIndex; }
    public ShapeLayerAnchorType getShapeLayerAnchorType() { return shapeLayerAnchorType; }
    public String getFilePath() { return filePath; }
    public ShapeLayer getTempShapeLayer() { return tempShapeLayer; }
    
    public void setArrayList(ArrayList<ShapeLayer> shapeLayerArrayList) { this.shapeLayerArrayList = shapeLayerArrayList; notifyCanvasContainerObservers(); }
    public void setShapeLayer(int index, ShapeLayer shapeLayer) { shapeLayerArrayList.set(index, shapeLayer); notifyCanvasContainerObservers(); }
    public void setCanvasSize(Point size) { this.canvasSize = size; notifyCanvasContainerObservers(); }
    public void setCanvasBackgroundColor(Color color) { this.canvasBackgroundColor = color; notifyCanvasContainerObservers(); }
    public void setSelectedLayerIndex(int index) { selectedLayerIndex = index; notifyCanvasContainerObservers(); }
    public void setShapeLayerAnchorType(ShapeLayerAnchorType shapeLayerAnchorType) { this.shapeLayerAnchorType = shapeLayerAnchorType; notifyCanvasContainerObservers(); }
    public void setFilePath(String filePath) { this.filePath = filePath; notifyCanvasContainerObservers(); }
    
    /*
    ** 옵저버 관련 메소드 - 사용하지 않음
    */
    public void registerCanvasContainerObserver(CanvasContainerObserver o) { /*CanvasContainerObserverArrayList.add(o);*/ }
    public void removeCanvasContainerObserver(CanvasContainerObserver o) { /*CanvasContainerObserverArrayList.remove(o);*/ }
    public void notifyCanvasContainerObservers() { /*for (CanvasContainerObserver o : CanvasContainerObserverArrayList) { o.updateCanvasContainer(); }*/ }
    
}
