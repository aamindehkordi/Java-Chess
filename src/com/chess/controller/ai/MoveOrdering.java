package com.chess.controller.ai;


import com.chess.model.board.Board;
import com.chess.model.board.Move;
import com.chess.controller.MoveTransition;
import com.chess.controller.Player;

import java.util.*;

/**
 * This class is used to order the moves in the MoveStrategy
 */
public final class MoveOrdering {

    /**
     * This is the board evaluator used to evaluate the boards
     */
    private final BoardEvaluator evaluator;

    /**
     * This is the constructor for the MoveOrdering class
     */
    private static final MoveOrdering INSTANCE = new MoveOrdering();

    /**
     * This is the number for order search depth
     */
    private static final int ORDER_SEARCH_DEPTH = 2;

    /**
     * This is the constructor for the MoveOrdering class
     */
    private MoveOrdering() {
        this.evaluator = StandardBoardEvaluator.get();
    }

    /**
     * This method returns the instance of the MoveOrdering class
     */
    public static MoveOrdering get() {
        return INSTANCE;
    }

    /**
     * This method returns the best move for the player
     */
    public List<Move> orderMoves(final Board board) {
        return orderImpl(board, ORDER_SEARCH_DEPTH);
    }

    /**
     * This class is used to order the moves in the MoveStrategy
     */
    private static class MoveOrderEntry {
        final Move move;                        // The move
        final int score;                        // The score

        /**
         * @param move  The move
         * @param score The score
         * This method is the constructor for the MoveOrderEntry class
         */
        MoveOrderEntry(final Move move,
                       final int score) {
            this.move = move;                    // Set the move
            this.score = score;                  // Set the score
        }

        /**
         * This method returns the move
         */
        final Move getMove() {
            return this.move;
        }

        /**
         * This method returns the score
         */
        final int getScore() {
            return this.score;
        }

        @Override
        public String toString() {                                      // Override the toString method
            return "move = " +this.move+ " score = " +this.score;       // Return the string
        }
    }

    /**
     * This method returns the best move for the player
     */
    private List<Move> orderImpl(final Board board,
                                 final int depth) {
        final List<MoveOrderEntry> moveOrderEntries = new ArrayList<>();                    // Create the list of move order entries
        for (final Move move : board.currentPlayer().getLegalMoves()) {                     // Iterate through the legal moves
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);     // Make the move
            if (moveTransition.getMoveStatus().isDone()) {                                  // If the move is done
                final int attackBonus = calculateAttackBonus(board.currentPlayer(), move);  // Calculate the attack bonus
                final int currentValue = attackBonus + (board.currentPlayer().getAlliance().isWhite() ? // Calculate the current value
                        min(moveTransition.getTransitionBoard(), depth - 1) :         // Return the min value
                        max(moveTransition.getTransitionBoard(), depth - 1));         // Return the max value
                moveOrderEntries.add(new MoveOrderEntry(move, currentValue));               // Add the move order entry
            }
        }
        Collections.sort(moveOrderEntries, new Comparator<MoveOrderEntry>() {               // Sort the move order entries
            @Override
            public int compare(final MoveOrderEntry o1, final MoveOrderEntry o2) {          // Override the compare method
                return o2.getScore() - o1.getScore();                                       // Return the score
            }
        });
        final List<Move> orderedMoves = new ArrayList<>();                                  // Create the list of ordered moves
        for(final MoveOrderEntry entry : moveOrderEntries) {                                // Iterate through the move order entries
            orderedMoves.add(entry.getMove());                                              // Add the move
        }

        return Collections.unmodifiableList(new LinkedList<>(orderedMoves));                // Return the list of ordered moves
    }

    /**
     * This calculates the attack bonus
     * @param player The player
     * @param move The move
     */
    private int calculateAttackBonus(final Player player,
                                     final Move move) {
        final int attackBonus = move.isAttack() ? 1000 : 0;                                  // Set the attack bonus
        return attackBonus * (player.getAlliance().isWhite() ? 1 : -1);                      // Return the attack bonus
    }

    /**
     * This method returns the min value
     */
    private static Collection<Move> calculateSimpleMoveOrder(final Collection<Move> moves) {
        final List<Move> sortedMoves = new ArrayList<>();                                     // Create the list of sorted moves
        sortedMoves.addAll(moves);                                                            // Add the moves
        Collections.sort(sortedMoves, new Comparator<Move>() {
            @Override
            public int compare(final Move o1, final Move o2) {                                // Override the compare method
                return o1.toString().compareTo(o2.toString());                                // Return the string
            }
        });
        return sortedMoves;                                                                   // Return the sorted moves
    }

    /**
     * This method returns the min value
     * @param board The board
     * @param depth The depth
     */
    public int min(final Board board,
                   final int depth) {
        if(depth == 0 || isEndGameScenario(board)) {              // If the depth is 0 or the end game scenario
            return this.evaluator.evaluate(board, depth);         // Return the evaluation
        }
        int lowestSeenValue = Integer.MAX_VALUE;                  // Set the lowest seen value
        for (final Move move : calculateSimpleMoveOrder(board.currentPlayer().getLegalMoves())) {     // Iterate through the legal moves
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);               // Make the move
            if (moveTransition.getMoveStatus().isDone()) {                                            // If the move is done
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);   // Calculate the current value
                if (currentValue <= lowestSeenValue) {                                                // If the current value is less than or equal to the lowest seen value
                    lowestSeenValue = currentValue;                                                   // Set the lowest seen value
                }
            }
        }
        return lowestSeenValue;                                                                        // Return the lowest seen value
    }

    /**
     * This method returns the max value
     * @param board The board
     * @param depth The depth
     */
    public int max(final Board board,
                   final int depth) {
        if(depth == 0 || isEndGameScenario(board)) {                                                    // If the depth is 0 or the end game scenario
            return this.evaluator.evaluate(board, depth);                                               // Return the evaluation
        }
        int highestSeenValue = Integer.MIN_VALUE;                                                       // Set the highest seen value
        for (final Move move : calculateSimpleMoveOrder(board.currentPlayer().getLegalMoves())) {       // Iterate through the legal moves
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);                 // Make the move
            if (moveTransition.getMoveStatus().isDone()) {                                              // If the move is done
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);     // Calculate the current value
                if (currentValue >= highestSeenValue) {                                                 // If the current value is greater than or equal to the highest seen value
                    highestSeenValue = currentValue;                                                    // Set the highest seen value
                }
            }
        }
        return highestSeenValue;                                                                        // Return the highest seen value
    }

    /**
     * This boolean returns true if the end game scenario
     */
    private static boolean isEndGameScenario(final Board board) {
        return  board.currentPlayer().isInCheckMate() ||        // If the current player is in checkmate
                board.currentPlayer().isInStaleMate();          // If the current player is in stalemate
    }

}
