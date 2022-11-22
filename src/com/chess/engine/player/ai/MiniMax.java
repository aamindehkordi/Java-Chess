package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{

    private final BoardEvaluator boardEvaluator;


    public MiniMax() {
        this.boardEvaluator = null;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board, int depth) {
        return null;
    }

    /**
     *  This method is the heart of the MiniMax algorithm. It is part of a corecursive pattern that
     *  will be used to traverse the game tree. The method will return the best move for the player
     *  that is passed in as a parameter. The depth parameter is used to determine how many moves
     *  ahead the algorithm will look.
     *
     * @param board the board
     * @param depth the depth of the search
     * @return the lowest score found in the search of the game tree
     */
    public int min(final Board board, final int depth) {

        if (depth == 0/* || isGameOver(board)*/) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        // lowest seen value is set to the highest possible value
        int lowestSeenValue = Integer.MAX_VALUE;
        for( final Move move : board.currentPlayer().getLegalMoves()) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            if(moveTransition.getMoveStatus().isDone()) { // if the move is done
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1); // get the max value of the board after the move
                if(currentValue <= lowestSeenValue) { // if the current value is less than the lowest seen value
                    lowestSeenValue = currentValue; // set the lowest seen value to the current value
                }
            }
        }
        return lowestSeenValue;
    }

    /**
     * This method is the heart of the MiniMax algorithm. It is part of a corecursive pattern that
     * will be used to traverse the game tree. The method will return the best move for the player
     * that is passed in as a parameter. The depth parameter is used to determine how many moves
     * ahead the algorithm will look.
     *
     * @param board the board
     * @param depth the depth of the search
     * @return the highest score found in the search of the game tree
     */
    public int max(final Board board, final int depth) {
        if (depth == 0/* || isGameOver(board)*/) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        // highest seen value is set to the lowest possible value
        int highestSeenValue = Integer.MIN_VALUE;
        for( final Move move : board.currentPlayer().getLegalMoves()) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            if(moveTransition.getMoveStatus().isDone()) { // if the move is done
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1); // get the min value of the board after the move
                if(currentValue >= highestSeenValue) { // if the current value is greater than the highest seen value
                    highestSeenValue = currentValue; // set the highest seen value to the current value
                }


            }
        }
        return highestSeenValue;
    }



}
