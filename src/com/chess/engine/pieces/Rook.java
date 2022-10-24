package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Tile;

import java.util.*;

import static com.chess.engine.board.BoardUtils.*;
import static com.chess.engine.board.Move.*;

public class Rook extends Piece{

    /* -8 = up
     * 8 = down
     * -1 = left
     * 1 = right
     */
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-8, -1, 1, 8};

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    Rook(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        /* for each of the possible moves, check if the move is legal */
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (isValidTileCoordinate(candidateDestinationCoordinate)) {
                /* if the rook is on the these column, the move is illegal */
                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                /* add the offset to the current position */
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (isValidTileCoordinate(candidateDestinationCoordinate)) {
                    /* if the tile is not occupied, add the move to the list of legal moves */
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        /* if the tile is occupied, check if the piece is an enemy piece */
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        /* if the piece is an ally piece, the move is illegal */
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    /* if the rook is on the first or eigth column, the move is illegal */
    private boolean isEighthColumnExclusion(int candidateDestinationCoordinate, int candidateCoordinateOffset) {
        return EIGHTH_COLUMN[candidateDestinationCoordinate] && (candidateCoordinateOffset == 1);
    }

    private boolean isFirstColumnExclusion(int candidateDestinationCoordinate, int candidateCoordinateOffset) {
        return FIRST_COLUMN[candidateDestinationCoordinate] && (candidateCoordinateOffset == -1);
    }
}
