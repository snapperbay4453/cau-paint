
package caupaint.model;

public class Enum {
    
     // 프로그램의 기능 상 현재 선택된 값을 나타냄
    public enum FunctionType { IDLE, DRAW, MOVE, RESIZE, ROTATE };
    
     // 도형의 종류를 나타냄
    public enum ShapeType { LINE, RECTANGLE, ELLIPSE, TRIANGLE, RHOMBUS, TEXT, SHAPE };
    
     // 배경을 채우는 방법을 나타냄
    public enum BackgroundType { EMPTY, FILL };

}
