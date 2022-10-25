package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

import java.util.*;

public class Bishop extends Piece {

    /* -9 = diagonal left up
     * -7 = diagonal right up
     *  7 = diagonal left down
     *  9 = diagonal right down
     */
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = { -9, -7, 7, 9 };

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Bishop(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    /* Calculate the legal moves for the Knight
     *
     * @param board the board
     * @return an unmodifiable collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        /* for each of the possible moves, check if the move is legal */
        for (final int currentCandidate : CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition + currentCandidate;
            /* while the move is legal, keep adding it to the list of legal moves */
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {

                /* if the bishop is on the these column, the move is illegal */
                if (isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                    break;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                /* if the tile is not occupied, add the move to the list of legal moves */
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    /* if the tile is occupied, check if the piece is an enemy piece */
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    /* if the piece is an enemy piece, add the move to the list of legal moves */
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                    /* if the piece is an ally piece, the move is illegal */
                    break;
                }
            }

        }
        /* return an immutable copy of the list of legal moves */
        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    /* if the bishop is on the first or eigth column, the move is illegal */
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
    }
}
