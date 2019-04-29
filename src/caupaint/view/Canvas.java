package caupaint.view;
import caupaint.model.*;
import java.awt.Color;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;

public class Canvas extends JPanel {
    
    ArrayList<Shape> layerArrayList;
    
    /*
    ** 생성자
    */
    public Canvas(Layer layer) {
        this.setBackground(Color.white);
        this.layerArrayList = layer.getArrayList();
    }
    
    /*
    ** 그래픽 관련 메소드
    */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (!layerArrayList.isEmpty()) {
            for(Shape x: layerArrayList){
                x.draw(g);
            }
        }
    }
}
