
package caupaint.model;

public class Enum {
    
    public enum FunctionType { // 프로그램의 기능 상 현재 선택된 값을 나타냄
        IDLE, DRAW, MOVE, RESIZE, ROTATE
    };
    
    public enum ShapeType { // 도형의 종류를 나타냄
        LINE, RECTANGLE, ELLIPSE, TRIANGLE, RHOMBUS
    };
    
    public enum BackgroundType { // 배경을 채우는 방법을 나타냄
        EMPTY, FILL
    };
}
