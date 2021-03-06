
package caupaint.model;
import caupaint.model.Enum.*;

import java.awt.*;

public class Constant {
    
    /*
    ** 기본 상수
    */
    final static public String defaultIconDirectoryPath = "./icon/"; // 아이콘이 저장되는 디렉토리
    final static public String defaultFilenameExtension = ".caupaint"; // 저장되는 파일이 가지는 확장자
    final static public String defaultCopiedFileSuffix = " - 사본";
    
    /*
    ** Layer 관련 상수
    */
    final static public BasicStroke defaultSolidLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f); // 실선
    final static public BasicStroke defaultDottedLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2, 13}, 15.0f); // 점선
    final static public BasicStroke defaultDashedLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{20, 20}, 40.0f); // 파선
    final static public BasicStroke defaultLongDashedLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{50, 50}, 100.0f); // 긴파선
    final static public BasicStroke defaultDashSingleDottedLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{40, 11, 2, 11}, 100.0f); // 1점 쇄선
    final static public BasicStroke defaultDashDoubleDottedLineBasicStroke = new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{40, 10, 2, 10, 2, 10}, 110.0f); // 2점 쇄선
    final static public BasicStroke anchorStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f); // 앵커 draw 시 사용하는 선
    final static public int nearPointRecognitionRangeRadius = 5; // 마우스가 특정 좌표의 근처에 있는지 판단하기 위한 반지름
    final static public int upperTopMargin = 50; // UPPER_TOP과 TOP 간의 y좌표 차이
    
    /*
    ** Canvas 관련 상수
    */
    final static public Point defaultCanvasSize = new Point(640, 480);
    final static public Color defaultCanvasBackgroundColor = Color.WHITE;
    final static public BasicStroke defaultLayerSelectedLineBasicStroke = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{2, 8}, 4.0f);

    /*
    ** Variable 관련 상수
    */
    final static public FunctionType defaultFunctionType = FunctionType.SELECT;
    final static public ShapeType defaultShapeType = ShapeType.RECTANGLE;
    final static public BackgroundType defaultBackgroundType = BackgroundType.EMPTY;
    final static public MouseActionType defaultMouseActionType = MouseActionType.RELEASED;
    final static public ShapeLayerAnchorType defaultShapeLayerAnchorType = ShapeLayerAnchorType.CENTER;
    final static public Color defaultBorderColor = Color.BLACK;
    final static public Color defaultBackgroundColor = Color.WHITE;
    final static public BasicStroke defaultStroke = defaultSolidLineBasicStroke;
    final static public int isFlippedHorizontallyFlag = 0x1;
    final static public int isFlippedVerticallyFlag  = 0x2;
    final static public boolean defaultIsVisible = true;
    final static public Font defaultFont = new Font("바탕", 0, 30);
    final static public int defaultSelectedLayerIndex = -1;
    final static public String defaultFilePath = null;
   
    /*
    ** MainView 관련 상수
    */
    final static public Dimension defaultWindowSize = new Dimension(1366, 768);
    final static public Color defaultChoosedButtonColor = new Color(255, 255, 255);
    final static public Color defaultUnchoosedButtonColor = new Color(234, 234, 234);
    
    /*
    ** SidebarView 관련 상수
    */
    final static public Dimension defaultLayerListScrollPaneSize = new Dimension(300, 1000);
    final static public Dimension defaultToolBarButtonSize = new Dimension(28, 28);
    final static public Dimension defaultToggleSelectedLayerVisibleButtonSize = new Dimension(14, 14);
    final static public Dimension defaultLayerListLabelSize = new Dimension(165, 20);
    final static public Dimension defaultToolBarButtonImageSize = new Dimension(20, 20);
    
}
