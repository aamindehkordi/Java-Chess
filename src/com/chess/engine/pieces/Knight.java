package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        /* for each of the possible moves, check if the move is legal */
        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                /* if the knight is on the these column, the move is illegal */
                if(isFirstColumnExclusion(this.piecePosition, currentCandidate) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidate) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidate) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidate)) {
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);

                if (!candidateDestinationTile.isTileOccupied()) {

                    legalMoves.add(new Move());

                } else {

                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {

                        legalMoves.add(new Move());

                    }
                }
            }
        }

        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    private boolean isEighthColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.EIGHTH_COLUMN[currentPos] && (candidateOff == -15 || candidateOff == -6 || candidateOff == 10 || candidateOff == 17);
    }

    private boolean isSeventhColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.SEVENTH_COLUMN[currentPos] && (candidateOff == -6 || candidateOff == 10);
    }

    private boolean isSecondColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.SECOND_COLUMN[currentPos] && (candidateOff == 6 || candidateOff == -10);
    }

    private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOff){
            /* if the current position is in the first column, then the knight cannot move to the left */
            return BoardUtils.FIRST_COLUMN[currentPos] && ((candidateOff == -17) || (candidateOff == -10) ||
                    (candidateOff == 6) || (candidateOff == 15));
        }

}
