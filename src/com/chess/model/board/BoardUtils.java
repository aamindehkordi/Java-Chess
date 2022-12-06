package com.chess.model.board;

import com.chess.model.pieces.King;
import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceType;

import java.util.*;

public class BoardUtils {


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;
    /* ROWS */
    public static final boolean[] FIRST_ROW = initRow(0);
    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] THIRD_ROW = initRow(16);
    public static final boolean[] FOURTH_ROW = initRow(24);
    public static final boolean[] FIFTH_ROW = initRow(32);
    public static final boolean[] SIXTH_ROW = initRow(40);
    public static final boolean[] SEVENTH_ROW = initRow(48);
    public static final boolean[] EIGHTH_ROW = initRow(56);

    /* COLUMNS */
    public static final boolean[] FIRST_COLUMN = initCol(0);
    public static final boolean[] SECOND_COLUMN = initCol(1);
    public static final boolean[] THIRD_COLUMN = initCol(2);
    public static final boolean[] FOURTH_COLUMN = initCol(3);
    public static final boolean[] FIFTH_COLUMN = initCol(4);
    public static final boolean[] SIXTH_COLUMN = initCol(5);
    public static final boolean[] SEVENTH_COLUMN = initCol(6);
    public static final boolean[] EIGHTH_COLUMN = initCol(7);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static String[] initializeAlgebraicNotation() {
        return new String[] {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    /**
     * This method is used to initialize the columns of the board.
     * @param colNum the column number
     * @return boolean array
     */
    private static boolean[] initCol(int colNum) {
        final boolean[] column = new boolean[NUM_TILES];
        /*
         * The following loop is used to initialize the columns of the board.
         * The loop starts at the column number and increments by 8.
         * The loop ends when the column number is greater than 63.
         */
        do {
            column[colNum] = true;
            colNum += NUM_TILES_PER_ROW;
        } while (colNum < NUM_TILES);
        return column;
    }

    /**
     * This method is used to initialize the rows of the board.
     * @param rowNum the row number
     * @return boolean array
     */
    private static boolean[] initRow(int rowNum) {
        final boolean[] row = new boolean[NUM_TILES];
        /*
         * The following loop is used to initialize the rows of the board.
         * The loop starts at the row number and increments by 1.
         * The loop ends when the row number is greater than 7.
         */
        do {
            row[rowNum] = true;
            rowNum++;
        } while (rowNum % NUM_TILES_PER_ROW != 0);
        return row;
    }

    /** Constructor
     * <p>
     * DO NOT CONSTRUCT AN INSTANCE OF THIS CLASS
     * <p>
     * BoardUtils is used to store constants and static methods
     * and should not be instantiated.
     */
    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    /** This method is used to check if the given coordinate is valid.
     *
     * @param coord the coordinate
     * @return true if the coordinate is valid, false otherwise
     */
    public static boolean isValidTileCoordinate(final int coord) {
        return coord >= 0 && coord < NUM_TILES;
    }

    /** Gets the coordinate of the given position.
     *
     * @param position the position
     * @return the coordinate
     */
    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    /** Gets the position of the given coordinate.
     *
     * @param coordinate the coordinate
     * @return the position
     */
    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION[coordinate];
    }

    /** The King Pawn Trap is a chess opening in which a player sacrifices a pawn
     * in order to trap the opponent's king in the center of the board.
     *
     * @param board the board
     * @param king the king
     * @param frontTile the front tile
     * @ return true if the king is in the King Pawn Trap, false otherwise
     */
    public static boolean isKingPawnTrap(final Board board,
                                         final King king,
                                         final int frontTile) {
        // Create the piece on the front tile
        final Piece piece = board.getTile(frontTile).getPiece();
        // return true if the piece is a pawn and the piece is the same color as the king
        return piece != null &&
                piece.getPieceType() == PieceType.PAWN &&
                piece.getPieceAlliance() != king.getPieceAlliance();
    }

    public static int mvvlva(final Move move) {
        final Piece movingPiece = move.getMovedPiece();
        if(move.isAttack()) {
            final Piece attackedPiece = move.getAttackedPiece();
            return (attackedPiece.getPieceValue() - movingPiece.getPieceValue() +  PieceType.KING.getPieceValue()) * 100;
        }
        return PieceType.KING.getPieceValue() - movingPiece.getPieceValue();
    }

    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while(currentMove != Move.MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

    public static boolean isEndGame(final Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }

    public static boolean kingThreat(Move move) {
        return move.isAttack() && move.getAttackedPiece().getPieceType() == PieceType.KING;
    }
}
