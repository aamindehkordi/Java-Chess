package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    /** The piece's type */
    protected final PieceType pieceType;
    /** The piece's position on the board */
    protected final int piecePosition;

    /** The piece's alliance */
    protected final Alliance pieceAlliance;

    /** Is piece's first move? */
    protected final boolean isFirstMove;

    private final int cachedHashCode;

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    /** Compute the hash code for the piece
     *
     * @return the hash code
     */
    private int computeHashCode() {
        int result = pieceType.hashCode(); /* get the hash code of the piece type */
        result = 31 * result + pieceAlliance.hashCode(); /* multiply the hash code by 31 and add the hash code of the piece alliance */
        result = 31 * result + piecePosition; /* multiply the hash code by 31 and add the piece position */
        result = 31 * result + (isFirstMove ? 1 : 0); /* multiply the hash code by 31 and add 1 if the piece is in its first move, 0 otherwise */
        return result; /* return the hash code */
    }

    /** Checks if two pieces are equal not only in reference but also in terms of objects
     * @param other the other piece object
     * @return true if the pieces are equal, false otherwise
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) { /* if the pieces are the same */
            return true;
        }
        if (!(other instanceof final Piece otherPiece)) { /* if the other object is not a piece */
            return false;
        }
        /* cast the other object to a piece */
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && /* True if the piece position and type are the same and */
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove(); /* the piece alliance and first move are the same */
    }

    /** Gets the hash code of the piece to make sure that the piece is not modified during move generation
     *
     * @return the hash code of the piece
     */
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }

    /** Calculate the legal moves for the piece
    *
    * @param board the board
    * @return a collection of legal moves
    */
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    /** Return a new piece with the updated position
     *
     * @param move the move
     * @return a new piece with the updated position
     */
    public abstract Piece movePiece(Move move);

    /** Get the alliance of the piece
     *
     * @return the alliance of the piece
     */
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    /** if the piece has made its first move
     *
     * @return true if the piece is on still its first move
     */
    public boolean isFirstMove() {
        return false;
    }

    /** Get the piece's position
     *
     * @return the piece's position
     */
    public Integer getPiecePosition() {
        return this.piecePosition;
    }

    /** Get the piece's type
     *
     * @return the piece's type
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }
}
