
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.controller.*;
import caupaint.observer.*;
import caupaint.view.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Variable implements VariableSubject{
    
    Controller controller;
    
    private FunctionType functionType;
    private ShapeType shapeType;
    private BackgroundType backgroundType;
    private Point canvasSize;
    private Color canvasBackgroundColor;
    private Point pointStart;
    private Point pointEnd;
    private Point pointChange;
    private Color color;
    private int lastSelectedLayerIndex;
    
    private ArrayList<VariableObserver> VariableObserverArrayList = new ArrayList<VariableObserver>(); // Variable을 구독하는 옵저버들을 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public Variable(Controller controller) {
        this.controller = controller;
        
        functionType = FunctionType.IDLE;
        shapeType = ShapeType.RECTANGLE;
        backgroundType = BackgroundType.EMPTY;
        canvasSize = new Point(640, 480);
        canvasBackgroundColor = Color.WHITE;
        pointStart = new Point(0,0);
        pointEnd = new Point(0,0);
        pointChange = new Point(0,0);
        color = new Color(0, 0, 0);
        lastSelectedLayerIndex = -1;
    }
    
    /*
    ** Shape의 속성 관련 메소드
    */
    public void chooseColor() {
        JColorChooser chooser=new JColorChooser();
        color = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyVariableObservers();
    }
    public void setCanvasSize() {
        int tempWidth;  int tempHeight;
        try {
            tempWidth = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 넓이를 입력하세요.", "넓이 입력", JOptionPane.QUESTION_MESSAGE));
            if (tempWidth <= 0) throw new IllegalArgumentException();
            tempHeight = Integer.parseInt(JOptionPane.showInputDialog(null, "캔버스의 높이를 입력하세요.", "높이 입력", JOptionPane.QUESTION_MESSAGE));
            if (tempHeight <= 0) throw new IllegalArgumentException();
            canvasSize = new Point(tempWidth, tempHeight);
        } catch (NumberFormatException exp){
            JOptionPane.showMessageDialog(null, "잘못된 값이 입력되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException exp){
            JOptionPane.showMessageDialog(null, "크기는 0보다 큰 수만 지정할 수 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
        notifyVariableObservers();
    }
    public void setCanvasBackgroundColor() {
        JColorChooser chooser=new JColorChooser();
        canvasBackgroundColor = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyVariableObservers();
    }
    
    /*
    ** getter, setter
    */
    public FunctionType getFunctionType() {
        return functionType;
    }
    public ShapeType getShapeType() {
        return shapeType;
    }
    public BackgroundType getBackgroundType() {
        return backgroundType;
    }
    public Point getCanvasSize() {
        return canvasSize;
    }
    public Color getCanvasBackgroundColor() {
        return canvasBackgroundColor;
    }
    public Point getPointStart() {
        return pointStart;
    }
    public Point getPointEnd() {
        return pointEnd;
    }
    public Color getColor() {
        return color;
    }
    public int getLastSelectedLayerIndex() {
        return lastSelectedLayerIndex;
    }
    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
        notifyVariableObservers();
    }
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
        notifyVariableObservers();
    }
    public void setBackgroundType(BackgroundType backgroundType) {
        this.backgroundType = backgroundType;
        notifyVariableObservers();
    }
    public void setPointStart(Point point) {
        this.pointStart = point;
        notifyVariableObservers();
    }
    public void setPointEnd(Point point) {
        this.pointEnd = point;
        notifyVariableObservers();
    }
    // setColor()는 chooseColor()로 대체
    public void setLastSelectedLayerIndex(int index) {
        if (index != -1) lastSelectedLayerIndex = index; // 선택 해제된 경우를 배제함
    }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerVariableObserver(VariableObserver o) {
	VariableObserverArrayList.add(o);
    }
    public void removeVariableObserver(VariableObserver o) {
	VariableObserverArrayList.remove(o);
    }
    public void notifyVariableObservers() {
	for (VariableObserver o : VariableObserverArrayList) {
            o.updateVariable();
	}
    }
    
}
