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

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove();
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
        return this.isFirstMove;
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
