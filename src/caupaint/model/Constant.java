
package caupaint.model;

import caupaint.model.Enum.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class Constant {
    
    /*
    ** 기본 상수
    */
    final static public Color defaultChoosedButtonColor = Color.CYAN;
    final static public String defaultIconDirectoryPath = "src/caupaint/source/icon/"; // 아이콘이 저장되는 디렉토리
    final static public String defaultFilenameExtension = ".caupaint"; // 저장되는 파일이 가지는 확장자
    final static public String defaultCopiedFileSuffix = " - 사본";
    
    /*
    ** Variable 관련 상수
    */
    final static public FunctionType defaultFunctionType = FunctionType.IDLE;
    final static public ShapeType defaultShapeType = ShapeType.RECTANGLE;
    final static public BackgroundType defaultBackgroundType = BackgroundType.EMPTY;
    final static public Point defaultPointStart = new Point(0,0);
    final static public Point defaultPointEnd = new Point(0,0);
    final static public Point defaultPointChange = new Point(0,0);
    final static public Color defaultColor = new Color(0, 0, 0);
    final static public int defaultSelectedLayerIndex = -1;
    final static public String defaultFilePath = null;
   
    /*
    ** View 관련 상수
    */
    final static public Dimension defaultWindowSize = new Dimension(1280, 720);
    
    /*
    ** Canvas 관련 상수
    */
    final static public Point defaultCanvasSize = new Point(640, 480);
    final static public Color defaultCanvasBackgroundColor = Color.WHITE;

    /*
    ** Sidebar 관련 상수
    */
    final static public Dimension defaultLayerListScrollPaneSize = new Dimension(300, 1000);
    final static public Dimension defaultToolBarButtonSize = new Dimension(28, 28);
    final static public Dimension defaultToolBarButtonImageSize = new Dimension(20, 20);
    
    /*
    ** Controller 관련 상수
    */

}
