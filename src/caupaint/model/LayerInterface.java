
package caupaint.model;

public interface LayerInterface {
    
    public void addShape(Shape shape);
    public void deleteShape(int index);
    public void deleteLastShape();
    public void clear();
    
}
