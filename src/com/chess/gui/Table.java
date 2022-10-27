package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static javax.swing.SwingUtilities.*;

public class Table {

    private final JFrame gameFrame; // JFrame is a class that creates a window
    private final BoardPanel boardPanel; // BoardPanel is a class that creates a board

    private final Color lightTileColor; // Light tile color
    private final Color darkTileColor; // Dark tile color
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600); // 600x600
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350); // 400x350
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10); // 10x10
    private Board chessBoard; // Board is a class that creates a board
    private String defaultPieceImagesPath; // path to the images of the pieces
    private Tile sourceTile; // source tile
    private Tile destinationTile; // destination tile
    private Piece humanMovedPiece; // human moved piece
    private BoardDirection boardDirection; // board direction
    private boolean highlightLegalMoves; // highlight legal moves



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
        this.boardDirection = BoardDirection.NORMAL; // Sets the board direction to normal
        this.boardPanel = new BoardPanel(); // Creates a new BoardPanel
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER); // Adds the BoardPanel to the center of the JFrame
        this.highlightLegalMoves = true; // Sets highlightLegalMoves to false
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
        tableMenuBar.add(createPreferencesMenu()); // Adds the preferences JMenu to the JMenuBar
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
    /**
     * Creates and adds the preferences menu to the JMenuBar
     *
     * @return the Preferences JMenu created
     */
    private JMenu createPreferencesMenu() {
        final JMenu preferencesMenu = new JMenu("Preferences"); // Creates a new JMenu with the title "Preferences"
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board"); // Creates a new JMenuItem with the title "Flip Board"
        flipBoardMenuItem.addActionListener(new ActionListener() { // Adds an ActionListener to the JMenuItem
            @Override
            public void actionPerformed(ActionEvent e) { // When the JMenuItem is clicked, this method is called
                boardDirection = boardDirection.opposite(); // Sets the board direction to the opposite of the current board direction
                boardPanel.drawBoard(chessBoard); // Draws the board
            }
        });
        preferencesMenu.add(flipBoardMenuItem); // Adds the JMenuItem to the Preferences JMenu
        preferencesMenu.addSeparator(); // Adds a separator to the Preferences JMenu
        final JCheckBox legalMoveHighlighterCheckbox = new JCheckBox("Highlight Legal Moves", true); // Creates a new JCheckBox with the title "Highlight Legal Moves" and sets it to false

        legalMoveHighlighterCheckbox.addActionListener(new ActionListener() { // Adds an ActionListener to the JCheckBox
            @Override
            public void actionPerformed(ActionEvent e) { // When the JCheckBox is clicked, this method is called
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected(); // Sets highlightLegalMoves to the selected state of the JCheckBox
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox); // Adds the JCheckBox to the Preferences JMenu
        //preferencesMenu.add(createStyleMenu()); // Adds the Style JMenu to the Preferences JMenu
        return preferencesMenu; // Returns the Preferences JMenu created
    }

    /** BoardDirection Class
     * */
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(final List<TilePanel> boardTiles) {
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        /**
         * Traverses the board tiles
         *
         * @param boardTiles the board tiles to traverse
         * @return the board tiles traversed
         */
        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        /**
         * Returns the opposite board direction
         *
         * @return the opposite board direction
         */
        abstract BoardDirection opposite();

    }

    /**
     * BoardPanel Class
     */
    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles; // List of all the tiles on the board

        /**
         * Constructor for the BoardPanel class
         * Creates the chess board full of JPanel Tiles
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

        public void drawBoard(final Board board) {
            removeAll(); // Remove all the tiles from the board
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) { // Loop through all the tiles
                tilePanel.drawTile(board); // Draw the tile
                add(tilePanel); // Add the tile to the board
            }
            validate(); // Validate the board
            repaint(); // Repaint the board
        }
    }

    /**
     * TilePanel Class
     */
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

            // Add a mouse listener to the tile
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent event) {
                /*if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) ||
                        BoardUtils.isEndGame(Table.get().getGameBoard())) {
                    return;
                }*/

                    if (isRightMouseButton(event)) {
                        sourceTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(event)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), tileId);
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                //moveLog.addMove(move);
                            }
                            sourceTile = null;
                            humanMovedPiece = null;
                        }
                    }
                    // SwingUtilities.invokeLater() runs the code in the Runnable object on the main thread
                    invokeLater(() -> {
                        boardPanel.drawBoard(chessBoard); // Redraw the board
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }
                @Override
                public void mouseReleased(MouseEvent e) {

                }
                @Override
                public void mouseEntered(MouseEvent e) {

                }
                @Override
                public void mouseExited(MouseEvent e) {

                }
            });


        }


        /**
         * Highlights the legal moves of the piece on the tile that was clicked
         *
         * @param board the board the tile is on
         */
        private void highlightLegals(final Board board) {
            if(highlightLegalMoves) { // If the user wants to highlight the legal moves
                Collection<Move> legalPieceMoves = pieceLegalMoves(board); // Get the legal moves of the piece on the tile
                for (final Move move : legalPieceMoves) { // Loop through all the legal moves of the piece on the tile
                    if (move.getDestinationCoordinate() == this.tileId) { // If the destination coordinate of the move is the tile that was clicked
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png"))))); // Add a green dot to the tile
                        } catch (IOException e) { // Catch any IOExceptions
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()) { // If the human moved piece is not null and the human moved piece is the same color as the current player
                return humanMovedPiece.calculateLegalMoves(board); // Return the legal moves of the human moved piece
            }
            return Collections.emptyList(); // Return an empty list
        }

        public void drawTile(Board board) {
            assignTileColor(); // Assign the color of the tile
            assignTilePieceIcon(board); // Assign the piece icon of the tile
            highlightLegals(board); // Highlight the legal moves of the piece on the tile
            validate(); // Validate the tile
            repaint(); // Repaint the tile
        }

        /**
         * Assigns the piece image to the tile its on
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
