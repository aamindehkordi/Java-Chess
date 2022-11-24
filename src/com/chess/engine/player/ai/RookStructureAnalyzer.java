package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

import java.util.*;

public final class RookStructureAnalyzer {

    private static final RookStructureAnalyzer INSTANCE = new RookStructureAnalyzer();
    private static final  List<boolean[]> BOARD_COLUMNS = initColumns();
    private static final int OPEN_COLUMN_ROOK_BONUS = 25;
    private static final int NO_BONUS = 0;

    private RookStructureAnalyzer() {
    }

    public static RookStructureAnalyzer get() {
        return INSTANCE;
    }

    private static  List<boolean[]> initColumns() {
        final List<boolean[]> columns = new ArrayList<>();
        columns.add(BoardUtils.FIRST_COLUMN);
        columns.add(BoardUtils.SECOND_COLUMN);
        columns.add(BoardUtils.THIRD_COLUMN);
        columns.add(BoardUtils.FOURTH_COLUMN);
        columns.add(BoardUtils.FIFTH_COLUMN);
        columns.add(BoardUtils.SIXTH_COLUMN);
        columns.add(BoardUtils.SEVENTH_COLUMN);
        columns.add(BoardUtils.EIGHTH_COLUMN);
        return Collections.unmodifiableList(new LinkedList<>(columns));
    }

    public int rookStructureScore(final Player player) {
        final int[] rookOnColumnTable = createRookColumnTable(calculatePlayerRooks(player));
        return calculateOpenFileRookBonus(rookOnColumnTable);
    }

    private static int[] createRookColumnTable(final Collection<Piece> playerRooks) {
        final int[] table = new int[8];
        for(final Piece playerRook : playerRooks) {
            table[playerRook.getPiecePosition() % 8]++;
        }
        return table;
    }

    private static Collection<Piece> calculatePlayerRooks(final Player player) {
        final List<Piece> playerRooks = new ArrayList<>();
        for(final Piece piece : player.getActivePieces()) {
            if(piece.getPieceType().isRook()) {
                playerRooks.add(piece);
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(playerRooks));
    }

    private static int calculateOpenFileRookBonus(final int[] rookOnColumnTable) {
        int bonus = NO_BONUS;
        for(final int rookLocation : rookOnColumnTable) {
            final int[] piecesOnColumn = rookOnColumnTable;
            final int rookColumn = rookLocation/8;
            for(int i = 0; i < piecesOnColumn.length; i++) {
                if(piecesOnColumn[i] == 1 && i == rookColumn){
                    bonus += OPEN_COLUMN_ROOK_BONUS;
                }

            }
        }
        return bonus;
    }

    private static int[] createPiecesOnColumnTable(final Board board) {
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