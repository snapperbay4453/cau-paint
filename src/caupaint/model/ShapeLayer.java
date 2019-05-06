
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.io.Serializable;

abstract public class ShapeLayer implements Serializable { // 파일로 저장해야 하므로 직렬화 구현

    private String name; // 레이어의 이름
    private Point position; // 레이어의 위치
    private Point size; // 레이어의 크기
    private Color color; // 색상
    //private BasicStroke stroke; // 외곽선 속성(직렬화가 불가하여 필요한 속성만 따로 저장함)
    private float strokeWidth; // 외곽선 속성 - 선 굵기
    private float[] strokeDash; // 외곽선 속성 - 점선 패턴
    private float strokeDashPhase; // 외곽선 속성 - 점선 간격
    private BackgroundType backgroundType; // 배경색 속성
    private double radianAngle; // 회전 각도(라디안)
    private boolean isVisible; // 화면에 표시 여부
    
    /*
    ** 생성자
    */
    public ShapeLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        this.name = name;
        this.position = position;
        this.size = size;
        this.color = color;
        this.strokeWidth = stroke.getLineWidth();
        this.strokeDash = stroke.getDashArray();
        this.strokeDashPhase = stroke.getDashPhase();
        this.backgroundType = backgroundType;
        this.radianAngle = radianAngle;
        this.isVisible = isVisible;
    }
    public ShapeLayer(Point position, Point size) { // 생성에 최소한으로 필요한 정보만 전달받음
        this.name = "새 도형";
        this.position = position;
        this.size = size;
        this.color = new Color(0, 0, 0);
        this.strokeWidth = Constant.defaultSolidLineBasicStroke.getLineWidth();
        this.strokeDash = Constant.defaultSolidLineBasicStroke.getDashArray();
        this.strokeDashPhase = Constant.defaultSolidLineBasicStroke.getDashPhase();
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
        this.isVisible = true;
    }
    public ShapeLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        this.name = "새 도형";
        this.position = new Point(0, 0);
        this.size = new Point(0, 0);
        this.color = new Color(0, 0, 0);
        this.strokeWidth = Constant.defaultSolidLineBasicStroke.getLineWidth();
        this.strokeDash = Constant.defaultSolidLineBasicStroke.getDashArray();
        this.strokeDashPhase = Constant.defaultSolidLineBasicStroke.getDashPhase();
        this.backgroundType = BackgroundType.EMPTY;
        this.radianAngle = 0;
        this.isVisible = true;
    }
    public ShapeLayer(ShapeLayer source) { // 복제 생성자
        this.name = source.getName();
        this.position = source.getPosition();
        this.size = source.getSize();
        this.color = source.getColor();
        this.strokeWidth = Constant.defaultSolidLineBasicStroke.getLineWidth();
        this.strokeDash = Constant.defaultSolidLineBasicStroke.getDashArray();
        this.strokeDashPhase = Constant.defaultSolidLineBasicStroke.getDashPhase();
        this.backgroundType = source.getBackgroundType();
        this.radianAngle = source.getRadianAngle();
        this.isVisible = source.isVisible;
    }

    /*
    ** 레이어 생성 관련 메소드
    */
    abstract public void initialize(Point currentMousePosition);
    abstract public void keepInitializing(Point recentlyPressedMousePosition, Point currentMousePosition);
    abstract public void finishInitializing();
    
    /*
    ** 레이어 변형 관련 메소드
    */
    public void translate(Point recentlyDraggedMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentlyDraggedMousePosition.getX();   double dy = currentMousePosition.getY() - recentlyDraggedMousePosition.getY();
        setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY() + dy)));
    }
    public void scale(Point recentlyDraggedMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentlyDraggedMousePosition.getX();   double dy = currentMousePosition.getY() - recentlyDraggedMousePosition.getY();
        if (isNearTopLeftCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY() + dy)));
            setSize(new Point((int)(getSize().getX() - dx), (int)(getSize().getY() - dy)));
        }
        else if (isNearTopRightCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX()), (int)(getPosition().getY() + dy)));
            setSize(new Point((int)(getSize().getX() + dx), (int)(getSize().getY() - dy)));
        }
        else if (isNearBottomLeftCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY())));
            setSize(new Point((int)(getSize().getX() - dx), (int)(getSize().getY() + dy)));
        }
        else if (isNearBottomRightCorner(currentMousePosition) == true) {
            // position은 바뀌지 않음
            setSize(new Point((int)(getSize().getX() + dx), (int)(getSize().getY() + dy)));
        }
        else return;
    }
    public void rotate(Point recentlyDraggedMousePosition, Point currentMousePosition){
        setRadianAngle(getRadianAngle()
                    + (Math.atan2
                         (currentMousePosition.getY() - (getPosition().getY() + getSize().getY() / 2), currentMousePosition.getX() - (getPosition().getX() + getSize().getX() / 2))
                     - Math.atan2
                          (recentlyDraggedMousePosition.getY() - (getPosition().getY() + getSize().getY() / 2), recentlyDraggedMousePosition.getX() - (getPosition().getX() + getSize().getX() / 2))
                   )
                );
    }
    /*
    public Point rotatePoint(Point targetPoint, Point centerPoint, double radianAngle) {
        return new Point((int)targetPoint.getX() + (int)(targetPoint.getX() - centerPoint.getX()) * (int)Math.cos(radianAngle) - (int)(targetPoint.getY() - (int)centerPoint.getX()) * (int)Math.sin(radianAngle),
                         (int)centerPoint.getY() + (int)(targetPoint.getX() - centerPoint.getX()) * (int)Math.sin(radianAngle) + (int)(targetPoint.getY() - (int)centerPoint.getY()) * (int)Math.cos(radianAngle));
    }
    */
    
    /*
    ** 레이어 인식 관련 메소드
    */
    public boolean isNearTopLeftCorner(Point mousePosition) { // 현재 마우스의 위치가 레이어의 왼쪽 위 모서리인지 판단하는 메소드
        if (mousePosition.getX() < getCentralPoint().getX() && mousePosition.getY() < getCentralPoint().getY()) return true;
        else return false;
    }
    public boolean isNearTopRightCorner(Point mousePosition) { // 현재 마우스의 위치가 레이어의 오른쪽 위 모서리인지 판단하는 메소드
        if (mousePosition.getX() >= getCentralPoint().getX() && mousePosition.getY() < getCentralPoint().getY()) return true;
        else return false;
    }
    public boolean isNearBottomLeftCorner(Point mousePosition) { // 현재 마우스의 위치가 레이어의 왼쪽 아래 모서리인지 판단하는 메소드
        if (mousePosition.getX() < getCentralPoint().getX() && mousePosition.getY() >= getCentralPoint().getY()) return true;
        else return false;
    }
    public boolean isNearBottomRightCorner(Point mousePosition) { // 현재 마우스의 위치가 레이어의 오른쪽 아래 모서리인지 판단하는 메소드
        if (mousePosition.getX() >= getCentralPoint().getX() && mousePosition.getY() >= getCentralPoint().getY()) return true;
        else return false;
    }
    
    /*
    ** 레이어 출력 관련 메소드
    */
    abstract public void draw(Graphics g);

    /*
    ** getter, setter
    */
    public String getName() { return name; }
    public Point getPosition() { return position; }
    public Point getSize() { return size; }
    //public Point getCentralPoint() { return new Point((getPosition().getX() + getSize().getX() / 2), (getPosition().getY() + getSize().getY() / 2)); } // 한 줄로 코드를 작성하면 런타임 오류 발생
    public Point getCentralPoint() {
        Point point = new Point();
        point.setLocation((getPosition().getX() + getSize().getX() / 2), (getPosition().getY() + getSize().getY() / 2));
        return point;
    }
    public Color getColor() { return color; }
    public BasicStroke getStroke() { return new BasicStroke(strokeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, strokeDash, strokeDashPhase); }
    public BackgroundType getBackgroundType() { return backgroundType; }
    public double getRadianAngle() { return radianAngle; }
    public boolean getIsVisible() { return isVisible; }
    public ShapeType getRealShapeType() { return ShapeType.SHAPE; }
    public Rectangle getBoundingBox() { return new Rectangle((int)position.getX() - 10, (int)position.getY() - 10, (int)size.getX() + 20, (int)size.getY() + 20); }
    
    public void setName(String name) { this.name = name; }
    public void setPosition(Point position) { this.position = position; }
    public void setSize(Point size) { this.size = size; }
    public void setColor(Color color) { this.color = color; }
    public void setStroke(BasicStroke stroke) {
        this.strokeWidth = stroke.getLineWidth();
        this.strokeDash = stroke.getDashArray();
        this.strokeDashPhase = stroke.getDashPhase();
    }
    public void setBackgroundType (BackgroundType backgroundType) { this.backgroundType = backgroundType;  }
    public void setRadianAngle(double radianAngle) { this.radianAngle = radianAngle; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }

}
