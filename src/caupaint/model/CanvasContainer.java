
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
    private int selectedLayerIndex;
    
    transient private String filePath;
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
        filePath = Constant.defaultFilePath;
    }
    
    
    /*
    ** Canvas 관련 메소드
    */
    public void createNewCanvas() { // 캔버스와 레이어를 모두 초기화
        deleteAllLayers();
        setFilePath(Constant.defaultFilePath);
        clearCanvas();
    }
    
    /*
    ** shapeLayerArrayList 조작 관련 메소드
    */
    public void addLayerToArrayList(ShapeLayer shapeLayer) { // 이미 생성된 레이어를 ArrayList에 추가
        shapeLayerArrayList.add(shapeLayer);
    }
    public int selectLayerByMousePoint(Point mousePoint) { // 마우스의 위치로 레이어를 선택하는 메소드
        for(int i = shapeLayerArrayList.size() - 1; i >= 0; i--) { // shapeLayerArrayList를 역순으로, 즉 가장 최근의 레이어부터 순회함
            if (shapeLayerArrayList.get(i).isOnLayer(mousePoint) == true) return i;
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
            /*
            switch(shapeLayerArrayList.get(index).getRealShapeType()) { // Shape 객체의 실제 형식에 따라 다른 복제 생성자를 호출함
                case LINE:
                    copiedShapeLayer = new LineLayer((LineLayer)shapeLayerArrayList.get(index));            break;
                case POLYLINE:
                    copiedShapeLayer = new PolylineLayer((PolylineLayer)shapeLayerArrayList.get(index));    break;
                case RECTANGLE:
                    copiedShapeLayer = new RectangleLayer((RectangleLayer)shapeLayerArrayList.get(index));  break;
                case ELLIPSE:
                    copiedShapeLayer = new EllipseLayer((EllipseLayer)shapeLayerArrayList.get(index));      break;
                case TRIANGLE:
                    copiedShapeLayer = new TriangleLayer((TriangleLayer)shapeLayerArrayList.get(index));    break;
                case RHOMBUS:
                    copiedShapeLayer = new RhombusLayer((RhombusLayer)shapeLayerArrayList.get(index));      break;
                case TEXT:
                    copiedShapeLayer = new TextLayer((TextLayer)shapeLayerArrayList.get(index));            break;  
                default: return;
            }
            */
            shapeLayerArrayList.add(index + 1, copiedShapeLayer);
            shapeLayerArrayList.get(index + 1).setName(shapeLayerArrayList.get(index).getName() + Constant.defaultCopiedFileSuffix);
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
        if ( mouseActionType.equals(MouseActionType.RELEASED) && !((tempShapeLayer.getRealShapeType().equals(ShapeType.POLYLINE)) && (((PolylineLayer)tempShapeLayer).getIsFinishedInitializing() == false)) ) {
                                                 tempShapeLayer.setBorderColor(borderColor);
                                                 tempShapeLayer.setBackgroundColor(backgroundColor);
                                                 tempShapeLayer.setStroke(stroke);
                                                 tempShapeLayer.setBackgroundType(backgroundType);
            addLayerToArrayList(ShapeLayerFactory.createClone(shapeType, tempShapeLayer));
            tempShapeLayer = null;
        }
        notifyCanvasContainerObservers();
    }
    
    /*
    public void createAndInitializeNewLayer(ShapeType shapeType, Point point, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, Font font, String imagePath) {
        switch(shapeType) {
            case LINE:
            case PEN:
            case RECTANGLE:
            case ELLIPSE:
            case TRIANGLE:
            case RHOMBUS:
            case TEXT:
                createNewLayer(shapeType, point, borderColor, backgroundColor, stroke, backgroundType, font, imagePath);
                initializeLayer(point);
                break;
            case POLYLINE:
                if ((getShapeLayerArrayList().isEmpty() == false) // 1. ShapeLayerArrayList가 비어있지 않고
                        && (getShapeLayerArrayList().get(getShapeLayerArrayList().size() - 1).getRealShapeType() == ShapeType.POLYLINE) // 2. ShapeLayerArrayList의 가장 마지막 레이어가 Polyline 타입이고
                        && (((PolylineLayer)(getShapeLayerArrayList().get(getShapeLayerArrayList().size() - 1))).getIsFinishedInitializing() == false )) { // 3. 그 레이어의 생성이 완료되지 않았다고 표시되었을 시
                    initializeLayer(point);
                }
                else {
                    createNewLayer(shapeType, point, borderColor, backgroundColor, stroke, backgroundType, font, imagePath);
                    initializeLayer(point);
                }
                break;
            case IMAGE:
                break;
        }
        notifyCanvasContainerObservers();
    }
    */
    
    public void insertTextLayer(Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, Font font) {
        String name = null;
        try {
            name = JOptionPane.showInputDialog(null, "텍스트를 입력하세요.", "새 텍스트");
            if (name == null) throw new IllegalArgumentException();
            shapeLayerArrayList.add(ShapeLayerFactory.createBuilder(ShapeType.TEXT).setName(name).setBorderColor(borderColor).setBackgroundColor(backgroundColor).setStroke(stroke).setBackgroundType(backgroundType).setFont(font).build());
        } catch (IllegalArgumentException exp){}    // 취소 버튼을 누른 경우 catch문 발동
    }
    public void insertImageLayer() {
        String imagePath = getImageFilePathToOpen();
        if (imagePath == null) return;
        shapeLayerArrayList.add(ShapeLayerFactory.createBuilder(ShapeType.IMAGE).setImagePath(imagePath).build());
        //shapeLayerArrayList.add(new ImageLayer(new Point(0, 0), new Point(0, 0), imagePath));
        notifyCanvasContainerObservers();
    }

        /*
                
        switch(shapeType) {
             case LINE:
                shapeLayerArrayList.add(new LineLayer("새 직선", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case POLYLINE:
                shapeLayerArrayList.add(new PolylineLayer("새 폴리선", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case PEN:
                shapeLayerArrayList.add(new PenLayer("새 펜", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
            case RECTANGLE:
                //shapeLayerArrayList.add(ShapeLayerFactory.create(ShapeType.RECTANGLE));
                shapeLayerArrayList.add(new RectangleLayer("새 직사각형", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case ELLIPSE:
                shapeLayerArrayList.add(new EllipseLayer("새 타원", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case TRIANGLE:
                shapeLayerArrayList.add(new TriangleLayer("새 삼각형", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case RHOMBUS:
                shapeLayerArrayList.add(new RhombusLayer("새 마름모", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true));
                break;
             case TEXT:
                shapeLayerArrayList.add(new TextLayer("새 텍스트", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true, font));
                break;
             case IMAGE:
                shapeLayerArrayList.add(new ImageLayer("새 이미지", point, new Point(0, 0), borderColor, backgroundColor, stroke, backgroundType, 0, 0x0, true, imagePath));
                break;
        }
        */

    
    /*
    public void initializeLayer(Point currentMousePosition) { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 버튼을 눌러 도형 생성 및 위치 지정
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).initialize(currentMousePosition);
        notifyCanvasContainerObservers();
    }
    public void keepInitializingLayer(Point recentlyPressedMousePosition, Point currentMousePosition) { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 버튼을 누른 채로 드래그하여 크기 변형
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).keepInitializing(recentlyPressedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
    }
    public void finishInitializingLayer() { // 레이어를 처음 생성할 때 호출되는 메서드, 마우스의 드래그를 완료하여 크기 지정 완료
        shapeLayerArrayList.get(shapeLayerArrayList.size() - 1).finishInitializing();
        notifyCanvasContainerObservers();
    }
    */
    
    /*
    ** ShapeLayer 변수 조작 관련 메소드
    */
    public void renameLayer(int index) { // 선택한 레이어의 name을 변경함
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            String tempName = JOptionPane.showInputDialog(null, "새 이름을 입력하세요.", shapeLayerArrayList.get(index).getName());
            if (tempName == "") { // 새로 입력한 이름이 비어 있으면
                JOptionPane.showMessageDialog(null, "이름을 지정해야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (tempName == null) return; // 취소 버튼을 누른 경우
            shapeLayerArrayList.get(index).setName(tempName);
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
    public void modifyLayer(int index, FunctionType functionType, MouseActionType mouseActionType, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException {
        try {
            switch(mouseActionType) {
                case PRESSED:
                    tempShapeLayer = ShapeLayerFactory.createClone(shapeLayerArrayList.get(index).getRealShapeType(), shapeLayerArrayList.get(index));
                    tempShapeLayer.setBorderColor(Color.LIGHT_GRAY);
                    break;
                case DRAGGED:
                    switch(functionType) {
                        case MOVE:      tempShapeLayer.translate(recentlyDraggedMousePosition, currentMousePosition);    break;
                        case RESIZE:    tempShapeLayer.scale(recentlyDraggedMousePosition, currentMousePosition);  break;
                        case ROTATE:    tempShapeLayer.rotate(recentlyDraggedMousePosition, currentMousePosition); break;
                        default:    break;
                    }
                    break;
                case RELEASED:
                    switch(functionType) {
                        case MOVE:
                            shapeLayerArrayList.get(index).setPosition(tempShapeLayer.getPosition());
                            break;
                        case RESIZE: 
                            shapeLayerArrayList.get(index).setPosition(tempShapeLayer.getPosition());
                            shapeLayerArrayList.get(index).setSize(tempShapeLayer.getSize());
                            break;
                        case ROTATE:
                            shapeLayerArrayList.get(index).setRadianAngle(tempShapeLayer.getRadianAngle());
                            break;
                        default:
                            break;
                    }
                    tempShapeLayer = null;
                    break;
                default:
                    break;
            }
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    /*
    public void moveLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
            shapeLayerArrayList.get(index).translate(recentlyDraggedMousePosition, currentMousePosition);
            notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void resizeLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).scale(recentlyDraggedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void rotateLayer(int index, Point recentlyDraggedMousePosition, Point currentMousePosition) throws IndexOutOfBoundsException{
        try {
            if (index == -1) throw new IndexOutOfBoundsException(); // 선택된 도형이 없을 경우 예외 호출
        shapeLayerArrayList.get(index).rotate(recentlyDraggedMousePosition, currentMousePosition);
        notifyCanvasContainerObservers();
        } catch (IndexOutOfBoundsException exp) {
            JOptionPane.showMessageDialog(null, "레이어가 선택되지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    */
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
            canvasSize = new Point(tempWidth, tempHeight);
        } catch (NumberFormatException exp){
            JOptionPane.showMessageDialog(null, "잘못된 값이 입력되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exp){
            JOptionPane.showMessageDialog(null, "크기는 0보다 큰 수만 지정할 수 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        notifyCanvasContainerObservers();
    }
    public void showSetCanvasBackgroundColorDialogBox() {
        JColorChooser chooser=new JColorChooser();
        canvasBackgroundColor = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyCanvasContainerObservers();
    }
    public void clearCanvas() {
        canvasSize = Constant.defaultCanvasSize;
        canvasBackgroundColor = Constant.defaultCanvasBackgroundColor;
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
    public String getFilePath() { return filePath; }
    public ShapeLayer getTempShapeLayer() { return tempShapeLayer; }
    
    public void setArrayList(ArrayList<ShapeLayer> shapeLayerArrayList) { this.shapeLayerArrayList = shapeLayerArrayList; notifyCanvasContainerObservers(); }
    public void setShapeLayer(int index, ShapeLayer shapeLayer) { shapeLayerArrayList.set(index, shapeLayer); notifyCanvasContainerObservers(); }
    public void setCanvasSize(Point size) { this.canvasSize = size; notifyCanvasContainerObservers(); }
    public void setCanvasBackgroundColor(Color color) { this.canvasBackgroundColor = color; notifyCanvasContainerObservers(); }
    public void setSelectedLayerIndex(int index) { selectedLayerIndex = index; notifyCanvasContainerObservers();
    }
    public void setFilePath(String filePath) { this.filePath = filePath; notifyCanvasContainerObservers(); }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.add(o); }
    public void removeCanvasContainerObserver(CanvasContainerObserver o) { CanvasContainerObserverArrayList.remove(o); }
    public void notifyCanvasContainerObservers() { for (CanvasContainerObserver o : CanvasContainerObserverArrayList) { o.updateCanvasContainer(); } }
    
}
