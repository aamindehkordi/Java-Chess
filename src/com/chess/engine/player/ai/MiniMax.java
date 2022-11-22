package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{
    

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private long boardsEvaluated;


    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
        this.boardsEvaluated = 0;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis(); //Start to measure the time it takes to execute the algorithm

        Move bestMove = null; //The best move to be returned

        int highestSeenValue = Integer.MIN_VALUE; //The highest value seen so far
        int lowestSeenValue = Integer.MAX_VALUE; //The lowest value seen so far

        int currentValue; //The current value of the move being evaluated

        int numMoves = board.currentPlayer().getLegalMoves().size(); //The number of legal moves

        int moveCounter = 1; //The number of moves evaluated so far

        System.out.println(board.currentPlayer() + " THINKING with searchDepth = " + this.searchDepth);

        for(final Move move : board.currentPlayer().getLegalMoves()) { //For each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); //Make the move
            boardsEvaluated++; //Increment the number of boards evaluated
            if(moveTransition.getMoveStatus().isDone()) { //If the move is legal
                currentValue = board.currentPlayer().getAlliance().isWhite() ? //If the current player is white
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1) : //Call min
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1); // Else if the player is black, Call max
                if(board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) { //If the current player is white AND the current value is greater than the highest value seen so far
                    highestSeenValue = currentValue; //Set the highest value seen so far to the current value
                    bestMove = move; //Set the best move to the current move
                } else if(board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) { //If the current player is black AND the current value is less than the lowest value seen so far
                    lowestSeenValue = currentValue; //Set the lowest value seen so far to the current value
                    bestMove = move; //Set the best move to the current move
                }
            }
            System.out.println("Move " + moveCounter + " of " + numMoves + " evaluated.");
            moveCounter++; //Increment the number of moves evaluated so far
        }

        final long executionTime = System.currentTimeMillis() - startTime; //Stop measuring the time it takes to execute the algorithm

        return bestMove; //Return the best move
    }

    @Override
    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }

    /**
     *  This method is the heart of the MiniMax algorithm. It is part of a corecursive pattern that
     *  will be used to traverse the game tree. The method will return the best move for the player
     *  that is passed in as a parameter. The searchDepth parameter is used to determine how many moves
     *  ahead the algorithm will look.
     *
     * @param board the board
     * @param searchDepth the searchDepth of the search
     * @return the lowest score found in the search of the game tree
     */
    public int min(final Board board, final int searchDepth) {

        if (searchDepth == 0|| isGameOver(board)) {
            return this.boardEvaluator.evaluate(board, searchDepth);
        }
        // lowest seen value is set to the highest possible value
        int lowestSeenValue = Integer.MAX_VALUE;
        for( final Move move : board.currentPlayer().getLegalMoves()) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            if(moveTransition.getMoveStatus().isDone()) { // if the move is done
                // recursively call max on the new board
                final int currentValue = max(moveTransition.getTransitionBoard(), searchDepth - 1); // get the max value of the board after the move
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
     * that is passed in as a parameter. The searchDepth parameter is used to determine how many moves
     * ahead the algorithm will look.
     *
     * @param board the board
     * @param searchDepth the searchDepth of the search
     * @return the highest score found in the search of the game tree
     */
    public int max(final Board board, final int searchDepth) {
        if (searchDepth == 0 || isGameOver(board)) {
            return this.boardEvaluator.evaluate(board, searchDepth);
        }
        // highest seen value is set to the lowest possible value
        int highestSeenValue = Integer.MIN_VALUE;
        for( final Move move : board.currentPlayer().getLegalMoves()) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            if(moveTransition.getMoveStatus().isDone()) { // if the move is done
                // recursively call min on the new board
                final int currentValue = min(moveTransition.getTransitionBoard(), searchDepth - 1); // get the min value of the board after the move
                if(currentValue >= highestSeenValue) { // if the current value is greater than the highest seen value
                    highestSeenValue = currentValue; // set the highest seen value to the current value
                }
            }
        }
        return highestSeenValue;
    }

    private boolean isGameOver(Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }


}
