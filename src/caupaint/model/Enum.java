
package caupaint.model;

public class Enum {
    
     // 프로그램의 기능 상 현재 선택된 값을 나타냄
    public enum FunctionType { SELECT, DRAW, MOVE, RESIZE, ROTATE, FREE_TRANSFORM };
    
     // 도형의 종류를 나타냄
    public enum ShapeType { POLYLINE, PEN, RECTANGLE, ELLIPSE, TRIANGLE, RHOMBUS, TEXT, IMAGE };
    
     // 배경을 채우는 방법을 나타냄
    public enum BackgroundType { EMPTY, FILL };

    // 마우스의 상태를 나타냄
    public enum MouseActionType { CLICKED, ENTERED, EXITED, PRESSED, RELEASED, DRAGGED, MOVED };
    
    // 도형의 변형에 사용되는 점들의 위치를 나타냄
    public enum ShapeLayerAnchorType { CENTER, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, TOP_LEFT, UPPER_TOP };
    
    
}
