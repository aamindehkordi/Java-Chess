package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

    /** The board */
    final Board board;
    /** The piece being moved */
    final Piece movedPiece;
    /** The destination coordinate */
    final int destinationCoordinate;

    /** A null move */
    public static final Move NULL_MOVE = new NullMove();

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

    /** Hashcode
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    /** Equals
     * @param other the other move
     * @return true if the moves are equal, false otherwise
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) { /* if the moves are the same */
            return true;
        }
        if (!(other instanceof Move)) { /* if the other object is not a move */
            return false;
        }
        final Move otherMove = (Move) other; /* cast the other object to a move */
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() && /* True if the destination coordinates are the same */
                getMovedPiece().equals(otherMove.getMovedPiece()); /* and the moved pieces are the same */
    }

    /** Get the destination coordinate
     * @return the destination coordinate
     */
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    /** Get the moved piece
     * @return the moved piece
     */
    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    /** Gets the current Coordinate of a piece
     *
     * @return the current coordinate of a piece
     */
    private int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    /** Checks if the move is an attacking move
     *
     * @return true if the move is an attack, false otherwise
     */
    public boolean isAttack() {
        return false;
    }

    /** Checks if the move is a castling move
     *
     * @return true if the move is a castling move, false otherwise
     */
    public boolean isCastlingMove() {
        return false;
    }

    /** Gets the attacked piece
     *
     * @return the attacked piece
     */
    public Piece getAttackedPiece() {
        return null;
    }
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

    /** The Major Move class */
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

    }
    public static class AttackMove extends Move {
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
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            if (this == other) { /* if the moves are the same */
                return true;
            }
            if (!(other instanceof AttackMove)) { /* if the other object is not an attack move */
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other; /* cast the other object to an attack move */
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece()); /* True if the destination coordinates are the same */
        }
        @Override
        public Board execute() {
            return null;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }

    public static class PawnMove extends Move {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public PawnMove(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param attackedPiece the piece that is being attacked
         */
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends PawnMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         */
        public PawnJump(final Board board, final Piece movedPiece, final int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder(); /* create a new board builder */
            for (final Piece piece : this.board.currentPlayer().getActivePieces()) { /* for each piece on the board */
                if (!this.movedPiece.equals(piece)) { /* for all the pieces except the moved piece */
                    builder.setPiece(piece); /* add them to the new board */
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) { /* for each piece on the board */
                builder.setPiece(piece); /* add the piece to the board */
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this); /* move the piece */
            builder.setPiece(movedPawn); /* add the moved piece to the board */ /* only difference */
            builder.setEnPassantPawn(movedPawn); /* set the en passant pawn */
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance()); /* set the move maker to the opponent */
            return builder.build(); /* build the board */
        }
    }

    public static abstract class CastleMove extends Move {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public CastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
    }

    public static final class KingSideCastleMove extends CastleMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove {
        /** Constructor
         *
         * @param board the board
         * @param movedPiece the piece that is being moved
         * @param destinationCoordinate the destination coordinate
         * @param castleRook the rook that is being castled
         * @param castleRookStart the start coordinate of the rook
         * @param castleRookDestination the destination coordinate of the rook
         */
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destinationCoordinate, final Rook castleRook, final int castleRookStart, final int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
    }

    public static final class NullMove extends Move {
        /** Constructor
         *
         */
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove(final Board board, final int currentCoordinate, final int destinationCoordinate) {

            for(final Move move : board.getAllLegalMoves()) {
                if(move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
