package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.MoveLog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel; /* the panel for the white pieces */
    private final JPanel southPanel; /* the panel for the black pieces */
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED); /* the border for the panels */
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6"); /* the bg color for the panels */
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80); /* the dimension for the panels */

    /**
     * Constructor
     */
    public TakenPiecesPanel() {
        super(new BorderLayout()); /* set the layout */
        setBackground(PANEL_COLOR); /* set the bg color */
        setBorder(PANEL_BORDER); /* set the border */
        this.northPanel = new JPanel(new GridLayout(8, 2)); /* set the layout for the white panel */
        this.southPanel = new JPanel(new GridLayout(8, 2)); /* set the layout for the black panel */
        this.northPanel.setBackground(PANEL_COLOR); /* set the bg color for the white panel */
        this.southPanel.setBackground(PANEL_COLOR); /* set the bg color for the black panel */
        add(this.northPanel, BorderLayout.NORTH); /* add the white panel to the north */
        add(this.southPanel, BorderLayout.SOUTH); /* add the black panel to the south */
        setPreferredSize(TAKEN_PIECES_DIMENSION); /* set the dimension */
    }

    /**
     * Redraw the taken pieces panel
     *
     * @param moveLog the move log
     */
    public void redo(final MoveLog moveLog) {
        southPanel.removeAll(); /* remove all the pieces from the black panel */
        northPanel.removeAll(); /* remove all the pieces from the white panel */

        final List<Piece> whiteTakenPieces = new ArrayList<>(); /* create a list for the white pieces */
        final List<Piece> blackTakenPieces = new ArrayList<>(); /* create a list for the black pieces */

        for (final Move move : moveLog.getMoves()) { /* for each move in the move log */
            if (move.isAttack()) { /* if the move is an attack */
                final Piece takenPiece = move.getAttackedPiece(); /* get the attacked piece */
                if (takenPiece.getPieceAlliance().isWhite()) { /* if the piece is white */
                    whiteTakenPieces.add(takenPiece); /* add the piece to the white list */
                } else if (takenPiece.getPieceAlliance().isBlack()) { /* if the piece is black */
                    blackTakenPieces.add(takenPiece); /* add the piece to the black list */
                } else { /* if the piece is neither white nor black */
                    throw new RuntimeException("Should not reach here!"); /* throw an exception */
                }
            }
        }

        /*
         * Sort the white taken pieces
         */
        Collections.sort(whiteTakenPieces, new Comparator<>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceType().getPieceValue(), o2.getPieceType().getPieceValue()); /* sort the white pieces by value */
            }
        });

        /*
         * Sort the black taken pieces
         */
        Collections.sort(blackTakenPieces, new Comparator<>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceType().getPieceValue(), o2.getPieceType().getPieceValue()); /* sort the black pieces by value */
            }
        });


        /*
         * Add the white taken pieces to the panels
         */
        for (final Piece takenPiece : whiteTakenPieces) {
            try {
                String defaultPieceImagesPath = "art/standard/"; /* the path to the default piece images */
                final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +  /* get the image for the piece */
                        takenPiece.getPieceAlliance().toString().substring(0, 1)
                        + takenPiece + ".png"));
                final ImageIcon icon = new ImageIcon(image); /* create an icon for the image */
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance( /* create a label for the piece */
                        icon.getIconWidth() - 15, icon.getIconHeight() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel); /* add the piece to the black panel */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * Add the black taken pieces to the panels
         */
        for (final Piece takenPiece : blackTakenPieces) {
            try {
                String defaultPieceImagesPath = "art/standard/"; /* the path to the default piece images */
                final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +  /* get the image for the piece */
                        takenPiece.getPieceAlliance().toString().substring(0, 1)
                        + takenPiece + ".png"));
                final ImageIcon icon = new ImageIcon(image); /* create an icon for the image */
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance( /* create a label for the piece */
                        icon.getIconWidth() - 15, icon.getIconHeight() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel); /* add the piece to the white panel */
            } catch (IOException e) {
                e.printStackTrace();
            }

            validate();
        }

    }
}
