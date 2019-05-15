
package caupaint.model;

import caupaint.model.Enum.ShapeType;

public class ShapeLayerFactory {
    public static ShapeLayer create(ShapeType shapeType) {
        switch(shapeType) {
            case POLYLINE: return new PolylineLayer();
            case PEN: return new PenLayer();
            case RECTANGLE: return new RectangleLayer();
            case ELLIPSE: return new EllipseLayer();
            case TRIANGLE: return new TriangleLayer();
            case RHOMBUS: return new RhombusLayer();
            case TEXT: return new TextLayer();
            case IMAGE: return new ImageLayer();
            default: return null;
        }
    }
    public static ShapeLayer createClone(ShapeType shapeType, ShapeLayer shapeLayer) {
        switch(shapeType) {
            case POLYLINE: return new PolylineLayer((PolylineLayer)shapeLayer);
            case PEN: return new PenLayer((PenLayer)shapeLayer);
            case RECTANGLE: return new RectangleLayer((RectangleLayer)shapeLayer);
            case ELLIPSE: return new EllipseLayer((EllipseLayer)shapeLayer);
            case TRIANGLE: return new TriangleLayer((TriangleLayer)shapeLayer);
            case RHOMBUS: return new RhombusLayer((RhombusLayer)shapeLayer);
            case TEXT: return new TextLayer((TextLayer)shapeLayer);
            case IMAGE: return new ImageLayer((ImageLayer)shapeLayer);
            default: return null;
        }
    }
    public static ShapeLayer.Builder createBuilder(ShapeType shapeType) {
        switch(shapeType) {
            case POLYLINE: return new PolylineLayer.Builder();
            case PEN: return new PenLayer.Builder();
            case RECTANGLE: return new RectangleLayer.Builder();
            case ELLIPSE: return new EllipseLayer.Builder();
            case TRIANGLE: return new TriangleLayer.Builder();
            case RHOMBUS: return new RhombusLayer.Builder();
            case TEXT: return new TextLayer.Builder();
            case IMAGE: return new ImageLayer.Builder();
            default: return null;
        }
    }
}
