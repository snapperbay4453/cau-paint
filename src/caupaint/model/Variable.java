
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.observer.*;

import java.awt.*;
import java.util.ArrayList;

public class Variable implements VariableSubject{
    
    private FunctionType functionType;
    private ShapeType shapeType;
    private BackgroundType backgroundType;
    private MouseActionType mouseActionType;
    private Color borderColor;
    private Color backgroundColor;
    private BasicStroke stroke;
    private Font font;
    private Point recentlyPressedMousePosition;
    private Point recentlyDraggedMousePosition;

    
    private ArrayList<VariableObserver> VariableObserverArrayList = new ArrayList<VariableObserver>(); // Variable을 구독하는 옵저버들을 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public Variable() {
        functionType = Constant.defaultFunctionType;
        shapeType = Constant.defaultShapeType;
        backgroundType = Constant.defaultBackgroundType;
        mouseActionType = Constant.defaultMouseActionType;
        borderColor = Constant.defaultBorderColor;
        backgroundColor = Constant.defaultBackgroundColor;
        stroke = Constant.defaultStroke;
        font = Constant.defaultFont;
        recentlyPressedMousePosition = new Point(0, 0);
        recentlyDraggedMousePosition = new Point(0, 0);

    }
    
    /*
    ** 프로그램 실행 중 저장하는 속성 관련 메소드
    */
    public void setStrokeByName(String name) { // 선 이름으로 stroke 설정
        if (name == "실선") { setStrokeDash(Constant.defaultSolidLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
        else if (name == "점선") { setStrokeDash(Constant.defaultDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
        else if (name == "파선") { setStrokeDash(Constant.defaultDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
        else if (name == "긴파선") { setStrokeDash(Constant.defaultLongDashedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
        else if (name == "1점 쇄선") { setStrokeDash(Constant.defaultDashSingleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
        else if (name == "2점 쇄선") { setStrokeDash(Constant.defaultDashDoubleDottedLineBasicStroke.getDashArray(), Constant.defaultSolidLineBasicStroke.getDashPhase()); }
    }
    
    /*
    ** getter, setter
    */
    public FunctionType getFunctionType() { return functionType; }
    public ShapeType getShapeType() { return shapeType; }
    public BackgroundType getBackgroundType() { return backgroundType; }
    public MouseActionType getMouseActionType() { return mouseActionType; }
    public Color getBorderColor() {  return borderColor;  }
    public Color getBackgroundColor() {  return backgroundColor;  }
    public BasicStroke getStroke() { return stroke;  }
    public Font getFont() { return font; }
    public Point getRecentlyPressedMousePosition() { return recentlyPressedMousePosition;  }
    public Point getRecentlyDraggedMousePosition() { return recentlyDraggedMousePosition;  }

        
    public void setFunctionType(FunctionType functionType) { this.functionType = functionType; notifyVariableObservers(); }
    public void setShapeType(ShapeType shapeType) { this.shapeType = shapeType; notifyVariableObservers(); }
    public void setBackgroundType(BackgroundType backgroundType) { this.backgroundType = backgroundType; notifyVariableObservers(); }
    public void setMouseActionType(MouseActionType mouseActionType) { this.mouseActionType = mouseActionType; notifyVariableObservers(); }
    public void setBorderColor(Color color) {  this.borderColor = color; notifyVariableObservers(); }
    public void setBackgroundColor(Color color) {  this.backgroundColor = color; notifyVariableObservers(); }
    public void setStroke(BasicStroke stroke) { this.stroke = stroke; notifyVariableObservers(); }
    public void setStrokeWidth(float width) {
        this.stroke = new BasicStroke(width, getStroke().getEndCap(), getStroke().getLineJoin(), getStroke().getMiterLimit(), getStroke().getDashArray(), getStroke().getDashPhase());
        notifyVariableObservers();
    }
    public void setStrokeDash(float[] dash, float dashPhase) {
        this.stroke = new BasicStroke(getStroke().getLineWidth(), getStroke().getEndCap(), getStroke().getLineJoin(), getStroke().getMiterLimit(), dash, dashPhase);
        notifyVariableObservers();
    }
    public void setFont(Font font) { this.font = font;  notifyVariableObservers();}
    public void setFontName (String name) {
        this.font = new Font(name, getFont().getStyle(), getFont().getSize());
        notifyVariableObservers();
    }
    public void setFontStyle (int style) {
        this.font = new Font(getFont().getName(), style, getFont().getSize());
        notifyVariableObservers();
    }
    public void setFontSize (int size) {
        this.font = new Font(getFont().getName(), getFont().getStyle(), size);
        notifyVariableObservers();
    }
    public void setRecentlyPressedMousePosition (Point point) { recentlyPressedMousePosition = point; }
    public void setRecentlyDraggedMousePosition (Point point) { recentlyDraggedMousePosition = point; }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerVariableObserver(VariableObserver o) { VariableObserverArrayList.add(o); }
    public void removeVariableObserver(VariableObserver o) { VariableObserverArrayList.remove(o);}
    public void notifyVariableObservers() { for (VariableObserver o : VariableObserverArrayList) { o.updateVariable(); } }
    
}
