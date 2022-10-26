package com.chess.gui;


import com.chess.engine.board.BoardUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private final JFrame gameFrame; // JFrame is a class that creates a window
    private final BoardPanel boardPanel; // BoardPanel is a class that creates a board

    private final Color lightTileColor;
    private final Color darkTileColor;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600); // 600x600
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350); // 400x350
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10); // 10x10

    /**
     * Constructor for the Table class
     * Creates a new JFrame and sets the title, size, and default close operation
     * Also sets the layout of the JFrame to a BorderLayout
     */
    public Table() {
        this.gameFrame = new JFrame("Chess"); // Creates a new JFrame with the title "Chess"
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION); // Sets the size of the JFrame to 600x600
        final JMenuBar tableMenuBar = createTableMenuBar(); // Creates a new JMenuBar
        this.gameFrame.setLayout(new BorderLayout()); // Sets the layout of the JFrame to a BorderLayout
        this.gameFrame.setJMenuBar(tableMenuBar); // Sets the JMenuBar of the JFrame to the JMenuBar created above

        this.darkTileColor = Color.decode("#71573F");
        this.lightTileColor = Color.decode("#B9A582");

        this.boardPanel = new BoardPanel(); // Creates a new BoardPanel
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER); // Adds the BoardPanel to the center of the JFrame

        this.gameFrame.setVisible(true); // Sets the JFrame to be visible

    }

    /**
     * Creates a new JMenuBar and adds a new JMenu to it
     *
     * @return the JMenuBar created
     */
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;

    }

    /**
     * Adds the file menu to the JMenuBar
     *
     * @return the File JMenu created
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up that pgn file!");
            }
        });
        fileMenu.add(openPGN);
        return fileMenu;
    }


    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles; // List of all the tiles on the board

        /**
         * Constructor for the BoardPanel class
         */
        BoardPanel() {
            super(new GridLayout(8, 8)); // GridLayout is a layout manager that lays out a container's components in a grid
            this.boardTiles = new ArrayList<>();  // Initialize the list of tiles
            for (int i = 0; i < BoardUtils.NUM_TILES; i++) { // Loop through all the tiles
                final TilePanel tilePanel = new TilePanel(this, i); // Create a new tile
                this.boardTiles.add(tilePanel); // Add the tile to the list of tiles
                add(tilePanel); // Add the tile to the board
            }
            setPreferredSize(BOARD_PANEL_DIMENSION); // Set the size of the board to 400x350
            validate(); // Validate the board
        }
    }

    private class TilePanel extends JPanel {
        private final int tileId; // The id of the tile

        /**
         * Constructor for the TilePanel class
         *
         * @param tileId     the id of the tile
         * @param boardPanel the board the tile is on
         */
        TilePanel(final BoardPanel boardPanel, final int tileId) {
            super(new GridBagLayout()); // GridBagLayout is a layout manager that lays out a container's components in a grid
            this.tileId = tileId; // Set the id of the tile
            setPreferredSize(TILE_PANEL_DIMENSION); // Set the size of the tile
            assignTileColor(); // Assign the color of the tile
            //assignTilePieceIcon(); // Assign the piece icon of the tile
            validate(); // Validate the tile
        }

        /**
         * Assigns the color of the tile based on the id of the tile
         */
        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId] ||
                    BoardUtils.EIGHTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }

        }
    }
}
