package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
    private final Board transitionBoard; // the board after the move
    private final Move move; // the move
    private final MoveStatus moveStatus; // the status of the move

    /** Constructor
     * @param transitionBoard the board after the move
     * @param move the move
     * @param moveStatus the status of the move
     */
    public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    /** Get the board after the move
     * @return the board after the move
     */
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    public MoveTransition getTransitionMove() {
        return this;
    }

    public int getFromBoard() {
        return this.transitionBoard.getTile(this.move.getCurrentCoordinate()).getPiece().getPiecePosition();
    }
}
