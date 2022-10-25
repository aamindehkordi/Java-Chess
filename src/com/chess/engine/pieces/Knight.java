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

    /** The possible move offsets for a knight
     * -17 : up 2, left 1
     * -15 : up 2, right 1
     * -10 : up 1, left 2
     * -6  : up 1, right 2
     * 6   : down 1, right 2
     * 10  : down 1, left 2
     * 15  : down 2, right 1
     * 17  : down 2, left 1
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {-17, -15, -10, -6, 6, 10, 15, 17};

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Knight(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KNIGHT ,piecePosition, pieceAlliance);
    }

    @Override
    /** Prints the piece
     *
     * @return the piece as a string
     */
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
    @Override
    /** Calculate the legal moves for the Knight
     *
     * @param board the board
     * @return a collection of legal moves
     */
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>(); /* for each of the possible moves, check if the move is legal */

        for (final int currentCandidate : CANDIDATE_MOVE_COORDINATE) { /* for each of the possible moves*/
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidate;  /* get the destination coordinate */

            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) { /* if the move is legal*/
                if(isFirstColumnExclusion(this.piecePosition, currentCandidate) || /* the move is illegal if the knight is on the 1st, */
                        isSecondColumnExclusion(this.piecePosition, currentCandidate) || /* 2nd, */
                        isSeventhColumnExclusion(this.piecePosition, currentCandidate) || /* 7th, */
                        isEighthColumnExclusion(this.piecePosition, currentCandidate)) { /* or 8th columns */
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate); /* get the tile at the destination */

                if (!candidateDestinationTile.isTileOccupied()) { /* if the tile is not occupied */
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate)); /* add the move to the list of legal moves */

                } else { /* if the tile is occupied, check if the piece is an enemy piece */
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece(); /* get the piece at the destination */
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();  /* get the alliance of the piece at the destination */

                    if (this.pieceAlliance != pieceAlliance) { /* if the piece is an enemy piece */
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination)); /* add the move to the list of legal moves */

                    }
                }
            }
        }
        /* return an unmodifiable collection of legal moves */
        return Collections.unmodifiableList(new LinkedList<>(legalMoves));
    }

    /**
     * Return a new piece with the updated position
     *
     * @param move the move
     * @return a new piece with the updated position
     */
    @Override
    public Piece movePiece(Move move) {
        return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    /** if the knight is on the 8th column then the corresponding move is illegal
     *
     * @param currentPos
     * @param candidateOff
     * @return true if the move is illegal, false otherwise
     */
    private boolean isEighthColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.EIGHTH_COLUMN[currentPos] && (candidateOff == -15 || candidateOff == -6 || candidateOff == 10 || candidateOff == 17);
    }

    /** if the knight is on the 7th column then the corresponding move is illegal
     *
     * @param currentPos
     * @param candidateOff
     * @return true if the move is illegal, false otherwise
     */
    private boolean isSeventhColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.SEVENTH_COLUMN[currentPos] && (candidateOff == -6 || candidateOff == 10);
    }

    /** if the knight is on the 2nd column then the corresponding move is illegal
     *
     * @param currentPos
     * @param candidateOff
     * @return true if the move is illegal, false otherwise
     */
    private boolean isSecondColumnExclusion(final int currentPos, final int candidateOff) {
        return BoardUtils.SECOND_COLUMN[currentPos] && (candidateOff == 6 || candidateOff == -10);
    }

    /** if the knight is on the 1st column then the corresponding move is illegal
     *
     * @param currentPos
     * @param candidateOff
     * @return true if the move is illegal, false otherwise
     */
    private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOff){
        /* if the current position is in the first column, then the knight cannot move to the left */
        return BoardUtils.FIRST_COLUMN[currentPos] && ((candidateOff == -17) || (candidateOff == -10) ||
                (candidateOff == 6) || (candidateOff == 15));
    }

}
