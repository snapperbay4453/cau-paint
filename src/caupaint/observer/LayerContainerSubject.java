
package caupaint.observer;

/*
** Layer를 변경하고 변경 사실을 전파하는 클래스에 구현하는 인터페이스
*/
public interface LayerContainerSubject {
    
    public void registerLayerContainerObserver(LayerContainerObserver o);
    public void removeLayerContainerObserver(LayerContainerObserver o);
    public void notifyLayerContainerObservers();
    
}
