package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {


    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    public static BoardEvaluator get() {
        return new StandardBoardEvaluator();
    }


    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) -
               scorePlayer(board, board.blackPlayer(), depth);
    }

    /**
     * This method returns the score of the player
     *
     * @param board The board
     * @param player The player
     * @param depth The depth
     * @return the score of the player
     */
    private int scorePlayer(Board board, Player player, int depth) {
        return pieceValues(player) +
                mobility(player) + check(player) +
                checkMate(player, depth) +
                castled(player);
    }

    /**
     * This method returns the score of the sum of the current player's pieces
     *
     * @param player The player
     * @return the score of the player's pieces
     */
    private int pieceValues(Player player) {
        return player.getActivePieces().stream().mapToInt(Piece::getPieceValue).sum(); // Sum the value of all pieces
    }

/**
     * This method returns the score of the player's mobility
     *
     * @param player The player
     * @return the score of the player's mobility
     */
    private int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    /**
     * This method returns the score of the player's check
     *
     * @param player The player
     * @return the score of the player's check
     */
    private int check(Player player) {
        return player.isInCheck() ? CHECK_BONUS : 0;
    }

    /**
     * This method returns the score of the player's checkmate
     *
     * @param player The player
     * @param depth The depth
     * @return the score of the player's checkmate
     */
    private int checkMate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    /**
     * This method returns the score of the player's castling
     *
     * @param player The player
     * @return the score of the player's castling
     */
    private int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    /**
     * This method returns the depth bonus
     *
     * @param depth The depth
     * @return the depth bonus
     */
    private int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }



}
