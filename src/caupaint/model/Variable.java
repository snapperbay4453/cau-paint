
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.*;
import static java.lang.Math.*;
import java.util.ArrayList;
import javax.swing.*;

public class Variable implements VariableSubject{
    
    Controller controller;
    
    private FunctionType functionType;
    private ShapeType shapeType;
    private Point pointStart;
    private Point pointEnd;
    private Point pointChange;
    private Color color;
    private int lastSelectedIndex;
    private Shape tempShape; // Layer에 추가하기 전 임시로 shape를 저장
    
    private ArrayList<VariableObserver> VariableObserverArrayList = new ArrayList<VariableObserver>(); // Variable을 구독하는 옵저버들을 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public Variable(Controller controller) {
        this.controller = controller;
        
        functionType = FunctionType.IDLE;
        shapeType = ShapeType.RECTANGLE;
        pointStart = new Point(0,0);
        pointEnd = new Point(0,0);
        pointChange = new Point(0,0);
        color = new Color(0, 0, 0);
        lastSelectedIndex = -1;
        tempShape = null;
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
    ** tempShape 관련 메소드
    */
    public void makeTempShape() {
        switch (shapeType){
            case RECTANGLE:
                tempShape = new Rectangle();
                break;
            case OVAL:
                tempShape = new Oval();
                break;
        }
        tempShape.setColor(color);
    }
    public void refreshTempShape() {
        tempShape.setPosition(new Point(min((int)pointStart.getX(), (int)pointEnd.getX()), min((int)pointStart.getY(), (int)pointEnd.getY())));
        tempShape.setSize(new Point(abs((int)pointStart.getX() - (int)pointEnd.getX()), abs((int)pointStart.getY() - (int)pointEnd.getY())));
        notifyVariableObservers();
    }
    public void finalizeTempShape() {   
        controller.addShape(tempShape);
        tempShape = null;
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
    public Point getPointStart() {
        return pointStart;
    }
    public Point getPointEnd() {
        return pointEnd;
    }
    public Color getColor() {
        return color;
    }
    public int getLastSelectedIndex() {
        return lastSelectedIndex;
    }
    public Shape getTempShape() {
        return tempShape;
    }
    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
        notifyVariableObservers();
    }
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
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
    public void setLastSelectedIndex(int index) {
        if (index != -1) lastSelectedIndex = index; // 선택 해제된 경우를 배제함
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
