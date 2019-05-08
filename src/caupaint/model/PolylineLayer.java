
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.ArrayList;

public class PolylineLayer extends ShapeLayer{

    private ArrayList<Point2D.Double> vertexArrayList; // 이 도형을 구성하는 점의 위치 정보를 저장하는 ArrayList
    private boolean isFinishedInitializing; // 폴리선 레이어가 그리기를 마쳤으면 true, 그렇지 않으면 false
    
    /*
    ** 생성자
    */
    public PolylineLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, color, stroke, backgroundType, radianAngle, isVisible);
        super.setName("새 폴리선");
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = true;
    }
    public PolylineLayer(Point position, Point size) {
        super(position, size);
        super.setName("새 폴리선");
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = true;
    }
    public PolylineLayer() {
        super();
        super.setName("새 폴리선");
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = true;
    }
    public PolylineLayer(PolylineLayer source) { // 복제 생성자
        super(source);
        vertexArrayList = new ArrayList<Point2D.Double>();
        for(int i = 0; i < source.vertexArrayList.size(); i++) {
            vertexArrayList.add(new Point2D.Double(source.vertexArrayList.get(i).getX(), source.vertexArrayList.get(i).getY()));
        }
        isFinishedInitializing = source.getIsFinishedInitializing();
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(Point currentMousePosition) {
        isFinishedInitializing = false; // 그리는 중이라고 표시
        vertexArrayList.add(new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY()));
        //if (vertexArrayList.size() <= 1)  vertexArrayList.add(new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY())); // Vertex가 1개만 저장되어 있을 경우 직선 생성을 위해 Vertex 1개를 더 생성함
    }
    @Override
    public void keepInitializing(Point recentlyPressedMousePosition, Point currentMousePosition) {
        vertexArrayList.set(vertexArrayList.size() - 1, new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY())); // 가장 최근에 생성한 Vertex의 위치 정보를 현재 마우스의 위치로 변경함
    }
    @Override
    public void finishInitializing() {
        if((vertexArrayList.size() > 1 // 최소 2개 이상의 Vertex가 있어야 함
                && vertexArrayList.get(vertexArrayList.size() - 1).getX() > vertexArrayList.get(vertexArrayList.size() - 2).getX() - Constant.nearPointRecognitionRangeRadius)
                && (vertexArrayList.get(vertexArrayList.size() - 1).getX() < vertexArrayList.get(vertexArrayList.size() - 2).getX() + Constant.nearPointRecognitionRangeRadius)
                && (vertexArrayList.get(vertexArrayList.size() - 1).getY() > vertexArrayList.get(vertexArrayList.size() - 2).getY() - Constant.nearPointRecognitionRangeRadius)
                && (vertexArrayList.get(vertexArrayList.size() - 1).getY() < vertexArrayList.get(vertexArrayList.size() - 2).getY() + Constant.nearPointRecognitionRangeRadius)) { // 마지막 Vertex의 위치가 그 전 Vertex의 근처일 경우
            setPosition(new Point((int)getBoundingBox().getX(), (int)getBoundingBox().getY())); // 외곽 상자로부터 이 레이어의 위치를 알아내 저장함
            setSize(new Point((int)getBoundingBox().getWidth(), (int)getBoundingBox().getHeight())); // 외곽 상자로부터 이 레이어의 크기를 알아내 저장함
            isFinishedInitializing = true; // 그리기 완료
        }
    }
    
    /*
    ** 레이어 변형 관련 메소드
    */
    @Override
    public void translate(Point recentMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentMousePosition.getX();   double dy = currentMousePosition.getY() - recentMousePosition.getY();
        for (Point2D.Double point : vertexArrayList) {
            point.setLocation(point.getX() + dx, point.getY() + dy);
        }
        setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY() + dy)));
    }
    @Override
    public void scale(Point recentMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentMousePosition.getX();   double dy = currentMousePosition.getY() - recentMousePosition.getY();
        if (isNearTopLeftCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY() + dy)));
            setSize(new Point((int)(getSize().getX() - dx), (int)(getSize().getY() - dy)));
            for (Point2D.Double point : vertexArrayList) {
                double proportionalFactorX = (getSize().getX() - (point.getX() - getPosition().getX())) / getSize().getX();
                double proportionalFactorY = (getSize().getY() - (point.getY() - getPosition().getY())) / getSize().getY();
                point.setLocation(point.getX() + dx * proportionalFactorX, point.getY() + dy * proportionalFactorY);
            }
        }
        else if (isNearTopRightCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX()), (int)(getPosition().getY() + dy)));
            setSize(new Point((int)(getSize().getX() + dx), (int)(getSize().getY() - dy)));
            for (Point2D.Double point : vertexArrayList) {
                double proportionalFactorX = (point.getX() - getPosition().getX()) / getSize().getX();
                double proportionalFactorY = (getSize().getY() - (point.getY() - getPosition().getY())) / getSize().getY();
                point.setLocation(point.getX() + dx * proportionalFactorX, point.getY() + dy * proportionalFactorY);
            }
        }
        else if (isNearBottomLeftCorner(currentMousePosition) == true) {
            setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY())));
            setSize(new Point((int)(getSize().getX() - dx), (int)(getSize().getY() + dy)));
            for (Point2D.Double point : vertexArrayList) {
                double proportionalFactorX = (getSize().getX() - (point.getX() - getPosition().getX())) / getSize().getX();
                double proportionalFactorY = (point.getY() - getPosition().getY()) / getSize().getY();
                point.setLocation(point.getX() + dx * proportionalFactorX, point.getY() + dy * proportionalFactorY);
            }
        }
        else if (isNearBottomRightCorner(currentMousePosition) == true) {
            // position은 바뀌지 않음
            setSize(new Point((int)(getSize().getX() + dx), (int)(getSize().getY() + dy)));
            for (Point2D.Double point : vertexArrayList) {
                double proportionalFactorX = (point.getX() - getPosition().getX()) / getSize().getX();
                double proportionalFactorY = (point.getY() - getPosition().getY()) / getSize().getY();
                point.setLocation(point.getX() + dx * proportionalFactorX, point.getY() + dy * proportionalFactorY);
            }
        }
        else return;
    }
    
    /*
    **  레이어 출력 관련 메소드
    */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform resetAffineTransform = g2d.getTransform(); // 기존 아핀 변환 정보 저장
        g.setColor(getColor());
        g2d.setStroke(getStroke());
        g2d.rotate(getRadianAngle(), getPosition().getX() + getSize().getX() / 2, getPosition().getY() + getSize().getY() / 2);
        
        // drawPolyline 메소드서 Polyline을 그리기 위해 필요한 배열을 생성함
        int[] xPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            xPoints[i] = (int)(vertexArrayList.get(i).getX());
        }
        int[] yPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            yPoints[i] = (int)(vertexArrayList.get(i).getY());
        }

        if (getBackgroundType() == BackgroundType.EMPTY) g.drawPolyline(xPoints, yPoints, vertexArrayList.size());
        else if (getBackgroundType() == BackgroundType.FILL) g.fillPolygon(xPoints, yPoints, vertexArrayList.size());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.POLYLINE; }
    
    @Override
    public Rectangle getBoundingBox() { // 이 도형을 감싸는 사각형 반환
        double minX = vertexArrayList.get(0).getX();   double minY = vertexArrayList.get(0).getY();
        double maxX = vertexArrayList.get(0).getX();   double maxY = vertexArrayList.get(0).getY();
        for (Point2D.Double point : vertexArrayList) {
            minX = min(point.getX(), minX);
            minY = min(point.getY(), minY);
            maxX = max(point.getX(), maxX);
            maxY = max(point.getY(), maxY);     
        }
        return new Rectangle((int)minX, (int)minY, (int)maxX - (int)minX, (int)maxY - (int)minY);
    }
    public boolean getIsFinishedInitializing() { // 이 도형의 생성이 완료되었는지 반환
        return isFinishedInitializing;
    }

}
