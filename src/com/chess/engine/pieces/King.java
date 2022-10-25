package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class King extends Piece{

    /** The possible move offsets for the king
     * -9 : top left
     * -8 : top
     * -7 : top right
     * -1 : left
     *  1 : right
     *  7 : bottom left
     *  8 : bottom
     *  9 : bottom right
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public King(int piecePosition, Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance);
    }

    @Override
    /** Calculate the legal moves for the King
     *
     * @param board the board
     * @return a collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {                                                             /* for each of the possible moves, check if the move is legal */
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;                                      /* add the offset to the current position */
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {                                                      /* if the tile is not occupied, add the move to the list of legal moves */
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||                                                /* if the king is on the these column, the move is illegal */
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {                                               /* if the king is on the these column, the move is illegal */
                    continue;                                                                                                            /* skip to the next move */
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);                                     /* if the tile is not occupied, add the move to the list of legal moves */
                if (!candidateDestinationTile.isTileOccupied()) {                                                                        /* if the tile is not occupied, add the move to the list of legal moves */
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));                          /* if the tile is not occupied, add the move to the list of legal moves */
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();                                                /* get the piece at the destination tile */
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();                                                /* get the alliance of the piece at the destination tile */
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); /* if the piece is an enemy piece, add the move to the list of legal moves */
                    }
                }
            }
        }


        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    /** Prints the piece
     *
     * @return the piece as a string
     */
    public String toString() {
        return PieceType.KING.toString();
    }

    /** If the king is on the first column, the move is illegal
     *
     * @param piecePosition the current position of the king
     * @param currentCandidateOffset the offset of the move
     * @return true if the move is illegal
     */
    private boolean isEighthColumnExclusion(int piecePosition, int currentCandidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[piecePosition] && (currentCandidateOffset == -9 || currentCandidateOffset == -1 || currentCandidateOffset == 7); /* if the king is on the eighth column, the move is illegal */
    }

    /** If the king is on the eighth column, the move is illegal
     *
     * @param piecePosition the current position of the king
     * @param currentCandidateOffset the offset of the move
     * @return true if the move is illegal
     */
    private boolean isFirstColumnExclusion(int piecePosition, int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[piecePosition] && (currentCandidateOffset == -7 || currentCandidateOffset == 1 || currentCandidateOffset == 9); /* if the king is on the first column, the move is illegal */
    }
}
