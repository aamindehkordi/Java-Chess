package com.chess.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public interface MoveStrategy {

    /**
     * This method returns the best move for the player
     *
     * @param board The board
     * @return the move
     */
    Move execute(Board board);

    /**
     * This method returns the name of the strategy
     *
     * @return the name of the strategy
     */
    String toString();

    /**
     * This method returns the number of boards evaluated
     *
     * @return the number of boards evaluated
     */
    long getNumBoardsEvaluated();
}
