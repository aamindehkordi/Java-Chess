package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

import java.util.*;

/**
 * This class is used to analyze the rook structure of a player.
 */
public final class RookStructureAnalyzer {
//TODO FIX

    /**
     * This method is used to get instance of the RookStructureAnalyzer.
     */
    private static final RookStructureAnalyzer INSTANCE = new RookStructureAnalyzer();

    /**
     * This method initializes board culumns for the rook structure.
     */
    private static final  List<boolean[]> BOARD_COLUMNS = initColumns();

    /**
     * This method initializes the bonus for open culumns and sets it to 25.
     */
    private static final int OPEN_COLUMN_ROOK_BONUS = 25;

    /**
     * This method initializes the no bonus and sets it to 0.
     */
    private static final int NO_BONUS = 0;


    private RookStructureAnalyzer() {               //singleton
    }

    /**
     * This method is used to get the instance of the RookStructureAnalyzer.
     * @return the instance of the RookStructureAnalyzer.
     */
    public static RookStructureAnalyzer get() {
        return INSTANCE;
    }

    /**
     * This method is used to initialize the board columns.
     * @return the board columns.
     */
    private static  List<boolean[]> initColumns() {
        final List<boolean[]> columns = new ArrayList<>();                  //initialize columns
        columns.add(BoardUtils.FIRST_COLUMN);                               //add first column
        columns.add(BoardUtils.SECOND_COLUMN);                              //add second column
        columns.add(BoardUtils.THIRD_COLUMN);
        columns.add(BoardUtils.FOURTH_COLUMN);
        columns.add(BoardUtils.FIFTH_COLUMN);                               //...
        columns.add(BoardUtils.SIXTH_COLUMN);
        columns.add(BoardUtils.SEVENTH_COLUMN);
        columns.add(BoardUtils.EIGHTH_COLUMN);
        return Collections.unmodifiableList(new LinkedList<>(columns));     //return columns
    }

    /**
     * This method is used to get the rook structure score.
     * @param player the player.
     * @return the rook structure score.
     */
    public int rookStructureScore(final Player player) {
        final int[] rookOnColumnTable = createRookColumnTable(calculatePlayerRooks(player));    //create rook column table
        return calculateOpenFileRookBonus(rookOnColumnTable);                                   //return rook structure score
    }

    /**
     * This method is used create a table of rooks on columns.
     * @param playerRooks the player.
     * @return ta table of rooks on columns.
     */
    private static int[] createRookColumnTable(final Collection<Piece> playerRooks) {
        final int[] table = new int[8];                                         //create table
        for(final Piece playerRook : playerRooks) {                             //for each player rook
            table[playerRook.getPiecePosition() % 8]++;                         //add rook to table
        }
        return table;                                                           //return table
    }

    /**
     * This method is used to return the rooks on the board.
     * @param player the player.
     * @return the rooks on the board.
     */
    private static Collection<Piece> calculatePlayerRooks(final Player player) {
        final List<Piece> playerRooks = new ArrayList<>();                          //initialize player rooks
        for(final Piece piece : player.getActivePieces()) {                         //for each piece
            if(piece.getPieceType().isRook()) {                                     //if piece is a rook
                playerRooks.add(piece);                                             //add rook to player rooks
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(playerRooks));         //return player rooks
    }

    /**
     * This method is used to calculate the open file rook bonus.
     * @param rookOnColumnTable the table of rooks on columns.
     * @return the open file rook bonus.
     */
    private static int calculateOpenFileRookBonus(final int[] rookOnColumnTable) {
        int bonus = NO_BONUS;                                                       //initialize bonus
        for(final int rookLocation : rookOnColumnTable) {                           //for each rook location
            final int[] piecesOnColumn = rookOnColumnTable;                         //initialize pieces on column
            final int rookColumn = rookLocation/8;                                  //get rook column
            for(int i = 0; i < piecesOnColumn.length; i++) {                        //for each piece on column
                if(piecesOnColumn[i] == 1 && i == rookColumn){                      //if piece is on column and is a rook
                    bonus += OPEN_COLUMN_ROOK_BONUS;                                //add bonus
                }

            }
        }
        return bonus;                                                               //return bonus
    }

    /**
     * This method is used to create a table of pieces on columns.
     * @param board the board.
     * @return the table of pieces on columns.
     */
    private static int[] createPiecesOnColumnTable(final Board board) {         //not used so I am not going to comment
        final int[] piecesOnColumnTable = new int[BOARD_COLUMNS.size()];
        for(final Piece piece : board.getAllPieces()) {
            for(int i = 0 ; i < BOARD_COLUMNS.size(); i++) {
                if(BOARD_COLUMNS.get(i)[piece.getPiecePosition()]) {
                    piecesOnColumnTable[i]++;
                }
            }
        }
        return piecesOnColumnTable;
    }


}