package com.chess.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.player.MoveTransition;
import com.chess.player.Player;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;
import java.util.stream.Collectors;

import static com.chess.engine.board.BoardUtils.mvvlva;
import static com.chess.engine.board.Move.MoveFactory;

/**
 * This class is used to implement the Stock Alpha Beta algorithm.
 */
public class StockAlphaBeta extends Observable implements MoveStrategy {

    /**
     * This method initializes the BoardEvaluator.
     */
    private final BoardEvaluator evaluator;

    /**
     * This method initializes the search depth.
     */
    private final int searchDepth;

    /**
     * This method initializes board evaluation.
     */
    private long boardsEvaluated;

    /**
     * This method initializes the quiescence counter.
     */
    private int quiescenceCount;

    /**
     * This method initializes the maximum quiescence.
     */
    private static final int MAX_QUIESCENCE = 5000 * 5;

    /**
     * This method sorts the moves.
     */
    private enum MoveSorter {


        // Using standard java libraries
        STANDARD {
            Collection<Move> sort(final Collection<Move> moves) {
                return moves.stream()
                        .sorted(Comparator.comparingInt((Move move) -> mvvlva(move)).reversed()
                                .thenComparingInt(move -> move.isCastlingMove() ? 1 : 0))
                        .collect(Collectors.toList());
            }
        },
        EXPENSIVE {
            Collection<Move> sort(final Collection<Move> moves) {
                return moves.stream()
                        .sorted(Comparator.comparingInt((Move move) -> BoardUtils.kingThreat(move) ? 1 : 0)
                                .thenComparingInt(move -> mvvlva(move)).reversed()
                                .thenComparingInt(move -> move.isCastlingMove() ? 1 : 0))
                        .collect(Collectors.toList());
            }
        };


        /**
         * This abstract method is used to sort the moves.
         * @param moves
         */
        abstract  Collection<Move> sort(Collection<Move> moves);
    }


    /**
     * This method is used to initialize the StockAlphaBeta.
     * @param searchDepth
     */
    public StockAlphaBeta(final int searchDepth) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.boardsEvaluated = 0;
        this.quiescenceCount = 0;
    }

    @Override
    public String toString() {      //To string method
        return "StockAB";           //return StockAB
    }

    @Override
    public long getNumBoardsEvaluated() {   //get number of boards evaluated
        return this.boardsEvaluated;        //return number of boards evaluated
    }

    @Override
    public Move execute(final Board board) {
        final long startTime = System.currentTimeMillis();  //get start time
        final Player currentPlayer = board.currentPlayer(); //get current player
        Move bestMove = MoveFactory.getNullMove();          //get best move
        int highestSeenValue = Integer.MIN_VALUE;           //get highest seen value
        int lowestSeenValue = Integer.MAX_VALUE;            //get lowest seen value
        int currentValue;                                   //current value
        System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);   //print board current player and search depth
        int moveCounter = 1;                                //move counter is 1
        int numMoves = board.currentPlayer().getLegalMoves().size();
        for (final Move move : MoveSorter.EXPENSIVE.sort((board.currentPlayer().getLegalMoves()))) {    //for each move in move sorter
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);                 //make move
            this.quiescenceCount = 0;      //quiescence count is 0
            final String s;                //string s
            if (moveTransition.getMoveStatus().isDone()) {
                final long candidateMoveStartTime = System.nanoTime();  //get candidate move start time
                currentValue = currentPlayer.getAlliance().isWhite() ?  //if current player alliance is white
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue);
                if (currentPlayer.getAlliance().isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;    //highest seen value is current value
                    bestMove = move;                    //best move is move
                    if(moveTransition.getTransitionBoard().blackPlayer().isInCheckMate()) {
                        break;
                    }
                }
                else if (currentPlayer.getAlliance().isBlack() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;    //lowest seen value is current value
                    bestMove = move;                   //best move is move
                    if(moveTransition.getTransitionBoard().whitePlayer().isInCheckMate()) {
                        break;
                    }
                }

                final String quiescenceInfo = " " + score(currentPlayer, highestSeenValue, lowestSeenValue) + " q: " +this.quiescenceCount;
                s = "\t" + toString() + "(" +this.searchDepth+ "), m: (" +moveCounter+ "/" +numMoves+ ") " + move + ", best:  " + bestMove

                        + quiescenceInfo + ", t: " +calculateTimeTaken(candidateMoveStartTime, System.nanoTime());
            } else {
                s = "\t" + toString() + "(" +this.searchDepth + ")" + ", m: (" +moveCounter+ "/" +numMoves+ ") " + move + " is illegal! best: " +bestMove;
            }
            System.out.println(s);
            setChanged();
            notifyObservers(s);
            moveCounter++;
        }

        final long executionTime = System.currentTimeMillis() - startTime;  //get execution time
        final String result = board.currentPlayer() + " SELECTS " +bestMove+ " [#boards evaluated = " +this.boardsEvaluated+
                " time taken = " + executionTime /1000+ " rate = " +(1000 * ((double)this.boardsEvaluated/ executionTime));
        System.out.printf("%s SELECTS %s [#boards evaluated = %d, time taken = %d ms, rate = %.1f\n", board.currentPlayer(),
                bestMove, this.boardsEvaluated, executionTime, (1000 * ((double)this.boardsEvaluated/ executionTime)));
        setChanged();   //set changed
        notifyObservers(result);    //notify observers
        return bestMove;            //return best move
    }

    /**
     * This method is used to calculate the time taken.
     * @param currentPlayer
     * @param highestSeenValue
     * @param lowestSeenValue
     * @return either highest seen value or lowest seen value
     */
    private static String score(final Player currentPlayer,
                                final int highestSeenValue,
                                final int lowestSeenValue) {

        if(currentPlayer.getAlliance().isWhite()) {
            return "[score: " +highestSeenValue + "]";
        } else if(currentPlayer.getAlliance().isBlack()) {
            return "[score: " +lowestSeenValue+ "]";
        }
        throw new RuntimeException("bad bad boy!");
    }

    /**
     * This method is used to calculate the time taken.
     * @param board
     * @param depth
     * @param highest
     * @param lowest
     * @return return the highest seen value
     */
    private int max(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : MoveSorter.STANDARD.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getTransitionBoard();
                currentHighest = Math.max(currentHighest, min(toBoard,
                        calculateQuiescenceDepth(toBoard, depth), currentHighest, lowest));
                if (currentHighest >= lowest) {
                    return lowest;
                }
            }
        }
        return currentHighest;
    }

    /**
     * This method is used to calculate the time taken.
     * @param board
     * @param depth
     * @param highest
     * @param lowest
     * @return return the lowest seen value
     */
    private int min(final Board board,
                    final int depth,
                    final int highest,
                    final int lowest) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : MoveSorter.STANDARD.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                final Board toBoard = moveTransition.getTransitionBoard();
                currentLowest = Math.min(currentLowest, max(toBoard,
                        calculateQuiescenceDepth(toBoard, depth), highest, currentLowest));
                if (currentLowest <= highest) {
                    return highest;
                }
            }
        }
        return currentLowest;
    }

    /**
     * This method is used to the quiescence depth.
     * @param toBoard
     * @param depth
     * @return return the depth.
     */
    private int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        if(depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.currentPlayer().isInCheck()) {
                activityMeasure += 1;
            }
            for(final Move move: BoardUtils.lastNMoves(toBoard, 2)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }

    /**
     * This method is used to calculate the time taken.
     * @param start
     * @param end
     * @return return the time taken.
     */
    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken + " ms";
    }

}