package com.chess.engine.player.ai;

import com.chess.engine.board.Board;

public interface BoardEvaluator {

    /**
     * This method returns the score of the board
     *
     * @param board The board
     * @return the score of the board
     */
    int evaluate(Board board, int depth);

}
