
package caupaint.observer;

/*
** Layer가 변경되었을 때 이를 구독하는 클래스에 구현하는 인터페이스
*/
public interface LayerContainerObserver {
    
    public void updateLayerContainer();
    
}
