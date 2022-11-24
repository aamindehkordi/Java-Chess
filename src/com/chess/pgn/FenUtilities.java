package com.chess.pgn;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.pieces.Pawn;

public class FenUtilities {

    private FenUtilities() {
        throw new RuntimeException("You cannot instantiate me!");
    }

    public static Board createGameFromFen(final String fen) {
        return null;
    }

    public static String createFenFromGame(final Board board) {
        return calculateBoardText(board) + " " + calculateCurrentPlayerText(board) + " " +
                calculateCastlingAvailabilityText(board) + " " + calculateEnPassantSquare(board) + " "
                + calculateMoveNumber(board);
    }

    private static String calculateMoveNumber(Board board) {
        return String.valueOf(board.currentPlayer().getOpponent().getMoveNumber());
    }

    private static String calculateBoardText(Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = board.getTile(i).toString();
            builder.append(tileText);
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
                builder.append("/");
            }
        }
        return builder.toString().replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }

    private static String calculateEnPassantSquare(Board board) {
        final Pawn enPassantPawn = board.getEnPassantPawn();
        if(enPassantPawn != null) {
            return BoardUtils.getPositionAtCoordinate(enPassantPawn.getPiecePosition() +
                    (8) * enPassantPawn.getPieceAlliance().getOppositeDirection());
        }
        return "-";
    }

    private static String calculateCastlingAvailabilityText(Board board) {
        final StringBuilder builder = new StringBuilder();
        if (board.whitePlayer().getPlayerKing().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if (board.whitePlayer().getPlayerKing().isQueenSideCastleCapable()) {
            builder.append("Q");
        }
        if (board.blackPlayer().getPlayerKing().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if (board.blackPlayer().getPlayerKing().isQueenSideCastleCapable()) {
            builder.append("q");
        }
        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateCurrentPlayerText(Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }


}
