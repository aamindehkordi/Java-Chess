package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class Piece {

    /* the position of the piece */
    protected final int piecePosition;

    /* the alliance of the piece */
    protected final Alliance pieceAlliance;

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }

    /* Calculate the legal moves for the piece
    *
    * @param board the board
    * @return a collection of legal moves
    */
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    /* Get the alliance of the piece
     *
     * @return the alliance of the piece
     */
    protected Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
}
