package com.chess.controller;

import com.chess.model.board.Board;
import com.chess.model.board.Move;

public class MoveTransition {
    private final Board transitionBoard; // the board after the move
    private final Move move; // the move
    private final MoveStatus moveStatus; // the status of the move
    private Board fromBoard; // From Board


    /**
     * Constructor for MoveTransition
     *
     * @param fromBoard the board before the move
     * @param toBoard the board after the move
     * @param move the move
     * @param moveStatus the status of the move
     */
    public MoveTransition(final Board fromBoard,
                          final Board toBoard,
                          final Move move,
                          final MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.transitionBoard = toBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

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

    /** Gets the board after the move
     * @return the board after the move
     */
    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    /** Gets the move
     * @return the move
     */
    public MoveTransition getTransitionMove() {
        return this;
    }

    /**
     * Gets the tile of the current piece's position that is attempted to moved
     *
     * @return the tile of the current piece's position from the board
     */
    public Board getFromBoard() {
        return this.fromBoard;
    }
}
