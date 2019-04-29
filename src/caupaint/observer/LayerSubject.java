
package caupaint.observer;

public interface LayerSubject {
    
    public void registerLayerObserver(LayerObserver o);
    public void removeLayerObserver(LayerObserver o);
    public void notifyLayerObservers();
    
}
