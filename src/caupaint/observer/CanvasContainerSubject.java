
package caupaint.observer;

/*
** CanvasContainer를 변경하고 변경 사실을 전파하는 클래스에 구현하는 인터페이스
*/
public interface CanvasContainerSubject {
    
    public void registerCanvasContainerObserver(CanvasContainerObserver o);
    public void removeCanvasContainerObserver(CanvasContainerObserver o);
    public void notifyCanvasContainerObservers();
    
}
