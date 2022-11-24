package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.AlphaBetaWithMoveOrdering;
import com.chess.engine.player.ai.MoveStrategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static javax.swing.SwingUtilities.*;

public class Table extends Observable {

    private final JFrame gameFrame; // JFrame is a class that creates a window
    private final GameHistoryPanel gameHistoryPanel; // the panel for the game history
    private final TakenPiecesPanel takenPiecesPanel; // the panel for the taken pieces

    private final BoardPanel boardPanel; // BoardPanel is a class that creates a board

    private final Color lightTileColor; // Light tile color
    private final Color darkTileColor; // Dark tile color
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600); // 600x600
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350); // 400x350
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10); // 10x10
    private Board chessBoard; // Board is a class that creates a board
    private final MoveLog moveLog; // MoveLog is a class that creates a move log

    private final GameSetup gameSetup; // GameSetup is a class that creates a game setup

    private String pieceIconPath; // path to the images of the pieces
    private Tile sourceTile; // source tile
    private Tile destinationTile; // destination tile
    private Piece humanMovedPiece; // human moved piece
    private BoardDirection boardDirection; // board direction
    private boolean highlightLegalMoves; // highlight legal moves
    private Move computerMove; // computer move

    private static final Table INSTANCE = new Table(); // Table is a class that creates a table


    /**
     * Constructor for the Table class
     * Creates a new JFrame and sets the title, size, and default close operation
     * Also sets the layout of the JFrame to a BorderLayout
     */
    private Table() {
        this.gameFrame = new JFrame("Chess"); // Creates a new JFrame with the title "Chess"
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION); // Sets the size of the JFrame to 600x600
        final JMenuBar tableMenuBar = createTableMenuBar(); // Creates a new JMenuBar

        this.gameFrame.setLayout(new BorderLayout()); // Sets the layout of the JFrame to a BorderLayout
        this.destinationTile = null; // Sets the destination tile to null
        this.gameFrame.setJMenuBar(tableMenuBar); // Sets the JMenuBar of the JFrame to the JMenuBar created above
        this.pieceIconPath = "art/standard/"; // Sets the path to the images of the pieces
        this.chessBoard = Board.createStandardBoard(); // Creates a new chess board
        this.gameHistoryPanel = new GameHistoryPanel(); // Creates a new GameHistoryPanel
        this.takenPiecesPanel = new TakenPiecesPanel(); // Creates a new TakenPiecesPanel
        this.gameSetup = new GameSetup(this.gameFrame, true); // Creates a new GameSetup, modal means that the user cannot interact with the main window until the dialog is closed
        this.darkTileColor = Color.decode("#71573F"); // Sets the dark tile color to a brown color
        this.lightTileColor = Color.decode("#B9A582"); // Sets the light tile color to a tan color
        this.boardDirection = BoardDirection.NORMAL; // Sets the board direction to normal
        this.boardPanel = new BoardPanel(); // Creates a new BoardPanel
        this.moveLog = new MoveLog(); // Creates a new MoveLog
        this.addObserver(new TableGameAIWatcher()); // Adds a new TableGameAIWatcher to the observers
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST); // Adds the TakenPiecesPanel to the left side of the JFrame
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER); // Adds the BoardPanel to the center of the JFrame
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST); // Adds the GameHistoryPanel to the right side of the JFrame
        this.highlightLegalMoves = true; // Sets highlightLegalMoves to false
        this.gameFrame.setVisible(true); // Sets the JFrame to be visible


    }

    public static Table get() {
        return INSTANCE;
    }

    public void show() {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                Table.get().getMoveLog().clear();
                Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
            }
        });
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }
    private GameSetup getGameSetup() {
        return this.gameSetup;
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
        tableMenuBar.add(createOptionsMenu()); // Adds the options JMenu to the JMenuBar
        return tableMenuBar; // Returns the JMenuBar created

    }

    /*
      Creates and adds the file menu to the JMenuBar

      @return the File JMenu created
     */

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
     */

    private void populateMenuBar(final JMenuBar tableMenuBar) {
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
    }

    private static void center(final JFrame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int w = frame.getSize().width;
        final int h = frame.getSize().height;
        final int x = (dim.width - w) / 2;
        final int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }

    private JMenu createFileMenu() {
        final JMenu filesMenu = new JMenu("File");
        filesMenu.setMnemonic(KeyEvent.VK_F);

        /*
        final JMenuItem openPGN = new JMenuItem("Load PGN File", KeyEvent.VK_O);
        openPGN.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(Table.get().getGameFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                loadPGNFile(chooser.getSelectedFile());
            }
        });
        filesMenu.add(openPGN);

        final JMenuItem openFEN = new JMenuItem("Load FEN File", KeyEvent.VK_F);
        openFEN.addActionListener(e -> {
            String fenString = JOptionPane.showInputDialog("Input FEN");
            if(fenString != null) {
                undoAllMoves();
                chessBoard = FenUtilities.createGameFromFEN(fenString);
                Table.get().getBoardPanel().drawBoard(chessBoard);
            }
        });
        filesMenu.add(openFEN);

        final JMenuItem saveToPGN = new JMenuItem("Save Game", KeyEvent.VK_S);
        saveToPGN.addActionListener(e -> {
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
                @Override
                public String getDescription() {
                    return ".pgn";
                }
                @Override
                public boolean accept(final File file) {
                    return file.isDirectory() || file.getName().toLowerCase().endsWith("pgn");
                }
            });
            final int option = chooser.showSaveDialog(Table.get().getGameFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                savePGNFile(chooser.getSelectedFile());
            }
        });
        filesMenu.add(saveToPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(e -> {
            Table.get().getGameFrame().dispose();
            System.exit(0);
        });
        filesMenu.add(exitMenuItem);

         */

        return filesMenu;
    }

    private JMenu createOptionsMenu() {

        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);

        /*
        final JMenuItem resetMenuItem = new JMenuItem("New Game", KeyEvent.VK_P);
        resetMenuItem.addActionListener(e -> undoAllMoves());
        optionsMenu.add(resetMenuItem);

        final JMenuItem evaluateBoardMenuItem = new JMenuItem("Evaluate Board", KeyEvent.VK_E);
        evaluateBoardMenuItem.addActionListener(e -> System.out.println(StandardBoardEvaluator.get().evaluationDetails(chessBoard, gameSetup.getSearchDepth())));
        optionsMenu.add(evaluateBoardMenuItem);

        final JMenuItem escapeAnalysis = new JMenuItem("Escape Analysis Score", KeyEvent.VK_S);
        escapeAnalysis.addActionListener(e -> {
            final Move lastMove = moveLog.getMoves().get(moveLog.size() - 1);
            if(lastMove != null) {
                System.out.println(MoveUtils.exchangeScore(lastMove));
            }

        });
        optionsMenu.add(escapeAnalysis);

        final JMenuItem undoMoveMenuItem = new JMenuItem("Undo last move", KeyEvent.VK_M);
        undoMoveMenuItem.addActionListener(e -> {
            if(Table.get().getMoveLog().size() > 0) {
                undoLastMove();
            }
        });
        optionsMenu.add(undoMoveMenuItem);
         */

        final JMenuItem legalMovesMenuItem = new JMenuItem("Current State", KeyEvent.VK_L);
        legalMovesMenuItem.addActionListener(e -> {
            System.out.println(chessBoard.getWhitePieces());
            System.out.println(chessBoard.getBlackPieces());
            System.out.println(playerInfo(chessBoard.currentPlayer()));
            System.out.println(playerInfo(chessBoard.currentPlayer().getOpponent()));
        });
        optionsMenu.add(legalMovesMenuItem);

        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game", KeyEvent.VK_S);
        setupGameMenuItem.addActionListener(e -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);

        return optionsMenu;
    }

    private JMenu createPreferencesMenu() {

        final JMenu preferencesMenu = new JMenu("Preferences");

        final JMenu colorChooserSubMenu = new JMenu("Choose Colors");
        colorChooserSubMenu.setMnemonic(KeyEvent.VK_S);

        final JMenuItem chooseDarkMenuItem = new JMenuItem("Choose Dark Tile Color");
        colorChooserSubMenu.add(chooseDarkMenuItem);

        final JMenuItem chooseLightMenuItem = new JMenuItem("Choose Light Tile Color");
        colorChooserSubMenu.add(chooseLightMenuItem);

        final JMenuItem chooseLegalHighlightMenuItem = new JMenuItem(
                "Choose Legal Move Highlight Color");
        colorChooserSubMenu.add(chooseLegalHighlightMenuItem);

        preferencesMenu.add(colorChooserSubMenu);

        chooseDarkMenuItem.addActionListener(e -> {
            final Color colorChoice = JColorChooser.showDialog(Table.get().getGameFrame(), "Choose Dark Tile Color",
                    Table.get().getGameFrame().getBackground());
            if (colorChoice != null) {
                Table.get().getBoardPanel().setTileDarkColor(chessBoard, colorChoice);
            }
        });

        chooseLightMenuItem.addActionListener(e -> {
            final Color colorChoice = JColorChooser.showDialog(Table.get().getGameFrame(), "Choose Light Tile Color",
                    Table.get().getGameFrame().getBackground());
            if (colorChoice != null) {
                Table.get().getBoardPanel().setTileLightColor(chessBoard, colorChoice);
            }
        });

        final JMenu chessMenChoiceSubMenu = new JMenu("Choose Chess Men Image Set");

        final JMenuItem holyWarriorsMenuItem = new JMenuItem("Holy Warriors");
        chessMenChoiceSubMenu.add(holyWarriorsMenuItem);

        final JMenuItem rockMenMenuItem = new JMenuItem("Rock Men");
        chessMenChoiceSubMenu.add(rockMenMenuItem);

        final JMenuItem abstractMenMenuItem = new JMenuItem("Abstract Men");
        chessMenChoiceSubMenu.add(abstractMenMenuItem);

        final JMenuItem woodMenMenuItem = new JMenuItem("Wood Men");
        chessMenChoiceSubMenu.add(woodMenMenuItem);

        final JMenuItem fancyMenMenuItem = new JMenuItem("Fancy Men");
        chessMenChoiceSubMenu.add(fancyMenMenuItem);

        final JMenuItem fancyMenMenuItem2 = new JMenuItem("Fancy Men 2");
        chessMenChoiceSubMenu.add(fancyMenMenuItem2);

        woodMenMenuItem.addActionListener(e -> {
            System.out.println("implement me");
            Table.get().getGameFrame().repaint();
        });

        holyWarriorsMenuItem.addActionListener(e -> {
            pieceIconPath = "art/holywarriors/";
            Table.get().getBoardPanel().drawBoard(chessBoard);
        });

        rockMenMenuItem.addActionListener(e -> {
        });

        abstractMenMenuItem.addActionListener(e -> {
            pieceIconPath = "art/simple/";
            Table.get().getBoardPanel().drawBoard(chessBoard);
        });

        fancyMenMenuItem2.addActionListener(e -> {
            pieceIconPath = "art/fancy2/";
            Table.get().getBoardPanel().drawBoard(chessBoard);
        });

        fancyMenMenuItem.addActionListener(e -> {
            pieceIconPath = "art/fancy/";
            Table.get().getBoardPanel().drawBoard(chessBoard);
        });

        preferencesMenu.add(chessMenChoiceSubMenu);

        chooseLegalHighlightMenuItem.addActionListener(e -> {
            final Color colorChoice = JColorChooser.showDialog(Table.get().getGameFrame(), "Choose Legal Move Highlight Color",
                    Table.get().getGameFrame().getBackground());
            Table.get().getGameFrame().repaint();
        });

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip board");

        flipBoardMenuItem.addActionListener(e -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });

        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();


        final JCheckBoxMenuItem cbLegalMoveHighlighter = new JCheckBoxMenuItem(
                "Highlight Legal Moves", false);

        cbLegalMoveHighlighter.addActionListener(e -> highlightLegalMoves = cbLegalMoveHighlighter.isSelected());

        preferencesMenu.add(cbLegalMoveHighlighter);

        final JCheckBoxMenuItem cbUseBookMoves = new JCheckBoxMenuItem(
                "Use Book Moves", false);

        //cbUseBookMoves.addActionListener(e -> useBook = cbUseBookMoves.isSelected());

        preferencesMenu.add(cbUseBookMoves);

        return preferencesMenu;

    }

    private JFrame getGameFrame() {
        return this.gameFrame;
    }

    private static String playerInfo(final Player player) {
        return ("Player is: " +player.getAlliance() + "\nlegal moves (" +player.getLegalMoves().size()+ ") = " +player.getLegalMoves() + "\ninCheck = " +
                player.isInCheck() + "\nisInCheckMate = " +player.isInCheckMate() +
                "\nisCastled = " +player.isCastled())+ "\n";
    }

    /*
    private void undoAllMoves() {
        for(int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) {
            final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
            this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        }
        this.computerMove = null;
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
        //Table.get().getDebugPanel().redo();
    }
    private void undoLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.chessBoard = this.chessBoard.currentPlayer().unMakeMove(lastMove).getToBoard();
        this.computerMove = null;
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
        //Table.get().getDebugPanel().redo();
    }

     */

    private void setupUpdate(final GameSetup gameSetup) {
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher
            implements Observer {

        @Override
        public void update(final Observable o,
                           final Object arg) {

            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
                    !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
                    !Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                System.out.println(Table.get().getGameBoard().currentPlayer() + " is set to AI, thinking....");
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().currentPlayer() + " is in stalemate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }

    }

    /**
     * AIThinkTank is a SwingWorker class that is used to calculate the best move for the AI player.
     * This class is used to prevent the GUI from freezing while the AI is calculating the best move.
     * The AI is implemented using the Minimax algorithm.
     *
     */
    private static class AIThinkTank extends SwingWorker<Move, String> {

        /**
         * The AI player.
         */
        private AIThinkTank() {
        }

        @Override
        protected Move doInBackground() {
            /*
            final Move bookMove = Table.get().getUseBook()
                    ? MySqlGamePersistence.get().getNextBestMove(Table.get().getGameBoard(),
                    Table.get().getGameBoard().currentPlayer(),
                    Table.get().getMoveLog().getMoves().toString().replaceAll("\\[", "").replaceAll("]", ""))
                    : MoveFactory.getNullMove();
            if (Table.get().getUseBook() && bookMove != MoveFactory.getNullMove()) {
                bestMove = bookMove;
            }
            else {
                final StockAlphaBeta strategy = new StockAlphaBeta(Table.get().getGameSetup().getSearchDepth());
                strategy.addObserver(Table.get().getDebugPanel());
                bestMove = strategy.execute(Table.get().getGameBoard());
            }

             */

            /* Sets the best move to the move returned by the minimax algorithm. */
            final MoveStrategy AlphaBetaWithMoveOrderOrdering = new AlphaBetaWithMoveOrdering(Table.get().getGameSetup().getSearchDepth(), 1500);
            final Move bestMove = AlphaBetaWithMoveOrderOrdering.execute(Table.get().getGameBoard());
            return bestMove;
        }

        @Override
        public void done() {
            try {
                // get() is a method of SwingWorker that returns the value returned by doInBackground()
                final Move bestMove = get();
                // The best move is set to the computer move.
                Table.get().updateComputerMove(bestMove);
                // The move is made on the board.
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                // The move is added to the move log.
                Table.get().getMoveLog().addMove(bestMove);
                // The GUI is updated.
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                // The GUI is updated.
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                //Table.get().getDebugPanel().redo();
                // Updates the GUI to show the current player.
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateGameBoard(final Board board) {
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move) {
        this.computerMove = move;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private GameHistoryPanel getGameHistoryPanel() {
        return this.gameHistoryPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }

    private void setupNewGame() {
        this.chessBoard = Board.createStandardBoard(); // Creates a new chess board
        this.boardPanel.drawBoard(this.chessBoard); // Draws the board
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

        /**
         * Draws the board
         *
         * @param board the board to draw
         */
        public void drawBoard(final Board board) {
            removeAll(); // Remove all the tiles from the board
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) { // Loop through all the tiles
                tilePanel.drawTile(board); // Draw the tile
                add(tilePanel); // Add the tile to the board
            }
            validate(); // Validate the board
            repaint(); // Repaint the board
        }

        /**
         * Sets the color of the dark tiles
         *
         * @param chessBoard the board to set the color of the dark tiles
         * @param colorChoice the color to set the dark tiles
         */
        public void setTileDarkColor(Board chessBoard, Color colorChoice) {
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.setTileDarkColor(chessBoard, colorChoice);
            }
        }

        /**
         * Sets the color of the light tiles
         *
         * @param chessBoard the board to set the color of the light tiles
         * @param colorChoice the color to set the light tiles
         */
        public void setTileLightColor(Board chessBoard, Color colorChoice) {
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.setTileLightColor(chessBoard, colorChoice);
            }
        }
    }

    public static class MoveLog {

        // List of all the moves made
        private final List<Move> moves;

        /**
         * Constructor for the MoveLog class
         */
        MoveLog() {
            this.moves = new ArrayList<>();
        }

        /**
         * Returns the list of moves made
         *
         * @return the list of moves made
         */
        public List<Move> getMoves() {
            return this.moves;
        }

        /**
         * Adds a move to the list of moves made
         *
         * @param move the move to add to the list of moves made
         */
        public void addMove(final Move move) {
            this.moves.add(move);
        }

        /**
         * Returns the number of moves made
         *
         * @return the number of moves made
         */
        public int size() {
            return this.moves.size();
        }

        /**
         * Clears the list of moves made
         */
        public void clear() {
            this.moves.clear();
        }

        /**
         * Removes the last move made
         *
         * @return the last move made
         */
        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        /**
         * Returns the last move made
         *
         * @return the last move made
         */
        public boolean removeMove(final Move move) {
            return this.moves.remove(move);
        }
    }

    /**
     * Player Type Enum
     *
     */
    public enum PlayerType {
        HUMAN,
        COMPUTER
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

                    if (isRightMouseButton(event)) { // If the right mouse button is clicked
                        sourceTile = null; // Set the source tile to null
                        humanMovedPiece = null; // Set the human moved piece to null
                        destinationTile = null; // Set the destination tile to null
                        //Testing
                        System.out.println("Right Clicked, Resetting");

                    } else if (isLeftMouseButton(event)) { // If the left mouse button is clicked
                        // For the first click
                        if (sourceTile == null) { // If the source tile is null (no piece has been selected)
                            sourceTile = chessBoard.getTile(tileId); // Set the source tile to the tile that was clicked
                            humanMovedPiece = sourceTile.getPiece(); // Set the human moved piece to the piece on the source tile
                            if (humanMovedPiece == null) { // If the human moved piece is null (no piece was selected/ empty square was clicked)
                                sourceTile = null; // Set the source tile to null
                            }

                        //Testing
                            assert sourceTile != null;
                            System.out.println("First Tile Selected: Piece:" + humanMovedPiece + " Tile:" + sourceTile.getTileCoordinate());

                        }
                        // Second click
                        else { // If the source tile is not null (a piece has been selected)
                            destinationTile = chessBoard.getTile(tileId); // Set the destination tile to the tile that was clicked
                            if(tileId == 28 || tileId == 20){
                                int x = 0;
                            }
                            final Move move = Move.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate()); // Create a move from the source tile to the destination tile
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move); // Make the move
                            if (transition.getMoveStatus().isDone()) { // If the move was successful
                                chessBoard = transition.getTransitionBoard(); // Set the chess board to the transition board
                                moveLog.addMove(move); // Add the move to the move log
                            }
                            sourceTile = null; // Set the source tile to null
                            humanMovedPiece = null; // Set the human moved piece to null
                            destinationTile = null; // Set the destination tile to null
                        }

                        //Testing
                        System.out.println("Second Tile Selected: Piece:" + chessBoard.getTile(tileId) + " Tile: " + tileId); // Print the piece and tile that was clicked
                    }
                    // SwingUtilities.invokeLater() runs the code in the Runnable object on the main thread
                    invokeLater(() -> {

                        if(gameSetup.isAIPlayer(chessBoard.currentPlayer()) && !gameSetup.isAIPlayer(chessBoard.currentPlayer().getOpponent())) {
                            Table.get().moveMadeUpdate(PlayerType.HUMAN);
                        }

                        boardPanel.drawBoard(chessBoard); // Redraw the board
                        gameHistoryPanel.redo(chessBoard, moveLog); // Redraw the game history
                        takenPiecesPanel.redo(moveLog); // Redraw the taken pieces panel

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
                    final BufferedImage image = ImageIO.read(new File(pieceIconPath + // Get the image of the piece
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

        /** Sets the dark tile color
         *
         * @param chessBoard the board the tile is on
         * @param colorChoice the color to set the dark tile to
         */
        public void setTileDarkColor(Board chessBoard, Color colorChoice) {
            // If the tile is dark and the tile is not occupied
            if (chessBoard.getTile(this.tileId).isTileOccupied()) {
                setBackground(colorChoice);
            }
        }

        /** Sets the light tile color
         *
         * @param chessBoard the board the tile is on
         * @param colorChoice the color to set the light tile to
         */
        public void setTileLightColor(Board chessBoard, Color colorChoice) {
            // If this tile is occupied, set the color to the colorChoice
            if (chessBoard.getTile(this.tileId).isTileOccupied()) {
                setBackground(colorChoice);
            }
        }
    }
}
