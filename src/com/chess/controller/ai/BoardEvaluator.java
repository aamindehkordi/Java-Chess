package com.chess.controller.ai;

import com.chess.model.board.Board;

public interface BoardEvaluator {

    /**
     * This method returns the score of the board
     *
     * @param board The board
     * @return the score of the board
     */
    int evaluate(Board board, int depth);

}
