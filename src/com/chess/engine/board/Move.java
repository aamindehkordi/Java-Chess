package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    /** Constructor
     * @param board the board
     * @param movedPiece the piece that is being moved
     * @param destinationCoordinate the destination coordinate
     */
    Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    /** Get the destination coordinate
     * @return the destination coordinate
     */
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /** Get the piece that is being moved
     * @return the piece that is being moved
     */

    /** Execute the move
     * @return the new board
     */
    public abstract Board execute();

    public Piece getMovedPiece() {
        return this.movedPiece;
    }


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

            final Builder builder = new Builder(); /* create a new board builder */

            for (final Piece piece : this.board.currentPlayer().getActivePieces()) { /* for each piece on the board */

                //TODO hashcode and equals for pieces

                if (!this.movedPiece.equals(piece)) { /* for all the pieces except the moved piece */
                    builder.setPiece(piece); /* add them to the new board */
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { /* for each piece on the board */
                builder.setPiece(piece); /* add the piece to the board */
            }
            builder.setPiece(this.movedPiece.movePiece(this)); /* move the piece */
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); /* set the move maker to the opponent */

            return builder.build(); /* build the board */
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
