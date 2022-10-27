package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

import java.util.*;

import static com.chess.engine.board.BoardUtils.isValidTileCoordinate;

public class Queen extends Piece{

    /** The offsets for the queen's legal moves
     * -9  : up 1, left 1
     * -8  : up 1
     * -7  : up 1, right 1
     * -1  : left 1
     *  1  : right 1
     *  7  : down 1, left 1
     *  8  : down 1
     *  9  : down 1, right 1
     */
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
    }

    /** Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     * @param isFirstMove if the piece has moved
     */
    public Queen(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }
    @Override
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
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    /** If the Queen is on the Eighth Column, the move is illegal
     *
     * @param candidateDestinationCoordinate the current position of the Queen
     * @param currentCandidateOffset the offset of the move
     * @return true if the move is illegal, false otherwise
     */
    private boolean isEighthColumnExclusion(int candidateDestinationCoordinate, int currentCandidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[candidateDestinationCoordinate] && (currentCandidateOffset == 1 || currentCandidateOffset == 9 || currentCandidateOffset == -7);
    }

    /** If the Queen is on the First Column, the move is illegal
     *
     * @param candidateDestinationCoordinate the current position of the Queen
     * @param currentCandidateOffset the offset of the move
     * @return true if the move is illegal, false otherwise
     */
    private boolean isFirstColumnExclusion(int candidateDestinationCoordinate, int currentCandidateOffset) {
        return BoardUtils.FIRST_COLUMN[candidateDestinationCoordinate] && (currentCandidateOffset == -1 || currentCandidateOffset == -9 || currentCandidateOffset == 7);
    }
}
