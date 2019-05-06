
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.ArrayList;

public class LineLayer extends ShapeLayer{

    private ArrayList<Point> vertexArrayList; // 이 도형을 구성하는 점의 위치 정보를 저장하는 ArrayList
    
    /*
    ** 생성자
    */
    public LineLayer(String name, Point position, Point size, Color color, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, color, stroke, backgroundType, radianAngle, isVisible);
        vertexArrayList = new ArrayList<Point>();
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 시작점 설정
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 끝점 설정, 시작점과 동일한 값을 가짐
    }
    public LineLayer(Point position, Point size) {
        super(position, size);
        vertexArrayList = new ArrayList<Point>();
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 시작점 설정
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 끝점 설정, 시작점과 동일한 값을 가짐
    }
    public LineLayer() {
        super();
        vertexArrayList = new ArrayList<Point>();
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 시작점 설정
        vertexArrayList.add(new Point((int)getPosition().getX(), (int)getPosition().getY())); // 끝점 설정, 시작점과 동일한 값을 가짐
    }
    public LineLayer(LineLayer source) { // 복제 생성자
        super(source);
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(Point currentMousePosition) {
        vertexArrayList.set(0, currentMousePosition);
    }
    @Override
    public void keepInitializing(Point recentlyPressedMousePosition, Point currentMousePosition) {
        vertexArrayList.set(1, currentMousePosition);
    }
    @Override
    public void finishInitializing() {
        if (vertexArrayList.get(0).getX() <= vertexArrayList.get(1).getX() && vertexArrayList.get(0).getY() <= vertexArrayList.get(1).getY()) { // 1사분면
            setPosition(new Point((int)vertexArrayList.get(0).getX(), (int)vertexArrayList.get(0).getY()));
        }
        else if (vertexArrayList.get(0).getX() <= vertexArrayList.get(1).getX() && vertexArrayList.get(0).getY() <= vertexArrayList.get(1).getY()) { // 2사분면
            setPosition(new Point((int)vertexArrayList.get(1).getX(), (int)vertexArrayList.get(0).getY()));
        }
        else if (vertexArrayList.get(0).getX() <= vertexArrayList.get(1).getX() && vertexArrayList.get(0).getY() <= vertexArrayList.get(1).getY()) { // 3사분면
            setPosition(new Point((int)vertexArrayList.get(1).getX(), (int)vertexArrayList.get(1).getY()));
        }
        else if (vertexArrayList.get(0).getX() <= vertexArrayList.get(1).getX() && vertexArrayList.get(0).getY() <= vertexArrayList.get(1).getY()) { // 4사분면
            setPosition(new Point((int)vertexArrayList.get(0).getX(), (int)vertexArrayList.get(1).getY()));
        }
        setSize(new Point(abs((int)(vertexArrayList.get(1).getX() - vertexArrayList.get(0).getX())), abs((int)(vertexArrayList.get(1).getY() - vertexArrayList.get(0).getY()))));
    }
    
    /*
    ** 레이어 변형 관련 메소드
    */
    @Override
    public void translate(Point recentMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentMousePosition.getX();   double dy = currentMousePosition.getY() - recentMousePosition.getY();
        for (Point point : vertexArrayList) {
            point.setLocation(point.getX() + dx, point.getY() + dy);
        }
        setPosition(new Point((int)(getPosition().getX() + dx), (int)(getPosition().getY() + dy)));
    }
    @Override
    public void scale(Point recentMousePosition, Point currentMousePosition) {
        double dx = currentMousePosition.getX() - recentMousePosition.getX();   double dy = currentMousePosition.getY() - recentMousePosition.getY();
        
        if (sqrt(pow(currentMousePosition.getX() - vertexArrayList.get(0).getX(), 2) + pow(currentMousePosition.getY() - vertexArrayList.get(0).getY(), 2))
                <= sqrt(pow(currentMousePosition.getX() - vertexArrayList.get(1).getX(), 2) + pow(currentMousePosition.getY() - vertexArrayList.get(1).getY(), 2))) { // 현재 마우스의 위치가 0번 점에 더 가까운 경우
            vertexArrayList.get(0).setLocation(vertexArrayList.get(0).getX() + dx, vertexArrayList.get(0).getY() + dy);
        }
        else if (sqrt(pow(currentMousePosition.getX() - vertexArrayList.get(0).getX(), 2) + pow(currentMousePosition.getY() - vertexArrayList.get(0).getY(), 2))
                > sqrt(pow(currentMousePosition.getX() - vertexArrayList.get(1).getX(), 2) + pow(currentMousePosition.getY() - vertexArrayList.get(1).getY(), 2))) { // 현재 마우스의 위치가 1번 점에 더 가까운 경우
            vertexArrayList.get(1).setLocation(vertexArrayList.get(1).getX() + dx, vertexArrayList.get(1).getY() + dy);
        }
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
        g2d.rotate(getRadianAngle(), (vertexArrayList.get(0).getX() + vertexArrayList.get(1).getX()) / 2, (vertexArrayList.get(0).getY() + vertexArrayList.get(1).getY()) / 2);
        g.drawLine((int)vertexArrayList.get(0).getX(), (int)vertexArrayList.get(0).getY(), (int)vertexArrayList.get(1).getX(), (int)vertexArrayList.get(1).getY());
        g2d.setTransform(resetAffineTransform); // 기존 아핀 변환 정보로 초기화, 다음에 그려질 그래픽 객체들이 이전 객체의 아핀 변환 값에 영향을 받지 않게 하기 위함
    }
    
    /*
    ** getter, setter
    */
    @Override public ShapeType getRealShapeType() { return ShapeType.LINE; }
    
    @Override
    public Rectangle getBoundingBox() { // 이 도형을 감싸는 사각형 반환
        double minX = vertexArrayList.get(0).getX();   double minY = vertexArrayList.get(0).getY();
        double maxX = vertexArrayList.get(0).getX();   double maxY = vertexArrayList.get(0).getY();
        for (Point point : vertexArrayList) {
            minX = min(point.getX(), minX);
            minY = min(point.getY(), minY);
            maxX = max(point.getX(), maxX);
            maxY = max(point.getY(), maxY);     
        }
        return new Rectangle((int)minX, (int)minY, (int)maxX - (int)minX, (int)maxY - (int)minY);
    }
    @Override
    public Point getCentralPoint() {
        Point point = new Point();
        point.setLocation(getBoundingBox().getCenterX(), getBoundingBox().getCenterY());
        return point;
    }
    
}
