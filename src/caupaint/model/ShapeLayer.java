
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

abstract public class ShapeLayer implements Serializable { // 파일로 저장해야 하므로 직렬화 구현

    private String name; // 레이어의 이름
    private Point position; // 레이어의 위치
    private Point size; // 레이어의 크기
    private Color borderColor; // 외곽선 색상
    private Color backgroundColor; // 배경 색상
    //private BasicStroke stroke; // 외곽선 속성(직렬화가 불가하여 필요한 속성만 따로 저장함)
    private float strokeWidth; // 외곽선 속성 - 선 굵기
    private float[] strokeDash; // 외곽선 속성 - 점선 패턴
    private float strokeDashPhase; // 외곽선 속성 - 점선 간격
    private BackgroundType backgroundType; // 배경색 속성
    private double radianAngle; // 회전 각도(라디안)
    private int isFlipped; // 대칭 여부를 나타내는 플래그
    private boolean isVisible; // 화면에 표시 여부
    
    /*
    ** 생성자
    */
    public ShapeLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        this.name = name;
        this.position = position;
        this.size = size;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.strokeWidth = stroke.getLineWidth();
        this.strokeDash = stroke.getDashArray();
        this.strokeDashPhase = stroke.getDashPhase();
        this.backgroundType = backgroundType;
        this.radianAngle = radianAngle;
        this.isFlipped = isFlipped;
        this.isVisible = isVisible;
    }
    public ShapeLayer(Point position, Point size) { // 생성에 최소한으로 필요한 정보만 전달받음
        this("새 도형", position, size, Constant.defaultBorderColor, Constant.defaultBackgroundColor, Constant.defaultSolidLineBasicStroke, Constant.defaultBackgroundType, 0, 0x0, Constant.defaultIsVisible);
    }
    public ShapeLayer() { // 생성에 필요한 어떠한 정보도 전달받지 않음
        this("새 도형", new Point(0, 0), new Point(0, 0), Constant.defaultBorderColor, Constant.defaultBackgroundColor, Constant.defaultSolidLineBasicStroke, Constant.defaultBackgroundType, 0, 0x0, Constant.defaultIsVisible);
    }
    public ShapeLayer(ShapeLayer source) { // 복제 생성자
        this(source.getName(), source.getPosition(), source.getSize(), source.getBorderColor(), source.getBackgroundColor(), source.getStroke(), source.getBackgroundType(), source.getRadianAngle(), source.getIsFlipped(), source.getIsVisible());
    }

    /*
    ** Builder 메소드
    */
    abstract public static class Builder {
        String name; // 레이어의 이름
        Point position; // 레이어의 위치
        Point size; // 레이어의 크기
        Color borderColor; // 외곽선 색상
        Color backgroundColor; // 배경 색상
        //private BasicStroke stroke; // 외곽선 속성(직렬화가 불가하여 필요한 속성만 따로 저장함)
        float strokeWidth; // 외곽선 속성 - 선 굵기
        float[] strokeDash; // 외곽선 속성 - 점선 패턴
        float strokeDashPhase; // 외곽선 속성 - 점선 간격
        BackgroundType backgroundType; // 배경색 속성
        double radianAngle; // 회전 각도(라디안)
        ArrayList<Point2D.Double> vertexArrayList; // 이 도형을 구성하는 점의 위치 정보를 저장하는 ArrayList
        Font font; // 글꼴
        String imagePath; // 이미지 경로
        int isFlipped; // 대칭 여부를 나타내는 플래그
        boolean isVisible; // 화면에 표시 여부

        public Builder() {
            this.name = getDefaultName();
            this.position = new Point(0, 0);
            this.size= new Point(0, 0);
            this.borderColor = Constant.defaultBorderColor;
            this.backgroundColor = Constant.defaultBackgroundColor;
            this.strokeWidth = Constant.defaultSolidLineBasicStroke.getLineWidth();
            this.strokeDash = Constant.defaultSolidLineBasicStroke.getDashArray();
            this.strokeDashPhase = Constant.defaultSolidLineBasicStroke.getDashPhase();
            this.backgroundType = Constant.defaultBackgroundType;
            this.radianAngle = 0;
            this.vertexArrayList = new ArrayList<Point2D.Double>();
            this.font = Constant.defaultFont;
            this.imagePath = "";
            this.isFlipped = 0x0;
            this.isVisible = Constant.defaultIsVisible;
        }
        abstract public String getDefaultName();
        public Builder setName(String name) { this.name = name; return this; }
        public Builder setPosition(Point position) { this.position = position; return this; }
        public Builder setSize(Point size) { this.size = size; return this; }
        public Builder setBorderColor(Color color) { this.borderColor = color; return this; }
        public Builder setBackgroundColor(Color color) { this.backgroundColor = color; return this; }
        public Builder setStroke(BasicStroke stroke) {
            this.strokeWidth = stroke.getLineWidth();
            this.strokeDash = stroke.getDashArray();
            this.strokeDashPhase = stroke.getDashPhase();
            return this; 
        }
        public Builder setStrokeWidth(float strokeWidth) { this.strokeWidth = strokeWidth; return this; }
        public Builder setStrokeDash(float[] strokeDash) { this.strokeDash = strokeDash; return this; }
        public Builder setStrokeDashPhase(float strokeDashPhase) { this.strokeDashPhase = strokeDashPhase; return this; }
        public Builder setBackgroundType (BackgroundType backgroundType) { this.backgroundType = backgroundType; return this; }
        public Builder setRadianAngle(double radianAngle) { this.radianAngle = radianAngle; return this; }
        public Builder setFont(Font font) { this.font = font; return this; }
        public Builder setVertexArrayList(ArrayList<Point2D.Double> vertexArrayList) { this.vertexArrayList = vertexArrayList; return this; }
        public Builder setImagePath(String imagePath) { this.imagePath = imagePath; return this; }
        public Builder setIsVisible(boolean isVisible) { this.isVisible = isVisible; return this; }

        abstract public ShapeLayer build();
    }

    
    /*
    ** 레이어 생성 관련 메소드
    */
    abstract public void initialize(MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point currentMousePosition);
    
    /*
    ** 레이어 변형 관련 메소드
    */
    public void translate(Point recentlyDraggedMousePosition, Point currentMousePosition) {
        Point differential = new Point((int)currentMousePosition.getX() - (int)recentlyDraggedMousePosition.getX(), (int)currentMousePosition.getY() - (int)recentlyDraggedMousePosition.getY());
        setPosition(new Point((int)(getPosition().getX() + differential.getX()), (int)(getPosition().getY() + differential.getY())));
    }
    public void scale(Point recentlyDraggedMousePosition, Point currentMousePosition, ShapeLayerAnchorType anchorType) {
        Point differential = new Point((int)rotatePoint(currentMousePosition, getCentralPoint(), -1 * getRadianAngle()).getX() - (int)rotatePoint(recentlyDraggedMousePosition, getCentralPoint(), -1 * getRadianAngle()).getX(),
                (int)rotatePoint(currentMousePosition, getCentralPoint(), -1 * getRadianAngle()).getY() - (int)rotatePoint(recentlyDraggedMousePosition, getCentralPoint(), -1 * getRadianAngle()).getY());
        Point newPosition = new Point(0, 0);
        Point newSize = new Point(0, 0);
        switch(anchorType) {
            case TOP:
                newPosition = (new Point((int)(getPosition().getX()), (int)(getPosition().getY() + differential.getY())));
                newSize = (new Point((int)getSize().getX(), (int)(getSize().getY() - 2 * differential.getY())));
                break;
            case TOP_RIGHT:
                newPosition = (new Point((int)(getPosition().getX() - differential.getX()), (int)(getPosition().getY() + differential.getY())));
                newSize = (new Point((int)(getSize().getX() + 2 * differential.getX()), (int)(getSize().getY() - 2 * differential.getY())));
                break;
            case RIGHT:
                newPosition = (new Point((int)(getPosition().getX() - differential.getX()), (int)getPosition().getY()));
                newSize = (new Point((int)(getSize().getX() + 2 * differential.getX()), (int)getSize().getY()));
                break;
            case BOTTOM_RIGHT:
                newPosition = (new Point((int)(getPosition().getX() - differential.getX()), (int)(getPosition().getY() - differential.getY())));
                newSize = (new Point((int)(getSize().getX() + 2 * differential.getX()), (int)(getSize().getY() + 2 * differential.getY())));
                break;
            case BOTTOM:
                newPosition = (new Point((int)(getPosition().getX()), (int)(getPosition().getY() - differential.getY())));
                newSize = (new Point((int)getSize().getX(), (int)(getSize().getY() + 2 * differential.getY())));
                break;
            case BOTTOM_LEFT:
                newPosition = (new Point((int)(getPosition().getX() + differential.getX()), (int)(getPosition().getY() - differential.getY())));
                newSize = (new Point((int)(getSize().getX() - 2 * differential.getX()), (int)(getSize().getY() + 2 * differential.getY())));
                break;
            case LEFT:
                newPosition = (new Point((int)(getPosition().getX() + differential.getX()), (int)getPosition().getY()));
                newSize = (new Point((int)(getSize().getX() - 2 * differential.getX()), (int)getSize().getY()));
                break;
            case TOP_LEFT:
                newPosition = (new Point((int)(getPosition().getX() + differential.getX()), (int)(getPosition().getY() + differential.getY())));
                newSize = (new Point((int)(getSize().getX() - 2 * differential.getX()), (int)(getSize().getY() - 2 * differential.getY())));
                break;
            default:
                break;
            }
        if (newSize.getX() >= 2 * Constant.nearPointRecognitionRangeRadius && newSize.getY() >= 2 * Constant.nearPointRecognitionRangeRadius) {
            setPosition(newPosition);
            setSize(newSize);
        }
    }
    public void rotate(Point recentlyDraggedMousePosition, Point currentMousePosition){
        setRadianAngle(getRadianAngle()
                    + (Math.atan2
                        (currentMousePosition.getY() - getCentralPoint().getY(), currentMousePosition.getX() - getCentralPoint().getX())
                    - Math.atan2
                        (recentlyDraggedMousePosition.getY() - getCentralPoint().getY(), recentlyDraggedMousePosition.getX() - getCentralPoint().getX())
                   )
                );
        setRadianAngle((getRadianAngle() + (2 * Math.PI)) % (2 * Math.PI));
    }
    public void flipHorizontally(){
        if (getIsFlippedHorizontally() == true) isFlipped &= ~Constant.isFlippedHorizontallyFlag;
        else if (getIsFlippedHorizontally() == false) isFlipped |= Constant.isFlippedHorizontallyFlag;
        setRadianAngle((2 * Math.PI - getRadianAngle()) % (2 * Math.PI));
    }
    public void flipVertically(){
        if (getIsFlippedVertically() == true) isFlipped &= ~Constant.isFlippedVerticallyFlag;
        else if (getIsFlippedVertically() == false) isFlipped |= Constant.isFlippedVerticallyFlag;
        setRadianAngle(getRadianAngle() * -1);
    }
    
    /*
    ** 레이어 인식 관련 메소드
    */
    public Point rotatePoint(Point targetPoint, Point centerPoint, double radianAngle) {
        return new Point(
                            (int)Math.round((Math.cos(radianAngle) * (targetPoint.getX() - centerPoint.getX())) - (Math.sin(radianAngle) * (targetPoint.getY() - centerPoint.getY())) + centerPoint.getX()),
                            (int)Math.round((Math.sin(radianAngle) * (targetPoint.getX() - centerPoint.getX())) + (Math.cos(radianAngle) * (targetPoint.getY() - centerPoint.getY())) + centerPoint.getY())
                        );
    }
    public boolean isOnLayer(Point mousePosition) { // 현재 마우스의 위치가 레이어 내부인지 판단하는 메소드
        if (rotatePoint(mousePosition, getCentralPoint(), -1 * getRadianAngle()).getX() >= getPosition().getX() && rotatePoint(mousePosition, getCentralPoint(), -1 * getRadianAngle()).getY() >= getPosition().getY()
             && rotatePoint(mousePosition, getCentralPoint(), -1 * getRadianAngle()).getX() < getPosition().getX() + getSize().getX() && rotatePoint(mousePosition, getCentralPoint(), -1 * getRadianAngle()).getY() < getPosition().getY() + getSize().getY()
           ) return true;
        else return false;
    }
    public boolean isNearPoint(Point mousePosition, Point targetPoint) { // 현재 마우스의 위치가 targetPoint 근처인지 판단하는 메소드
        if  (   mousePosition.getX() > targetPoint.getX() - Constant.nearPointRecognitionRangeRadius
             && mousePosition.getX() < targetPoint.getX() + Constant.nearPointRecognitionRangeRadius
             && mousePosition.getY() > targetPoint.getY() - Constant.nearPointRecognitionRangeRadius
             && mousePosition.getY() < targetPoint.getY() + Constant.nearPointRecognitionRangeRadius
            ) return true;
        else return false;
    }
    public ShapeLayerAnchorType getAnchorType(Point mousePosition) { // 도형에 대한 현재 마우스의 위치가 어느 앵커인지 판단하는 메소드
        
        for (ShapeLayerAnchorType anchorType : ShapeLayerAnchorType.values()) {
            if (isNearPoint(mousePosition, rotatePoint(getAnchorPoint(anchorType), getCentralPoint(), getRadianAngle())) == true) return anchorType;
        }
        return null;
    }
    
    /*
    ** 레이어 출력 관련 메소드
    */
    abstract public void draw(Graphics g);
    public void drawBoundingBox(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
            g.setColor(Color.GRAY);
            g2d.rotate(getRadianAngle(), getCentralPoint().getX(), getCentralPoint().getY());
            g2d.setStroke(Constant.defaultLayerSelectedLineBasicStroke);
            g.drawRect((int)getBoundingBox().getX(), (int)getBoundingBox().getY(), (int)getBoundingBox().getWidth(), (int)getBoundingBox().getHeight());
            g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    public void drawAnchor(Graphics g, FunctionType functionType) {
            Graphics2D g2d = (Graphics2D)g;
            AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
            g2d.rotate(getRadianAngle(), getCentralPoint().getX(), getCentralPoint().getY());
            g2d.setStroke(Constant.anchorStroke);
            switch(functionType) {
                case MOVE:
                    drawAnchorPoint(g, ShapeLayerAnchorType.CENTER);
                    break;
                case RESIZE:
                    drawAnchorPoint(g, ShapeLayerAnchorType.TOP);
                    drawAnchorPoint(g, ShapeLayerAnchorType.TOP_RIGHT);
                    drawAnchorPoint(g, ShapeLayerAnchorType.RIGHT);
                    drawAnchorPoint(g, ShapeLayerAnchorType.BOTTOM_RIGHT);
                    drawAnchorPoint(g, ShapeLayerAnchorType.BOTTOM);
                    drawAnchorPoint(g, ShapeLayerAnchorType.BOTTOM_LEFT);
                    drawAnchorPoint(g, ShapeLayerAnchorType.LEFT);
                    drawAnchorPoint(g, ShapeLayerAnchorType.TOP_LEFT);
                    break;
                case ROTATE:
                    g.drawOval((int)getCentralPoint().getX() - (int)getSize().getY() / 2 - Constant.upperTopMargin, (int)getCentralPoint().getY() - (int)getSize().getY() / 2 - Constant.upperTopMargin, ((int)getSize().getY() / 2 + Constant.upperTopMargin) * 2, ((int)getSize().getY() / 2 + Constant.upperTopMargin) * 2);
                    drawAnchorPoint(g, ShapeLayerAnchorType.UPPER_TOP);
                    break;
                case FREE_TRANSFORM:
                    for (ShapeLayerAnchorType anchorType : ShapeLayerAnchorType.values()) {
                        g.fillOval((int)getAnchorPoint(anchorType).getX() - Constant.nearPointRecognitionRangeRadius, (int)getAnchorPoint(anchorType).getY() - Constant.nearPointRecognitionRangeRadius, Constant.nearPointRecognitionRangeRadius * 2, Constant.nearPointRecognitionRangeRadius * 2);
                    }
                    break;
            }
            g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    public void drawAnchorPoint(Graphics g, ShapeLayerAnchorType anchorType) {
        g.setColor(Color.WHITE);
        g.fillOval((int)getAnchorPoint(anchorType).getX() - Constant.nearPointRecognitionRangeRadius, (int)getAnchorPoint(anchorType).getY() - Constant.nearPointRecognitionRangeRadius, Constant.nearPointRecognitionRangeRadius * 2, Constant.nearPointRecognitionRangeRadius * 2);
        g.setColor(Color.GRAY);
        g.drawOval((int)getAnchorPoint(anchorType).getX() - Constant.nearPointRecognitionRangeRadius, (int)getAnchorPoint(anchorType).getY() - Constant.nearPointRecognitionRangeRadius, Constant.nearPointRecognitionRangeRadius * 2, Constant.nearPointRecognitionRangeRadius * 2);
    }

    /*
    ** getter, setter
    */
    public String getName() { return name; }
    public Point getPosition() { return position; }
    public Point getSize() { return size; }
    public Point getAnchorPoint(ShapeLayerAnchorType anchorType) {
        switch(anchorType) {
            case CENTER:        return new Point((int)getPosition().getX() + (int)(getSize().getX() / 2), (int)getPosition().getY() + (int)(getSize().getY() / 2));
            case TOP:           return new Point((int)getPosition().getX() + (int)(getSize().getX() / 2), (int)getPosition().getY());
            case TOP_RIGHT:     return new Point((int)getPosition().getX() + (int)getSize().getX(),       (int)getPosition().getY());
            case RIGHT:         return new Point((int)getPosition().getX() + (int)getSize().getX(),       (int)getPosition().getY() + (int)(getSize().getY() / 2));
            case BOTTOM_RIGHT:  return new Point((int)getPosition().getX() + (int)getSize().getX(),       (int)getPosition().getY() + (int)getSize().getY());
            case BOTTOM:        return new Point((int)getPosition().getX() + (int)(getSize().getX() / 2), (int)getPosition().getY() + (int)getSize().getY());
            case BOTTOM_LEFT:   return new Point((int)getPosition().getX(),                               (int)getPosition().getY() + (int)getSize().getY());
            case LEFT:          return new Point((int)getPosition().getX(),                               (int)getPosition().getY() + (int)(getSize().getY() / 2));
            case TOP_LEFT:      return new Point((int)getPosition().getX(),                               (int)getPosition().getY());
            case UPPER_TOP:     return new Point((int)getPosition().getX() + (int)(getSize().getX() / 2), (int)getPosition().getY() - Constant.upperTopMargin);
            default:            return null;
        }
    }
    //public Point getCentralPoint() { return new Point((getPosition().getX() + getSize().getX() / 2), (getPosition().getY() + getSize().getY() / 2)); } // 한 줄로 코드를 작성하면 런타임 오류 발생
    public Point getCentralPoint() {
        Point point = new Point();
        point.setLocation((getPosition().getX() + getSize().getX() / 2), (getPosition().getY() + getSize().getY() / 2));
        return point;
    }
    public Color getBorderColor() { return borderColor; }
    public Color getBackgroundColor() { return backgroundColor; }
    public BasicStroke getStroke() { return new BasicStroke(strokeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, strokeDash, strokeDashPhase); }
    public float getStrokeWidth() { return strokeWidth; }
    public float[] getStrokeDash() { return strokeDash; }
    public float getStrokeDashPhase() { return strokeDashPhase; }
    public BackgroundType getBackgroundType() { return backgroundType; }
    public double getRadianAngle() { return radianAngle; }
    public int getIsFlipped() { return isFlipped; }
    public boolean getIsFlippedHorizontally() {
        if ((isFlipped & Constant.isFlippedHorizontallyFlag) == Constant.isFlippedHorizontallyFlag) return true;
        else return false;
    }
    public boolean getIsFlippedVertically() {
        if ((isFlipped & Constant.isFlippedVerticallyFlag) == Constant.isFlippedVerticallyFlag) return true;
        else return false;
    }
    public boolean getIsVisible() { return isVisible; }
    abstract public ShapeType getRealShapeType();
    abstract public String getIconFileName();
    public Rectangle getBoundingBox() { return new Rectangle((int)position.getX(), (int)position.getY(), (int)size.getX(), (int)size.getY()); }
    abstract public ShapeLayer getWireframe();
    
    public void setName(String name) { this.name = name; }
    public void setPosition(Point position) { this.position = position; }
    public void setSize(Point size) { this.size = size; }
    public void setBorderColor(Color color) { this.borderColor = color; }
    public void setBackgroundColor(Color color) { this.backgroundColor = color; }
    public void setStroke(BasicStroke stroke) {
        this.strokeWidth = stroke.getLineWidth();
        this.strokeDash = stroke.getDashArray();
        this.strokeDashPhase = stroke.getDashPhase();
    }
    public void setStrokeWidth(float strokeWidth) { this.strokeWidth = strokeWidth; }
    public void setStrokeDash(float[] strokeDash) { this.strokeDash = strokeDash; }
    public void setStrokeDashPhase(float strokeDashPhase) { this.strokeDashPhase = strokeDashPhase; }
    public void setBackgroundType (BackgroundType backgroundType) { this.backgroundType = backgroundType;  }
    public void setRadianAngle(double radianAngle) { this.radianAngle = radianAngle; }
    public void setIsVisible(boolean isVisible) { this.isVisible = isVisible; }

}
