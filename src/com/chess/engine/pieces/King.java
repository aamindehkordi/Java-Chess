package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.player.Player;

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
    private final boolean isCastled;
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

    public boolean isCastled() {
        return this.isCastled;
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

            // Check if the exclusions
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }

            // Check if the destination coordinate is valid
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                // Get the piece at the destination coordinate
                final Piece pieceAtDestination = board.getTile(candidateDestinationCoordinate).getPiece();

                // Check if the piece at the destination coordinate is null
                if (pieceAtDestination == null) {
                    // Add the move to the list of legal moves
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else { // if the piece at the destination coordinate is not null
                    // Get the alliance of the piece at the destination coordinate
                    final Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();

                    // Check if the alliance of the piece at the destination coordinate is not the same as the alliance of the piece
                    if (this.pieceAlliance != pieceAtDestinationAlliance) {
                        // Add the move to the list of legal moves
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
                // White Kingside Castle
                if (this.piecePosition == 60 && this.pieceAlliance.isWhite() && !this.isFirstMove() &&
                        !this.isCastled()) { //If the white king is in the starting position has not moved and has not castled
                    if (!board.getTile(61).isTileOccupied() && !board.getTile(62).isTileOccupied()) { //If the tiles between the king and the rook are empty
                        final Piece rookTile = board.getTile(63).getPiece(); //Get the rook on the right side
                        if (rookTile != null && rookTile.getPieceType().isRook() && //If the rook is not null and is a rook
                                rookTile.getPieceAlliance() == this.pieceAlliance && //If the rook is the same alliance as the king
                                rookTile.isFirstMove()) { //If the rook has not moved
                            if (Player.calculateAttacksOnTile(61, board.getAllianceToMove()).isEmpty() && //If the tiles between the king and the rook are not attacked
                                    Player.calculateAttacksOnTile(62, board.getAllianceToMove()).isEmpty() &&
                                    Player.calculateAttacksOnTile(63, board.getAllianceToMove()).isEmpty()) {
                                legalMoves.add(new KingSideCastleMove(board, this, 62, (Rook) rookTile, 61, 63));
                            }
                        }
                    }
                }
                // Black Kingside Castle
                if (this.piecePosition == 4 && this.pieceAlliance.isBlack() && !this.isFirstMove() &&
                        !this.isCastled()) { //If the black king is in the starting position has not moved and has not castled
                    if (!board.getTile(5).isTileOccupied() && !board.getTile(6).isTileOccupied()) { //If the tiles between the king and the rook are empty
                        final Piece rookTile = board.getTile(7).getPiece(); //Get the rook on the right side
                        if (rookTile != null && rookTile.getPieceType().isRook() && //If the rook is not null and is a rook
                                rookTile.getPieceAlliance() == this.pieceAlliance && //If the rook is the same alliance as the king
                                rookTile.isFirstMove()) { //If the rook has not moved
                            if (Player.calculateAttacksOnTile(5, board.getAllianceToMove()).isEmpty() && //If the tiles between the king and the rook are not attacked
                                    Player.calculateAttacksOnTile(6, board.getAllianceToMove()).isEmpty() &&
                                    Player.calculateAttacksOnTile(7, board.getAllianceToMove()).isEmpty()) {
                                legalMoves.add(new KingSideCastleMove(board, this, 6, (Rook) rookTile, 5, 7));
                            }
                        }
                    }
                }
                if (!this.isCastled) {
                    if (this.kingSideCastleCapable) {
                        Collection<Piece> opponentPlayerPieces;
                        int castleRookStart, castleRookDestination, kingDestination, oppKingDangerPosition;
                        if (this.getPieceAlliance().toString().equals("BLACK")) {
                            castleRookStart = 7;
                            castleRookDestination = 5;
                            kingDestination = 6;
                            oppKingDangerPosition = 55;
                            opponentPlayerPieces = board.getWhitePieces();
                        } else {
                            castleRookStart = 63;
                            castleRookDestination = 61;
                            kingDestination = 62;
                            oppKingDangerPosition = 14;
                            opponentPlayerPieces = board.getBlackPieces();
                        }
                        boolean a = true, b = true;
                        if (board.getTile(kingDestination).isTileOccupied() || board.getTile(castleRookDestination).isTileOccupied())
                            a = false;


                        outer:
                        for (Piece oppPiece : opponentPlayerPieces) {
                            if (!oppPiece.getPieceType().isKing()) {
                                Collection<Move> oppMoves = oppPiece.calculateLegalMoves(board);
                                for (Move m : oppMoves) {
                                    int target = m.getDestinationCoordinate();
                                    if (target == castleRookDestination || target == kingDestination) {
                                        b = false;
                                        break outer;
                                    }
                                }
                            } else {
                                if (oppPiece.getPiecePosition() == oppKingDangerPosition) {
                                    b = false;
                                    break;
                                }
                            }
                        }


                        if (a & b)
                            legalMoves.add(new KingSideCastleMove(board,
                                    this,
                                    kingDestination,
                                    new Rook(castleRookStart, this.getPieceAlliance(), false),
                                    castleRookStart,
                                    castleRookDestination));

                    }
                    if (this.queenSideCastleCapable) {
                        Collection<Piece> opponentPlayerPieces;
                        int castleRookStart, castleRookDestination, kingDestination, extraTileCoordinate, oppKingDangerPosition;
                        if (this.getPieceAlliance().toString().equals("BLACK")) {
                            castleRookStart = 0;
                            castleRookDestination = 3;
                            kingDestination = 2;
                            extraTileCoordinate = 1;
                            oppKingDangerPosition = 10;
                            opponentPlayerPieces = board.getWhitePieces();
                        } else {
                            castleRookStart = 56;
                            castleRookDestination = 59;
                            kingDestination = 58;
                            extraTileCoordinate = 57;
                            oppKingDangerPosition = 50;
                            opponentPlayerPieces = board.getBlackPieces();
                        }
                        boolean a = true, b = true;
                        if (board.getTile(kingDestination).isTileOccupied() || board.getTile(castleRookDestination).isTileOccupied() || board.getTile(extraTileCoordinate).isTileOccupied())
                            a = false;

                        outer:
                        for (Piece oppPiece : opponentPlayerPieces) {
                            if (!oppPiece.getPieceType().isKing()) {
                                Collection<Move> oppMoves = oppPiece.calculateLegalMoves(board);
                                for (Move m : oppMoves) {
                                    int target = m.getDestinationCoordinate();
                                    if (target == castleRookDestination || target == kingDestination || target == extraTileCoordinate) {
                                        b = false;
                                        break outer;
                                    }
                                }
                            } else {
                                if (oppPiece.getPiecePosition() == oppKingDangerPosition || oppPiece.getPiecePosition() == oppKingDangerPosition - 1) {
                                    b = false;
                                    break;
                                }
                            }
                        }

                        if (a & b)
                            legalMoves.add(new QueenSideCastleMove(board,
                                    this,
                                    kingDestination,
                                    new Rook(castleRookStart, this.getPieceAlliance(), false),
                                    castleRookStart,
                                    castleRookDestination));
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
