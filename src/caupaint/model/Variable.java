
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
    private Color color;
    private BasicStroke stroke;
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
        color = Constant.defaultColor;
        stroke = Constant.defaultStroke;
        selectedLayerIndex = Constant.defaultSelectedLayerIndex;
        lastSelectedLayerIndex = Constant.defaultSelectedLayerIndex;
        filePath = Constant.defaultFilePath;
    }
    
    /*
    ** 프로그램 실행 중 저장하는 속성 관련 메소드
    */
    public void chooseColor() { // 대화 상자를 열어 색상 설정
        JColorChooser chooser=new JColorChooser();
        color = chooser.showDialog(null,"Color",Color.YELLOW);
        notifyVariableObservers();
    }
    public String generateMainViewWindowTitle(){ // 파일 주소 존재 여부에 따라 프로그램의 제목 표시줄 내용을 결정
        if (getFilePath() == null) return ("제목 없음 - CauPaint");
        else return(getFilePath() + " - CauPaint");
    }
    /*
    public BasicStroke getStrokeWithNewWidth(BasicStroke stroke, float width) { // 입력된 BasicStroke의 선 넓이만 변경해서 반환하는 메소드
        return new BasicStroke(width, stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase());
    }
    */
    
    /*
    ** getter, setter
    */
    public FunctionType getFunctionType() { return functionType; }
    public ShapeType getShapeType() { return shapeType; }
    public BackgroundType getBackgroundType() {  return backgroundType; }
    public Color getColor() {  return color;  }
    public BasicStroke getStroke() { return stroke;  }
    public int getSelectedLayerIndex() { return selectedLayerIndex; }
    public int getLastSelectedLayerIndex() { return lastSelectedLayerIndex; }
    public String getFilePath() { return filePath; }
        
    public void setFunctionType(FunctionType functionType) { this.functionType = functionType; notifyVariableObservers(); }
    public void setShapeType(ShapeType shapeType) { this.shapeType = shapeType; notifyVariableObservers(); }
    public void setBackgroundType(BackgroundType backgroundType) { this.backgroundType = backgroundType; notifyVariableObservers(); }
    public void setColor(Color color) {  this.color = color; notifyVariableObservers(); }
    public void setStroke(BasicStroke stroke) { this.stroke = stroke; notifyVariableObservers(); }
    public void setStrokeWithNewWidth(BasicStroke stroke, float width) {
        this.stroke = new BasicStroke(width, stroke.getEndCap(), stroke.getLineJoin(), stroke.getMiterLimit(), stroke.getDashArray(), stroke.getDashPhase());;
        notifyVariableObservers();
    }
    public void setStrokeWidth(float width) {
        this.stroke = new BasicStroke(width, getStroke().getEndCap(), getStroke().getLineJoin(), getStroke().getMiterLimit(), getStroke().getDashArray(), getStroke().getDashPhase());
        notifyVariableObservers();
    }
    public void setStrokeDash(float[] dash, float dashPhase) {
        this.stroke = new BasicStroke(getStroke().getLineWidth(), getStroke().getEndCap(), getStroke().getLineJoin(), getStroke().getMiterLimit(), dash, dashPhase);
        notifyVariableObservers();
    }
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
