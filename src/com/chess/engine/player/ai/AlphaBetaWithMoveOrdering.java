package com.chess.engine.player.ai;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;

import java.util.Collection;
import java.util.Comparator;
import java.util.Observable;
import java.util.stream.Collectors;

import static com.chess.engine.board.BoardUtils.mvvlva;

public class AlphaBetaWithMoveOrdering extends Observable implements MoveStrategy {
//TODO ADD COMMENTS

    private final BoardEvaluator evaluator;
    private final int searchDepth;
    private final MoveSorter moveSorter;
    private final int quiescenceFactor;
    private long boardsEvaluated;
    private long executionTime;
    private int quiescenceCount;
    private int cutOffsProduced;

    private enum MoveSorter {

        SORT {

            @Override
            Collection<Move> sort(final Collection<Move> moves) {
                return moves.stream().sorted(SMART_SORT).collect(Collectors.toList());
            }
        };

        // Using standard java libraries
        public static Comparator<Move> SMART_SORT = new Comparator<Move>() {
            @Override
                public int compare(final Move move1, final Move move2) {
                return (mvvlva(move2) - mvvlva(move1));
            }
        };

        abstract Collection<Move> sort(Collection<Move> moves);
    }

    public AlphaBetaWithMoveOrdering(final int searchDepth,
                                     final int quiescenceFactor) {
        this.evaluator = StandardBoardEvaluator.get();
        this.searchDepth = searchDepth;
        this.quiescenceFactor = quiescenceFactor;
        this.moveSorter = MoveSorter.SORT;
        this.boardsEvaluated = 0;
        this.quiescenceCount = 0;
        this.cutOffsProduced = 0;
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
        final long startTime = System.currentTimeMillis();
        final Player currentPlayer = board.currentPlayer();
        final Alliance alliance = currentPlayer.getAlliance();
        Move bestMove = MoveFactory.getNullMove();
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        int moveCounter = 1;
        final int numMoves = this.moveSorter.sort(board.currentPlayer().getLegalMoves()).size();
        System.out.println(board.currentPlayer() + " THINKING with depth = " + this.searchDepth);
        System.out.println("\tOrdered moves! : " + this.moveSorter.sort(board.currentPlayer().getLegalMoves()));
        for (final Move move : this.moveSorter.sort(board.currentPlayer().getLegalMoves())) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            this.quiescenceCount = 0;
            final String s;
            if (moveTransition.getMoveStatus().isDone()) {
                final long candidateMoveStartTime = System.nanoTime();
                currentValue = alliance.isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1, highestSeenValue, lowestSeenValue);
                if (alliance.isWhite() && currentValue > highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                    //setChanged();
                    //notifyObservers(bestMove);
                }
                else if (alliance.isBlack() && currentValue < lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                    //setChanged();
                    //notifyObservers(bestMove);
                }
                final String quiescenceInfo = " [h: " +highestSeenValue+ " l: " +lowestSeenValue+ "] q: " +this.quiescenceCount;
                s = "\t" + toString() + "(" +this.searchDepth+ "), m: (" +moveCounter+ "/" +numMoves+ ") " + move + ", best:  " + bestMove

                        + quiescenceInfo + ", t: " +calculateTimeTaken(candidateMoveStartTime, System.nanoTime());
            } else {
                s = "\t" + toString() + ", m: (" +moveCounter+ "/" +numMoves+ ") " + move + " is illegal, best: " +bestMove;
            }
            System.out.println(s);
            setChanged();
            notifyObservers(s);
            moveCounter++;
        }
        this.executionTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s SELECTS %s [#boards evaluated = %d, time taken = %d ms, eval rate = %.1f cutoffCount = %d prune percent = %.2f\n", board.currentPlayer(),
                bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)), this.cutOffsProduced, 100 * ((double)this.cutOffsProduced/this.boardsEvaluated));
        return bestMove;
    }

    public int max(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentHighest = highest;
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentHighest = Math.max(currentHighest, min(moveTransition.getTransitionBoard(),
                        calculateQuiescenceDepth(board, move, depth), currentHighest, lowest));
                if (lowest <= currentHighest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        return currentHighest;
    }

    public int min(final Board board,
                   final int depth,
                   final int highest,
                   final int lowest) {
        if (depth == 0 || BoardUtils.isEndGame(board)) {
            this.boardsEvaluated++;
            return this.evaluator.evaluate(board, depth);
        }
        int currentLowest = lowest;
        for (final Move move : this.moveSorter.sort((board.currentPlayer().getLegalMoves()))) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentLowest = Math.min(currentLowest, max(moveTransition.getTransitionBoard(),
                        calculateQuiescenceDepth(board, move, depth), highest, currentLowest));
                if (currentLowest <= highest) {
                    this.cutOffsProduced++;
                    break;
                }
            }
        }
        return currentLowest;
    }

    private int calculateQuiescenceDepth(final Board board,
                                         final Move move,
                                         final int depth) {
        return depth - 1;
    }

    private static String calculateTimeTaken(final long start, final long end) {
        final long timeTaken = (end - start) / 1000000;
        return timeTaken + " ms";
    }


}