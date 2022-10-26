package com.chess.gui;


import javax.swing.*;
import java.awt.*;

public class Table {

    private final JFrame gameFrame;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);


    public Table() {
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);

        this.gameFrame.setVisible(true);
    }

}
