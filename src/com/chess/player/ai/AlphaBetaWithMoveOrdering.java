package com.chess.player.ai;

import com.chess.player.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.player.MoveTransition;
import com.chess.player.Player;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;
import java.util.stream.Collectors;

import static com.chess.engine.board.BoardUtils.mvvlva;
/**
 * AI that uses the alpha-beta pruning algorithm with move ordering to determine the best move.
 */
public class AlphaBetaWithMoveOrdering extends Observable implements MoveStrategy {

    /**
     * The Evaluator is used to evaluate the board.
     */
    private final BoardEvaluator evaluator;

    /**
     * The search depth determines how many moves ahead the AI will look.
     */
    private final int searchDepth;

    /**
     * MoveSorter is used to sort the moves.
     */
    private final MoveSorter moveSorter;

    /**
     * quiescenceFactor is used to determine how many moves ahead the AI will look in quiescence search.
     */
    private final int quiescenceFactor;

    /**
     * The number of boards evaluated.
     */
    private long boardsEvaluated;

    /**
     * Execution time of the AI.
     */
    private long executionTime;

    /**
     * The quiescenceCount is a count of the number of quiescence searches performed.
     */
    private int quiescenceCount;

    /**
     * The cutOffsProduced is a count of the number of cut-offs produced.
     */
    private int cutOffsProduced;


    /**
     * Move sorter is an enum that determines how the moves are sorted.
     */
    private enum MoveSorter {

        /**
         * SORT is an enum that sorts the moves by the MVV/LVA heuristic.
         */
        SORT {

            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return moves.stream().sorted(SMART_SORT).collect(Collectors.toList());
            }
        };

        // Using standard java libraries
        /**
         * Comparator that sorts the moves by the MVV/LVA heuristic.
         */
        public static Comparator<Move> SMART_SORT = new Comparator<Move>() {
            @Override
                public int compare(final Move move1, final Move move2) {
                return (mvvlva(move2) - mvvlva(move1));
            }
        };

        /**
         * Sorts the moves.
         * @param moves the moves to be sorted
         * @return the sorted moves
         */
        abstract Collection<Move> sort(Collection<Move> moves);
    }

    /**
     * Constructor for the AlphaBetaWithMoveOrdering class.
     * @param searchDepth the depth to search to
     * @param quiescenceFactor the quiescence factor to use
     * queisenceFactor is the number of moves to search before using the quiescence search
     */
    public AlphaBetaWithMoveOrdering(final int searchDepth,
                                     final int quiescenceFactor) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.quiescenceFactor = quiescenceFactor; // Quiescence factor is the number of moves to search before using the quiescence search which is a search that only looks at captures
        this.moveSorter = MoveSorter.SORT; // The move sorter is an enum that determines how the moves are sorted
        this.boardsEvaluated = 0; // The number of boards evaluated
        this.quiescenceCount = 0; // The number of times quiescence search was used
        this.cutOffsProduced = 0; // The number of times the alpha-beta pruning algorithm cut off a branch
    }

    @Override
    public String toString() {
        return "AB+MO";
    }

    @Override
    public long getNumBoardsEvaluated() {
        return this.boardsEvaluated;
    }


    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();      // start the timer
        final Player currentPlayer = board.currentPlayer();     // get the current player
        final Alliance alliance = currentPlayer.getAlliance();  // get the alliance of the current player
        Move bestMove = MoveFactory.getNullMove();              // set the best move to null
        int highestSeenValue = Integer.MIN_VALUE;               // set the highest seen value to the minimum integer value
        int lowestSeenValue = Integer.MAX_VALUE;                // set the lowest seen value to the maximum integer value
        int currentValue;                                       // declare the current value
        int moveCounter = 1;                                    // declare the move counter and set it to 1

        final int numMoves = this.moveSorter.sort(board.currentPlayer().getLegalMoves()).size();    // get the number of legal moves
        System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);   // print the current player and the depth
        System.out.println("\tOrdered moves! : " + this.moveSorter.sort(board.currentPlayer().getLegalMoves()));      // print the ordered moves
        for (final Move move : this.moveSorter.sort(board.currentPlayer().getLegalMoves())) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);     // make the move
            this.quiescenceCount = 0;                                                       // reset the quiescence count
            final String s;                                                                 // declare the string
            if (moveTransition.getMoveStatus().isDone()) {
                final long candidateMoveStartTime = System.nanoTime();                      // start the timer
                currentValue = alliance.isWhite() ?                                         // get the current value
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue) : // if the alliance is white
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue);  // if the alliance is black
                if (alliance.isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;              // set the highest seen value to the current value
                    bestMove = move;                              // set the best move to the current move
                    setChanged();
                    notifyObservers(bestMove);
                }
                else if (alliance.isBlack() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;               // set the lowest seen value to the current value
                    bestMove = move;                              // set the best move to the current move
                    setChanged();
                    notifyObservers(bestMove);
                }
                final String quiescenceInfo = " [h: " +highestSeenValue+ " l: " +lowestSeenValue+ "] q: " +this.quiescenceCount;   // get the quiescence info which:
                s = "\t" + toString() + "(" +this.searchDepth+ "), m: (" +moveCounter+ "/" +numMoves+ ") " + move + ", best:  " + bestMove // gets the best move and the current move and the number of moves and the move counter and the search depth
                        + quiescenceInfo + ", t: " +calculateTimeTaken(candidateMoveStartTime, System.nanoTime());                 // gets the time taken to make the move and the quiescence info
            } else {
                s = "\t" + toString() + ", m: (" +moveCounter+ "/" +numMoves+ ") " + move + " is illegal, best: " + bestMove;       // get the string
            }
            System.out.println(s);      // print the string
            setChanged();               // set the changed
            notifyObservers(s);         // notify the observers
            moveCounter++;              // increment the move counter
        }
        this.executionTime = System.currentTimeMillis() - startTime;    // get the execution time
        System.out.printf("%s SELECTS %s [#boards evaluated = %d, time taken = %d ms, eval rate = %.1f cutoffCount = %d prune percent = %.2f\n", board.currentPlayer(),
                bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)), this.cutOffsProduced, 100 * ((double)this.cutOffsProduced/this.boardsEvaluated));
        return bestMove;        // return the best move
    }

    public int max(final Board board,                       // the board
                   final int depth,                         // the depth
                   final int highest,                       // the highest value
                   final int lowest) {                      // the lowest value
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;                         // increment the boards evaluated
            return this.evaluator.evaluate(board, depth);   // return the evaluation of the board
        }
        int currentHighest = highest;                       // set the current highest to the highest
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {         // for each move in the ordered moves
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);                 // make the move
            if (moveTransition.getMoveStatus().isDone()) {                                              // if the move is done
                currentHighest = Math.max(currentHighest, min(moveTransition.getTransitionBoard(),      // set the current highest to the maximum of the current highest and the minimum of the transition board
                        calculateQuiescenceDepth(board, move, depth), currentHighest, lowest));         // and the quiescence depth and the current highest and the lowest
                if (lowest <= currentHighest) {         // if the lowest is less than or equal to the current highest
                    this.cutOffsProduced++;             // increment the cut-offs produced
                    break;
                }
            }
        }
        return currentHighest;                          // return the current highest
    }

    public int min(final Board board,                   // the board
                   final int depth,                     // the depth
                   final int highest,                   // the highest value
                   final int lowest) {                  // the lowest value
        if (depth == 0 || BoardUtils.isEndGame(board)) {    // if the depth is 0 or the board is in an end game
            this.boardsEvaluated++;                         // increment the boards evaluated
            return this.evaluator.evaluate(board, depth);   // return the evaluation of the board
        }
        int currentLowest = lowest;                         // set the current lowest to the lowest
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);          // make the move
            if (moveTransition.getMoveStatus().isDone()) {                                       // if the move is done
                currentLowest = Math.min(currentLowest, max(moveTransition.getTransitionBoard(), // set the current lowest to the minimum of the current lowest and the maximum of the transition board
                        calculateQuiescenceDepth(board, move, depth), highest, currentLowest));  // and the quiescence depth and the highest and the current lowest
                if (currentLowest <= highest) {      // if the current lowest is less than or equal to the highest
                    this.cutOffsProduced++;          // increment the cut-offs produced
                    break;
                }
            }
        }
        return currentLowest;                        // return the current lowest
    }

    /**
     * Calculates the quiescence depth.
     *
     * @param board the board
     * @param move the move
     * @param depth the depth
     * @return the quiescence depth
     */
    private int calculateQuiescenceDepth(final Board board,  // the board
                                         final Move move,    // the move
                                         final int depth) {  // the depth
        return depth - 1;                                    // return the depth minus 1
    }

    /**
     * Calculates the time taken.
     *
     * @param start the start time
     * @param end the end time
     * @return timeTaken
     */
    private static String calculateTimeTaken(final long start, final long end) {  // the start time and the end time
        final long timeTaken = (end - start) / 1000000;                           // get the time taken
        return timeTaken + " ms";                                                 // return the time taken
    }


}