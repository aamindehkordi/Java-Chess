package com.chess.engine.pieces;

import com.chess.player.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

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
    private boolean isCastled;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public King(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.KING, piecePosition, pieceAlliance, true);
        this.isCastled = false;
        this.kingSideCastleCapable = false;
        this.queenSideCastleCapable = false;
    }

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     * @param isFirstMove if the piece has moved
     */
    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
        this.isCastled = false;
        this.kingSideCastleCapable = false;
        this.queenSideCastleCapable = false;
    }

    public King(final Alliance alliance,
                final int piecePosition,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, alliance, true);
        this.isCastled = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(final Alliance alliance,
                final int piecePosition,
                final boolean isFirstMove,
                final boolean isCastled,
                final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable) {
        super(PieceType.KING, piecePosition, alliance, isFirstMove);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(int destinationCoordinate, Alliance pieceAlliance, boolean isCastled, boolean kingSideCastleCapable, boolean queenSideCastleCapable) {
        super(PieceType.KING, destinationCoordinate, pieceAlliance, true);
        this.isCastled = isCastled;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastled() {
        return this.isCastled;
    }

    /** Sets the king as castled
     *
     * @return a new king with the isCastled field set to true
     */
    public void setCastled() {
        this.isCastled = true;
    }


    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        // List of legal moves
        final List<Move> legalMoves = new ArrayList<>();

        // Loop through the possible move offsets
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            // Get the destination coordinate
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            // If the destination coordinate is valid
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                // If the king is on the edge of the board
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    // Skip to the next offset
                    continue;
                }

                // Get the tile at the destination coordinate
                final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                // If the tile is empty
                if (pieceAtDestination == null) {
                    // Add the move to the list of legal moves
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
                // If the tile is occupied
                else {
                    // Get the alliance of the piece at the destination coordinate
                    final Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();

                    // If the piece at the destination coordinate is not the same alliance as the king
                    if (this.pieceAlliance != pieceAtDestinationAlliance) {
                        // Add the move to the list of legal moves
                        legalMoves.add(new Move.MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    /**
     * Return a new piece with the updated position
     *
     * @param move the move
     * @return a new piece with the updated position
     */
    @Override
    public Piece movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.kingBonus(this.piecePosition);
    }

    public Piece movePiece(Move move, boolean isCastled) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), isCastled, false, false);
    }

    @Override
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
        // If the king is on the eighth column and the move is to the rightup, right, or rightdown, the move is illegal
        boolean onEightgh = BoardUtils.EIGHTH_COLUMN[piecePosition];
        boolean toRight = currentCandidateOffset == 1;
        boolean toRightUp = currentCandidateOffset == 9;
        boolean toRightDown = currentCandidateOffset == -7;
return onEightgh && (toRight || toRightUp || toRightDown);

    }

    /** If the king is on the eighth column, the move is illegal
     *
     * @param piecePosition the current position of the king
     * @param currentCandidateOffset the offset of the move
     * @return true if the move is illegal
     */
    private boolean isFirstColumnExclusion(int piecePosition, int currentCandidateOffset) {
        // If the king is on the first column and the move is to the leftup, left, or leftdown, the move is illegal
        boolean onFirst = BoardUtils.FIRST_COLUMN[piecePosition];
        boolean toLeft = currentCandidateOffset == -1;
        boolean toLeftUp = currentCandidateOffset == 7;
        boolean toLeftDown = currentCandidateOffset == -9;
        return onFirst && (toLeft || toLeftUp || toLeftDown);
    }
}
