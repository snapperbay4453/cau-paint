
package caupaint.controller;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.model.command.CommandFactory;
import caupaint.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

public class Controller{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    private CanvasView canvasView;
    private SidebarView sidebarView;
    private MainView mainView;
    
    /*
    ** 생성자
    */
    public Controller() {
        canvasContainer = new CanvasContainer();
        variable = new Variable(this);
        canvasView = new CanvasView(canvasContainer, variable, this);
        sidebarView = new SidebarView(canvasContainer, variable, this);
        mainView = new MainView(canvasContainer, variable, canvasView, sidebarView, this);
        
        mainView.createView();
    }
 
    /*
    ** MainView 이벤트 리스너 관련 메소드
    */
    // 이벤트 리스너: mainView.MenuBarClickedActionListener
    public void MainViewMenuBarClickedEventHandler(ActionEvent event) {
        CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute();
    }
    // 이벤트 리스너: mainView.ButtonClickedActionListener
    public void MainViewButtonClickedEventHandler(ActionEvent event) {
        CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute();
    }
    // 이벤트 리스너: mainView.StrokeTypeItemChangeActionListener
    public void MainViewStrokeTypeComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
            CommandFactory.create("setStrokeByName", canvasContainer, variable, event, event.getItem().toString()).execute();
       }
    }       
    // 이벤트 리스너: mainView.StrokeWidthSpinnerChangeActionListener
    public void MainViewStrokeWidthSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       CommandFactory.create("setStrokeWidth", canvasContainer, variable, event, spinnerValue).execute();
    }
    // 이벤트 리스너: mainView.FontNameComboBoxItemChangeActionListener
    public void MainViewFontNameComboBoxItemStateChangedEventHandler(ItemEvent event) {
       if (event.getStateChange() == ItemEvent.SELECTED) {
           CommandFactory.create("setFontName", canvasContainer, variable, event, event.getItem().toString()).execute();
       }
    }       
    // 이벤트 리스너: mainView.FontSizeSpinnerStateChangeActionListener
    public void MainViewFontSizeSpinnerStateChangedEventHandler(ChangeEvent event, int spinnerValue) {
       CommandFactory.create("setFontSize", canvasContainer, variable, event, spinnerValue).execute();
    }
    // 이벤트 리스너: mainView.WindowActionListener
    public void MainViewWindowClosingEventHandler(WindowEvent event) {
        CommandFactory.create("checkExit", canvasContainer, variable, event, null).execute();
    }
    
    /*
    ** Canvas 이벤트 리스너 관련 메소드
    */
    // 이벤트 리스너: canvasView.CanvasMouseAdapter
    public void CanvasViewMousePressedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.PRESSED);
    variable.setRecentlyPressedMousePosition(event.getPoint());
    variable.setRecentlyDraggedMousePosition(event.getPoint());
        switch(variable.getFunctionType()) {
            case SELECT:    CommandFactory.create("selectLayerByMousePoint", canvasContainer, variable, event, null).execute();  break;
            case DRAW:      CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case MOVE:      CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
            default:        break;
        }
    }
    public void CanvasViewMouseDraggedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.DRAGGED);
        switch(variable.getFunctionType()) {
            case SELECT:    break;
            case DRAW:      CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case MOVE:      CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
            default:        break;
        }
    variable.setRecentlyDraggedMousePosition(event.getPoint());
    }
    public void CanvasViewMouseReleasedEventHandler(MouseEvent event) {
    variable.setMouseActionType(MouseActionType.RELEASED);
        switch(variable.getFunctionType()) {
            case SELECT:    break;
            case DRAW:      CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case MOVE:      CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case RESIZE:    CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
            case ROTATE:    CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
            default:        break;
        }
    }
    
    /*
    ** Sidebar 이벤트 리스너 관련 메소드
    */
    // 이벤트 리스너: sidebarView.LayerListSelectionListener
        public void SidebarValueChangedEventHandler(ListSelectionEvent event, int index){
            CommandFactory.create("setSelectedLayerIndex", canvasContainer, variable, event, index).execute();
        }
    // 이벤트 리스너: sidebarView.ButtonClickedActionListener
        public void SidebarActionPerformedEventHandler(ActionEvent event) {
            CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute();
        }
        
}
