package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.*;

import static com.chess.engine.board.BoardUtils.isValidTileCoordinate;

public class Queen extends Piece{

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    /* Calculate the legal moves for the piece
     *
     * @param board the board
     * @return an unmodifiable collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        /* for each of the possible moves, check if the move is legal */
        for (final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (isValidTileCoordinate(candidateDestinationCoordinate)) {
                /* if the rook is on the these column, the move is illegal */
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)) {
                    break;
                }
                /* add the offset to the current position */
                candidateDestinationCoordinate += currentCandidateOffset;
                if (isValidTileCoordinate(candidateDestinationCoordinate)) {
                    /* if the tile is not occupied, add the move to the list of legal moves */
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        /* if the tile is occupied, check if the piece is an enemy piece */
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        /* if the piece is an ally piece, the move is illegal */
                        break;
                    }
                }
            }
        }
        /* return an unmodifiable collection of legal moves */
        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    private boolean isEighthColumnExclusion(int candidateDestinationCoordinate, int currentCandidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[candidateDestinationCoordinate] && (currentCandidateOffset == 1 || currentCandidateOffset == 9 || currentCandidateOffset == -7);
    }

    private boolean isFirstColumnExclusion(int candidateDestinationCoordinate, int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[candidateDestinationCoordinate] && (currentCandidateOffset == -1 || currentCandidateOffset == -9 || currentCandidateOffset == 7);
    }
}
