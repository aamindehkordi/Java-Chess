package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.*;

import static com.chess.engine.board.Move.AttackMove;
import static com.chess.engine.board.Move.MajorMove;

public class Knight extends Piece {

    /* -17 = up 2, left 1
     * -15 = up 2, right 1
     * -10 = up 1, left 2
     * -6 = up 1, right 2
     * 6 = down 1, right 2
     * 10 = down 1, left 2
     * 15 = down 2, right 1
     * 17 = down 2, left 1
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    /* Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT ,piecePosition, pieceAlliance);
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
    @Override
    /* Calculate the legal moves for the Knight
     *
     * @param board the board
     * @return a collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(final Board board) {

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
                    /* if the tile is not occupied, add the move to the list of legal moves */
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));

                } else {
                    /* if the tile is occupied, check if the piece is an enemy piece */
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();

                    if (this.pieceAlliance != pieceAlliance) {
                        /* if the piece is an enemy piece, add the move to the list of legal moves */
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));

                    }
                }
            }
        }
        /* return an unmodifiable collection of legal moves */
        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    /* if the knight is on the 1st, 2nd, 7th, or 8th column then the corresponding move is illegal */
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
