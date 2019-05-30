
package caupaint.controller;
import caupaint.model.*;
import caupaint.model.Enum.*;
import caupaint.model.command.CommandFactory;
import caupaint.view.*;

import java.awt.event.*;
import javax.swing.AbstractButton;
import javax.swing.JMenuItem;

public class Controller{
    
    private CanvasContainer canvasContainer;
    private Variable variable;
    private CanvasView canvasView;
    private SidebarView sidebarView;
    private MainView mainView;
    
    /*
    ** 생성자
    */
    public Controller(CanvasContainer canvasContainer, Variable variable, CanvasView canvasView, SidebarView sidebarView, MainView mainView) {
        this.canvasContainer = canvasContainer;
        this.variable = variable;
        this.canvasView = canvasView;
        this.sidebarView = sidebarView;
        this.mainView = mainView;
        
        /*
        ** MainView 이벤트 리스너 관련 메소드
        */
        for (JMenuItem menuItem : mainView.getMenuItemArrayList()) {
            menuItem.addActionListener(event -> CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute());
        }
        for (AbstractButton button : mainView.getButtonArrayList()) {
            button.addActionListener(event -> CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute());
        }
        
        mainView.getStrokeTypeComboBox().addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                CommandFactory.create("setStrokeByName", canvasContainer, variable, event, event.getItem().toString()).execute();
            }
        });
        mainView.getStrokeWidthSpinner().addChangeListener(event -> CommandFactory.create("setStrokeWidth", canvasContainer, variable, event, mainView.getStrokeWidthSpinner().getValue()).execute());
        mainView.getFontNameComboBox().addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                CommandFactory.create("setFontName", canvasContainer, variable, event, event.getItem().toString()).execute();
            }
        });
        mainView.getFontSizeSpinner().addChangeListener(event -> CommandFactory.create("setFontSize", canvasContainer, variable, event, mainView.getFontSizeSpinner().getValue()).execute());
        mainView.getFrame().addWindowListener(new MainViewWindowActionListener());
        
        /*
        ** Canvas 이벤트 리스너 관련 메소드
        */
        CanvasViewMouseListener canvasViewMouseListener = new CanvasViewMouseListener();
        canvasView.addMouseListener(canvasViewMouseListener);
        canvasView.addMouseMotionListener(canvasViewMouseListener);
        
        /*
        ** Sidebar 이벤트 리스너 관련 메소드
        */
        sidebarView.getLayerList().addListSelectionListener(event -> {
            if (sidebarView.getLayerList().getSelectedIndex() != -1) CommandFactory.create("setSelectedLayerIndex", canvasContainer, variable, event, sidebarView.getLayerList().getSelectedIndex()).execute();
        });
        for (AbstractButton button : sidebarView.getButtonArrayList()) {
            button.addActionListener(event -> CommandFactory.create(event.getActionCommand(), canvasContainer, variable, event, null).execute());
        }
    }

    class MainViewWindowActionListener extends WindowAdapter {
        @Override public void windowClosing(WindowEvent event) {
            CommandFactory.create("checkExit", canvasContainer, variable, event, null).execute();
        }
    }
    
    /*
    ** Canvas 이벤트 리스너 관련 메소드
    */
    class CanvasViewMouseListener extends MouseAdapter{
        public void mouseClicked(MouseEvent event) {
            variable.setRecentlyPressedMousePosition(event.getPoint());
            variable.setRecentlyDraggedMousePosition(event.getPoint());
            CommandFactory.create("selectLayerByMousePoint", canvasContainer, variable, event, null).execute();
        }
        public void mousePressed(MouseEvent event) {
            variable.setMouseActionType(MouseActionType.PRESSED);
            variable.setRecentlyPressedMousePosition(event.getPoint());
            variable.setRecentlyDraggedMousePosition(event.getPoint());
            switch(variable.getFunctionType()) {
                case SELECT:            CommandFactory.create("selectLayerByMousePoint", canvasContainer, variable, event, null).execute();  break;
                case DRAW:              CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case MOVE:              CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case RESIZE:            CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case ROTATE:            CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case FREE_TRANSFORM:    CommandFactory.create("freeTransformShapeLayer", canvasContainer, variable, event, null).execute();  break;
                default:                break;
            }
        }
        public void mouseDragged(MouseEvent event) {
            variable.setMouseActionType(MouseActionType.DRAGGED);
            switch(variable.getFunctionType()) {
                case SELECT:            break;
                case DRAW:              CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case MOVE:              CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case RESIZE:            CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case ROTATE:            CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case FREE_TRANSFORM:    CommandFactory.create("freeTransformShapeLayer", canvasContainer, variable, event, null).execute();  break;
                default:                break;
            }
            variable.setRecentlyDraggedMousePosition(event.getPoint());
        }
        public void mouseReleased(MouseEvent event) {
            variable.setMouseActionType(MouseActionType.RELEASED);
            switch(variable.getFunctionType()) {
                case SELECT:            break;
                case DRAW:              CommandFactory.create("createNewShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case MOVE:              CommandFactory.create("moveShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case RESIZE:            CommandFactory.create("resizeShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case ROTATE:            CommandFactory.create("rotateShapeLayer", canvasContainer, variable, event, null).execute();  break;
                case FREE_TRANSFORM:    CommandFactory.create("freeTransformShapeLayer", canvasContainer, variable, event, null).execute();  break;
                default:                break;
            }
        }
    }
    
}
