
package caupaint.observer;

/*
** Variable을 변경하고 변경 사실을 전파하는 클래스에 구현하는 인터페이스
*/
public interface VariableSubject {
    
    public void registerVariableObserver(VariableObserver o);
    public void removeVariableObserver(VariableObserver o);
    public void notifyVariableObservers();
    
}
