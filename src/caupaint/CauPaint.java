
package caupaint;
import caupaint.controller.*;
import caupaint.model.CanvasContainer;
import caupaint.model.Variable;
import caupaint.view.CanvasView;
import caupaint.view.MainView;
import caupaint.view.SidebarView;

public class CauPaint {
    
    public static void main(String[] args) {
        CanvasContainer canvasContainer = new CanvasContainer();
        Variable variable = new Variable();
        
        CanvasView canvasView = new CanvasView(canvasContainer, variable);
        SidebarView sidebarView = new SidebarView(canvasContainer, variable);
        MainView mainView = new MainView(canvasContainer, variable, canvasView, sidebarView);
        
        Controller controller = new Controller(canvasContainer, variable, canvasView, sidebarView, mainView);
        
        mainView.run();
    }
    
}
