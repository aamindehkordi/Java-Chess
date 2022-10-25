package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    /**
     * @param board the board
     * @param movedPiece the piece that is being moved
     * @param destinationCoordinate the destination coordinate
     */
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public abstract Board execute();


    public static final class MajorMove extends Move {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public MajorMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            return null;
        }
    }
    public static final class AttackMove extends Move {
        final Piece attackedPiece;

        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public AttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            return null;
        }
    }
}
