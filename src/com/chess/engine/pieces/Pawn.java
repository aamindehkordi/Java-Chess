package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {

    /**
     * The possible move offsets for a pawn
     * 8: up
     * 7: up and right
     * 9: up and left
     */
    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

    /**
     * Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    /**
     * Constructor
     *
     * @param piecePosition the position of the piece
     * @param pieceAlliance the alliance of the piece
     * @param isFirstMove   if the piece has moved
     */
    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    /**
     * Print the piece
     *
     * @return the piece as a string
     */
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    /**
     * Calculate the legal moves for the Pawn
     *
     * @param board the board
     * @return a collection of legal moves
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>(); /* for each of the possible moves, check if the move is legal */

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) { /* for each of the possible moves*/
            /* add the offset to the current position to get the destination coordinate */
            final int candidateDestinationCoordinate = this.piecePosition +
                    (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            // One move forward
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue; /* if the tile is not occupied, continue checking*/
            }

            /* if you are moving one tile forward and the tile is not occupied*/
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) { /* if the pawn is on the promotion square */
                    legalMoves.add(new PawnPromotion( /* add a pawn promotion move */
                            new PawnMove(board, this, candidateDestinationCoordinate)));
                } else { /* otherwise, add a normal move */
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }
            // Two moves forward
            else if (currentCandidateOffset == 16 && this.isFirstMove &&                                 /* if you are moving two tiles forward, and it is the first move AND */
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) || /* if you are on the second row and your alliance is black OR */
                            (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))) { /* if you are on the seventh row and your alliance is white */
                /* between the current position and the destination position, there must be an empty tile */
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);

                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&                /* if the tile behind the destination is not occupied and */
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {                 /* if the destination tile is not occupied */
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate)); /* add the move to the list of legal moves */
                }
            }
            // Attack move to the left
            else if (currentCandidateOffset == 9 &&                                                          /* if you are moving one tile diagonally to the right */
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||       /* and you are not on the first column, and you are white */
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {       /* or you are not on the eighth column, and you are black  (because of promotions)*/

                /* if there exist an en passant pawn, and its position is to the left of the current position */
                if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    /* if the candidate piece is not the same alliance as the current piece add the move to the legals */
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add( /* add the move to the list of legal moves */
                                new PawnEnPassantAttackMove(board, this,
                                        candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
                /* if the tile is occupied*/
                else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    /* get the piece on the tile */
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    /* if the piece is not the same color as the pawn*/
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        /* if the pawn is on the promotion square */
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination))); /* add a pawn promotion move */
                        } else { /* otherwise, add a normal move */
                            legalMoves.add(new PawnAttackMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination));
                        }
                    }
                }
            }
            // Attack move to the right
            else if (currentCandidateOffset == 7 &&                                                          /* if you are moving one tile diagonally to the left */
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() ||       /* and you are not on the first column, and you are black */
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())))) {       /* or you are not on the eighth column, and you are white  (because of promotions)*/

                /* if there exist an en passant pawn, and its position is to the right of the current position */
                if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    /* if the candidate piece is not the same alliance as the current piece add the move to the legals */
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add( /* add the en passant move to the list of legal moves */
                                new PawnEnPassantAttackMove(board, this,
                                        candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
                /* if the tile is occupied*/
                else if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    /* get the piece on the tile */
                    final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();
                    /* if the piece is not the same color as the pawn*/
                    if (this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                        /* if the pawn is on the promotion square */
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination))); /* add a pawn promotion move */
                        } else { /* otherwise, add a normal move */
                            legalMoves.add(new PawnAttackMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination));
                        }
                    }
                }
            }
        }


    return Collections.unmodifiableList(legalMoves); /* return an unmodifiable list of legal moves */
    }

    /**
     * Return a new piece with the updated position
     *
     * @param move the move
     * @return a new piece with the updated position
     */
    @Override
    public Piece movePiece (Move move){
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    public Piece getPromotionPiece() {
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }
}


