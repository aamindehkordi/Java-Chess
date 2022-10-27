package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {

    private final JFrame gameFrame; // JFrame is a class that creates a window
    private final BoardPanel boardPanel; // BoardPanel is a class that creates a board

    private final Color lightTileColor; // Light tile color
    private final Color darkTileColor; // Dark tile color
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600); // 600x600
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350); // 400x350
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10); // 10x10
    private final Board chessBoard; // Board is a class that creates a board
    private String defaultPieceImagesPath; // path to the images of the pieces

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
        this.defaultPieceImagesPath = "art/standard/"; // Sets the path to the images of the pieces
        this.chessBoard = Board.createStandardBoard(); // Creates a new chess board
        this.darkTileColor = Color.decode("#71573F"); // Sets the dark tile color to a brown color
        this.lightTileColor = Color.decode("#B9A582"); // Sets the light tile color to a tan color

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
        final JMenuBar tableMenuBar = new JMenuBar(); // Creates a new JMenuBar
        tableMenuBar.add(createFileMenu()); // Adds a new JMenu to the JMenuBar
        return tableMenuBar; // Returns the JMenuBar created

    }

    /**
     * Creates and adds the file menu to the JMenuBar
     *
     * @return the File JMenu created
     */
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File"); // Creates a new JMenu with the title "File"

        addPGNMenuItem(fileMenu); // Adds the PGN menu item to the File JMenu
        addExitMenuItem(fileMenu); // Adds the Exit menu item to the File JMenu

        return fileMenu; // Returns the File JMenu created
    }

    /**
     * Adds the PGN menu item to the File JMenu
     *
     * @param fileMenu the File JMenu to add the PGN menu item to
     */
    private static void addPGNMenuItem(JMenu fileMenu) {
        final JMenuItem openPGN = new JMenuItem("Load PGN File"); // Creates a new JMenuItem with the title "Load PGN File"
        openPGN.addActionListener(new ActionListener() { // Adds an ActionListener to the JMenuItem
            @Override
            public void actionPerformed(ActionEvent e) { // When the JMenuItem is clicked, this method is called
                System.out.println("Open up that pgn file!"); // Prints "Open up that pgn file!" to the console
            }
        });
        fileMenu.add(openPGN); // Adds the JMenuItem to the File JMenu
    }

    /**
     * Adds the Exit menu item to the File JMenu
     *
     * @param fileMenu the File JMenu to add the Exit menu item to
     */
    private static void addExitMenuItem(JMenu fileMenu) {
        final JMenuItem exitMenuItem = new JMenuItem("Exit"); // Creates a new JMenuItem with the title "Exit"
        exitMenuItem.addActionListener(new ActionListener() { // Adds an ActionListener to the JMenuItem
            @Override
            public void actionPerformed(ActionEvent e) { // When the JMenuItem is clicked, this method is called
                System.exit(0); // Exits the program
            }
        });
        fileMenu.add(exitMenuItem); // Adds the JMenuItem to the File JMenu
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
            assignTilePieceIcon(chessBoard); // Assign the piece icon of the tile
            validate(); // Validate the tile
        }

        /**
         * Assigns the color of the tile
         *
         * @param board the board the tile is on
         */
        private void assignTilePieceIcon(final Board board) {
            this.removeAll(); // Remove all the components from the tile because we are going to add a new one
            if (board.getTile(this.tileId).isTileOccupied()) { // If the tile is occupied
                try { // Try to get the image of the piece
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath + // Get the image of the piece
                            board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + // Get the alliance of the piece
                            board.getTile(this.tileId).getPiece().toString() + // Get the type of the piece
                            ".png")); // Get the file extension
                    add(new JLabel(new ImageIcon(image))); // Add the image to the tile
                } catch (IOException e) { // If there is an error
                    e.printStackTrace(); // Print the error to the console
                }
            }

        }

        /**
         * Assigns the color of the tile based on the id of the tile
         */
        private void assignTileColor() {
            // If the id of the tile is even on these rows, set the color to the light tile color
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                    BoardUtils.THIRD_ROW[this.tileId] ||
                    BoardUtils.FIFTH_ROW[this.tileId] ||
                    BoardUtils.SEVENTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }
            // If the id of the tile is odd on these rows, set the color to the light tile color
            else if (BoardUtils.SECOND_ROW[this.tileId] ||
                    BoardUtils.FOURTH_ROW[this.tileId] ||
                    BoardUtils.SIXTH_ROW[this.tileId] ||
                    BoardUtils.EIGHTH_ROW[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }

        }
    }
}
