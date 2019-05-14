
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.ArrayList;

public class PenLayer extends ShapeLayer{

    private ArrayList<Point2D.Double> vertexArrayList; // 이 도형을 구성하는 점의 위치 정보를 저장하는 ArrayList
    private boolean isFinishedInitializing; // 폴리선 레이어가 그리기를 마쳤으면 true, 그렇지 않으면 false
    private Point originalSize; // 생성했을 당시의 크기 정보, 외부에서 변경해서는 안되므로 setter를 만들지 않음
    
    /*
    ** 생성자
    */
    public PenLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isFlipped, isVisible);
        super.setName("새 펜");
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = false;
        originalSize = new Point((int)getSize().getX(), (int)getSize().getY());
    }
    public PenLayer() {
        super();
        super.setName("새 펜");
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = false;
        originalSize = new Point((int)getSize().getX(), (int)getSize().getY());
    }
    public PenLayer(PenLayer source) { // 복제 생성자
        super(source);
        vertexArrayList = new ArrayList<Point2D.Double>();
        for(int i = 0; i < source.vertexArrayList.size(); i++) {
            vertexArrayList.add(new Point2D.Double(source.vertexArrayList.get(i).getX(), source.vertexArrayList.get(i).getY()));
        }
        isFinishedInitializing = source.getIsFinishedInitializing();
        originalSize = source.getOriginalSize();
    }
    
    /*
    ** Builder 메소드
    */
    public static class Builder extends ShapeLayer.Builder { 
        public PenLayer build() {
            BasicStroke tempStroke = new BasicStroke(strokeWidth, Constant.defaultSolidLineBasicStroke.getEndCap(), Constant.defaultSolidLineBasicStroke.getLineJoin(), Constant.defaultSolidLineBasicStroke.getMiterLimit(), strokeDash, strokeDashPhase);
            return new PenLayer(name, position, size, borderColor, backgroundColor, tempStroke, backgroundType, radianAngle, isFlipped, isVisible);
        }
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point currentMousePosition) {
        switch(mouseActionType) {
            case PRESSED:
                isFinishedInitializing = false;
                vertexArrayList.add(new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY()));
                break;
            case DRAGGED:
                vertexArrayList.add(new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY()));
                break;
            case RELEASED:
                double minX = vertexArrayList.get(0).getX();   double minY = vertexArrayList.get(0).getY();
                double maxX = vertexArrayList.get(0).getX();   double maxY = vertexArrayList.get(0).getY();
                for (Point2D.Double point : vertexArrayList) { // 각 점들의 최소 위치값과 최대 위치값을 구함
                    minX = min(point.getX(), minX);
                    minY = min(point.getY(), minY);
                    maxX = max(point.getX(), maxX);
                    maxY = max(point.getY(), maxY);     
                }

                setPosition(new Point((int)minX, (int)minY)); // 이 레이어의 위치를 알아내 저장함
                setSize(new Point((int)(maxX - minX), (int)(maxY - minY))); // 이 레이어의 크기를 알아내 저장함
                originalSize = (new Point((int)(maxX - minX), (int)(maxY - minY))); // 이 레이어의 크기를 알아내 저장함

                for (int i = 0; i < vertexArrayList.size(); i++) { // vertex의 값을 절대 위치에서 position을 원점으로 하는 상대 위치로 변환
                    vertexArrayList.set(i, new Point2D.Double(vertexArrayList.get(i).getX() - getPosition().getX(), vertexArrayList.get(i).getY() - getPosition().getY()));
                }

                isFinishedInitializing = true; // 그리기 완료
                break;
            default: break;
        }

    }
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        
        // drawPen 메소드에서 Pen을 그리기 위해 필요한 배열을 생성함
        int[] xPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            if (getIsFinishedInitializing() == true) xPoints[i] = (int)((vertexArrayList.get(i).getX()) * (getSize().getX() / originalSize.getX()) + getPosition().getX());
            else xPoints[i] = (int)(vertexArrayList.get(i).getX());
        }
        int[] yPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            if (getIsFinishedInitializing() == true) yPoints[i] = (int)((vertexArrayList.get(i).getY()) * (getSize().getY() / originalSize.getY()) + getPosition().getY());
            else yPoints[i] = (int)(vertexArrayList.get(i).getY());
        }

        if (getBackgroundType() == BackgroundType.FILL) {
            g.setColor(getBackgroundColor());
            g.fillPolygon(xPoints, yPoints, vertexArrayList.size());
        }
        g.setColor(getBorderColor());
        g.drawPolyline(xPoints, yPoints, vertexArrayList.size());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.PEN; }
    public boolean getIsFinishedInitializing() { // 이 도형의 생성이 완료되었는지 반환
        return isFinishedInitializing;
    }
    public Point getOriginalSize() {
        return originalSize;
    }

}
