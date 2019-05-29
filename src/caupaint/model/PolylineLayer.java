
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.*;
import java.util.ArrayList;

public class PolylineLayer extends ShapeLayer{

    private ArrayList<Point2D.Double> vertexArrayList; // 이 도형을 구성하는 점의 위치 정보를 저장하는 ArrayList
    private boolean isFinishedInitializing; // 폴리선 레이어가 그리기를 마쳤으면 true, 그렇지 않으면 false
    private Point originalSize; // 생성했을 당시의 크기 정보, 외부에서 변경해서는 안되므로 setter를 만들지 않음
    
    /*
    ** 생성자
    */
    public PolylineLayer(String name, Point position, Point size, Color borderColor, Color backgroundColor, BasicStroke stroke, BackgroundType backgroundType, double radianAngle, int isFlipped, boolean isVisible) { // 생성에 사용할 모든 정보를 전달받음
        super(name, position, size, borderColor, backgroundColor, stroke, backgroundType, radianAngle, isFlipped, isVisible);
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = false;
        originalSize = new Point((int)getSize().getX(), (int)getSize().getY());
    }
    public PolylineLayer() {
        super();
        vertexArrayList = new ArrayList<Point2D.Double>();
        isFinishedInitializing = false;
        originalSize = new Point((int)getSize().getX(), (int)getSize().getY());
    }
    public PolylineLayer(PolylineLayer source) { // 복제 생성자
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
        public String getDefaultName() { return "새 폴리선"; }
        public PolylineLayer build() {
            BasicStroke tempStroke = new BasicStroke(strokeWidth, Constant.defaultSolidLineBasicStroke.getEndCap(), Constant.defaultSolidLineBasicStroke.getLineJoin(), Constant.defaultSolidLineBasicStroke.getMiterLimit(), strokeDash, strokeDashPhase);
            return new PolylineLayer(name, position, size, borderColor, backgroundColor, tempStroke, backgroundType, radianAngle, isFlipped, isVisible);
        }
    }
    
    /*
    ** 레이어 생성 관련 메소드
    */
    @Override
    public void initialize(MouseActionType mouseActionType, Point recentlyPressedMousePosition, Point currentMousePosition) {
        switch(mouseActionType) {
            case PRESSED:
                isFinishedInitializing = false; // 그리는 중이라고 표시
                vertexArrayList.add(new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY()));
                break;
            case DRAGGED:
                vertexArrayList.set(vertexArrayList.size() - 1, new Point2D.Double(currentMousePosition.getX(), currentMousePosition.getY())); // 가장 최근에 생성한 Vertex의 위치 정보를 현재 마우스의 위치로 변경함
                break;
            case RELEASED:
                if((vertexArrayList.size() > 1 // 최소 2개 이상의 Vertex가 있어야 함
                        && vertexArrayList.get(vertexArrayList.size() - 1).getX() > vertexArrayList.get(vertexArrayList.size() - 2).getX() - Constant.nearPointRecognitionRangeRadius)
                        && (vertexArrayList.get(vertexArrayList.size() - 1).getX() < vertexArrayList.get(vertexArrayList.size() - 2).getX() + Constant.nearPointRecognitionRangeRadius)
                        && (vertexArrayList.get(vertexArrayList.size() - 1).getY() > vertexArrayList.get(vertexArrayList.size() - 2).getY() - Constant.nearPointRecognitionRangeRadius)
                        && (vertexArrayList.get(vertexArrayList.size() - 1).getY() < vertexArrayList.get(vertexArrayList.size() - 2).getY() + Constant.nearPointRecognitionRangeRadius)) { // 마지막 Vertex의 위치가 그 전 Vertex의 근처일 경우

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
                }
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
        g2d.rotate(getRadianAngle(), getCentralPoint().getX(), getCentralPoint().getY());
        
        // drawPolyline 메소드에서 Polyline을 그리기 위해 필요한 배열을 생성함
        int[] xPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            if (getIsFinishedInitializing() == true && getIsFlippedHorizontally() == true) xPoints[i] = (int)(getSize().getX() - (vertexArrayList.get(i).getX()) * (getSize().getX() / originalSize.getX()) + getPosition().getX());
            else if (getIsFinishedInitializing() == true) xPoints[i] = (int)((vertexArrayList.get(i).getX()) * (getSize().getX() / originalSize.getX()) + getPosition().getX());
            else xPoints[i] = (int)(vertexArrayList.get(i).getX());
        }
        int[] yPoints = new int[vertexArrayList.size()];
        for(int i = 0; i < vertexArrayList.size(); i++) {
            if (getIsFinishedInitializing() == true && getIsFlippedVertically() == true) yPoints[i] = (int)(getSize().getY() - (vertexArrayList.get(i).getY()) * (getSize().getY() / originalSize.getY()) + getPosition().getY());
            else if (getIsFinishedInitializing() == true) yPoints[i] = (int)((vertexArrayList.get(i).getY()) * (getSize().getY() / originalSize.getY()) + getPosition().getY());
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
    public ArrayList<Point2D.Double> getVertexArayList() { return vertexArrayList; }
    @Override public ShapeType getRealShapeType() { return ShapeType.POLYLINE; }
    @Override public String getIconFileName() { return "polyline.png"; } ;
        @Override public ShapeLayer getWireframe() {
        PolylineLayer wireframe = (PolylineLayer)ShapeLayerFactory.createClone(ShapeType.POLYLINE, this);
        wireframe.setBackgroundType(BackgroundType.EMPTY);
        wireframe.setBorderColor(Color.GRAY);
        return wireframe;
    }
    public boolean getIsFinishedInitializing() { return isFinishedInitializing; }// 이 도형의 생성이 완료되었는지 반환
    public Point getOriginalSize() { return originalSize; }

}
