
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
    private Point pointStart;
    private Point pointEnd;
    private Point pointChange;
    private Color color;
    private int lastSelectedLayerIndex;
    private String filePath;
    
    private ArrayList<VariableObserver> VariableObserverArrayList = new ArrayList<VariableObserver>(); // Variable을 구독하는 옵저버들을 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public Variable(Controller controller) {
        this.controller = controller;
        
        functionType = FunctionType.IDLE;
        shapeType = ShapeType.RECTANGLE;
        backgroundType = BackgroundType.EMPTY;
        pointStart = new Point(0,0);
        pointEnd = new Point(0,0);
        pointChange = new Point(0,0);
        color = new Color(0, 0, 0);
        lastSelectedLayerIndex = -1;
        filePath = null;
    }
    
    /*
    ** Shape의 속성 관련 메소드
    */
    public void chooseColor() {
        JColorChooser chooser=new JColorChooser();
        color = chooser.showDialog(null,"Color",Color.YELLOW);
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
    public String getFilePath() {
        return filePath;
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
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        notifyVariableObservers();
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
