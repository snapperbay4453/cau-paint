
package caupaint.model;
import caupaint.model.Enum.*;
import caupaint.controller.*;
import caupaint.observer.*;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Variable implements VariableSubject{
    
    private FunctionType functionType;
    private ShapeType shapeType;
    private BackgroundType backgroundType;
    private Color borderColor;
    private Color backgroundColor;
    private BasicStroke stroke;
    private Font font;
    private Point recentlyPressedMousePosition;
    private Point recentlyDraggedMousePosition;
    private int selectedLayerIndex;
    private int lastSelectedLayerIndex;
    private String filePath;
    
    private ArrayList<VariableObserver> VariableObserverArrayList = new ArrayList<VariableObserver>(); // Variable을 구독하는 옵저버들을 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public Variable(Controller controller) {
        functionType = Constant.defaultFunctionType;
        shapeType = Constant.defaultShapeType;
        backgroundType = Constant.defaultBackgroundType;
        borderColor = Constant.defaultBorderColor;
        backgroundColor = Constant.defaultBackgroundColor;
        stroke = Constant.defaultStroke;
        font = Constant.defaultFont;
        recentlyPressedMousePosition = new Point(0, 0);
        recentlyDraggedMousePosition = new Point(0, 0);
        selectedLayerIndex = Constant.defaultSelectedLayerIndex;
        lastSelectedLayerIndex = Constant.defaultSelectedLayerIndex;
        filePath = Constant.defaultFilePath;
    }
    
    /*
    ** 프로그램 실행 중 저장하는 속성 관련 메소드
    */
    public void chooseBorderColor() { // 대화 상자를 열어 외곽선 색상 설정
        Color tempColor = new Color(0, 0, 0);
        JColorChooser chooser=new JColorChooser();
        tempColor = chooser.showDialog(null,"Color",Color.YELLOW);
        if (tempColor != null) borderColor = tempColor;
        notifyVariableObservers();
    }
    public void chooseBackgroundColor() { // 대화 상자를 열어 배경 색상 설정
        Color tempColor = new Color(0, 0, 0);
        JColorChooser chooser=new JColorChooser();
        tempColor = chooser.showDialog(null,"Color",Color.YELLOW);
        if (tempColor != null) backgroundColor = tempColor;
        notifyVariableObservers();
    }
    
    /*
    ** getter, setter
    */
    public FunctionType getFunctionType() { return functionType; }
    public ShapeType getShapeType() { return shapeType; }
    public BackgroundType getBackgroundType() {  return backgroundType; }
    public Color getBorderColor() {  return borderColor;  }
    public Color getBackgroundColor() {  return backgroundColor;  }
    public BasicStroke getStroke() { return stroke;  }
    public Font getFont() { return font; }
    public Point getRecentlyPressedMousePosition() { return recentlyPressedMousePosition;  }
    public Point getRecentlyDraggedMousePosition() { return recentlyDraggedMousePosition;  }
    public int getSelectedLayerIndex() { return selectedLayerIndex; }
    public int getLastSelectedLayerIndex() { return lastSelectedLayerIndex; }
    public String getFilePath() { return filePath; }
        
    public void setFunctionType(FunctionType functionType) { this.functionType = functionType; notifyVariableObservers(); }
    public void setShapeType(ShapeType shapeType) { this.shapeType = shapeType; notifyVariableObservers(); }
    public void setBackgroundType(BackgroundType backgroundType) { this.backgroundType = backgroundType; notifyVariableObservers(); }
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
    public void setSelectedLayerIndex(int index) {
        selectedLayerIndex = index;
        if (index != -1) lastSelectedLayerIndex = index; // 선택 해제된 경우를 배제함
        notifyVariableObservers();
    }
    public void setFilePath(String filePath) { this.filePath = filePath; notifyVariableObservers(); }
    
    /*
    ** 옵저버 관련 메소드
    */
    public void registerVariableObserver(VariableObserver o) { VariableObserverArrayList.add(o); }
    public void removeVariableObserver(VariableObserver o) { VariableObserverArrayList.remove(o);}
    public void notifyVariableObservers() { for (VariableObserver o : VariableObserverArrayList) { o.updateVariable(); } }
    
}
