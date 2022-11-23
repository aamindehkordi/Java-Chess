package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveStatus;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;

import java.util.Collection;

public class MiniMax implements MoveStrategy{
    

    /* The evaluation metric for the board. */
    private final BoardEvaluator boardEvaluator;
    /* The depth of the search tree. */
    private final int searchDepth;
    /* The number of boards evaluated. */
    private long boardsEvaluated;


    /** Constructor for MiniMax
     * @param searchDepth the depth of the search tree
     */
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

        Collection<Move> legalMoves = board.currentPlayer().getLegalMoves(); //The legal moves

        System.out.println(board.currentPlayer() + " THINKING with searchDepth = " + this.searchDepth);

        for(final Move move : legalMoves) { //For each legal move

            //Make the move on a copy of the board
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            //Get the board after the move has been made
            final Board tempBoard = moveTransition.getTransitionBoard();
            //Get the move status
            final MoveStatus moveStatus = moveTransition.getMoveStatus();


            if(moveStatus.isDone()) { //If the move was legal
                //Get the current player
                final Player currentPlayer = board.currentPlayer();

                currentValue = currentPlayer.getAlliance().isWhite() ? //If, the current player is white
                             min(tempBoard, this.searchDepth - 1) : //Call min, -1 because we have already made a move
                              max(tempBoard, this.searchDepth - 1); // Else if the player is black, Call max

                //If the current player is white AND the current value is greater than the highest value seen so far
                if(currentPlayer.getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue; //Set the highest value seen so far to the current value
                    bestMove = move; //Set the best move to the current move
                }
                //If the current player is black AND the current value is less than the lowest value seen so far
                else if(currentPlayer.getAlliance().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue; //Set the lowest value seen so far to the current value
                    bestMove = move; //Set the best move to the current move
                }
                else if(currentPlayer.getAlliance().isWhite() && currentValue < highestSeenValue) {
                    // Prune
                    System.out.println("Pruning move " + moveCounter + " of " + numMoves + " moves");
                }
                else if(currentPlayer.getAlliance().isBlack() && currentValue > lowestSeenValue) {
                    // Print out the move that was pruned or not evaluated
                    System.out.println("Pruning move " + moveCounter + " of " + numMoves + " moves");
                }
                else {
                    System.out.println("Error");
                }
            }
            moveCounter++; //Increment the number of moves evaluated so far
            System.out.println("Move " + moveCounter + " of " + numMoves + " evaluated.");
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
            this.boardsEvaluated++; // increment the number of boards evaluated
            return this.boardEvaluator.evaluate(board, searchDepth);
        }
        // lowest seen value is set to the highest possible value
        int lowestSeenValue = Integer.MAX_VALUE;
        // the legal moves
        final Collection<Move> legalMoves = board.currentPlayer().getLegalMoves();

        for( final Move move : legalMoves) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            final Board tempBoard = moveTransition.getTransitionBoard(); // get the board after the move has been made
            final MoveStatus moveStatus = moveTransition.getMoveStatus(); // get the move status
            if(moveStatus.isDone()) { // if the move is done
                // recursively call max on the new board
                final int currentValue = max(tempBoard, searchDepth - 1); // get the max value of the board after the move, -1 because we have already made a move
                if(currentValue <= lowestSeenValue) { // if the current value is less than the lowest seen value
                    lowestSeenValue = currentValue; // set the lowest seen value to the current value
                }
            }
        }
        // Print out boards evaluated
        //System.out.println("Boards evaluated: " + this.boardsEvaluated);
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
            boardsEvaluated++; // increment the number of boards evaluated
            return this.boardEvaluator.evaluate(board, searchDepth);
        }
        // highest seen value is set to the lowest possible value
        int highestSeenValue = Integer.MIN_VALUE;
        // the legal moves
        final Collection<Move> legalMoves = board.currentPlayer().getLegalMoves();

        for( final Move move : legalMoves) { // for each legal move
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move); // make the move
            final Board tempBoard = moveTransition.getTransitionBoard(); // get the board after the move has been made
            final MoveStatus moveStatus = moveTransition.getMoveStatus(); // get the move status

            if(moveStatus.isDone()) { // if the move is done
                // recursively call min on the new board
                final int currentValue = min(tempBoard, searchDepth - 1); // get the min value of the board after the move, -1 because we have already made a move
                if(currentValue >= highestSeenValue) { // if the current value is greater than the highest seen value
                    highestSeenValue = currentValue; // set the highest seen value to the current value
                }
            }
        }
        return highestSeenValue;
    }

    /**
     * This method checks to see if the game is over
     *
     * @param board the board
     * @return true if the game is over, false otherwise
     */
    private boolean isGameOver(Board board) {
        return board.currentPlayer().isInCheckMate() ||
                board.currentPlayer().isInStaleMate();
    }


}
