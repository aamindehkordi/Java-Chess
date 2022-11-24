package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.*;

public class BlackPlayer extends Player {
    private final Collection<Move> opponentLegals;

    /** Constructor
     *
     * @param board the board
     * @param whiteStandardLegalMoves the white player's legal moves
     * @param blackStandardLegalMoves the black player's legal moves
     */
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
        this.opponentLegals = whiteStandardLegalMoves;
    }
    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    /**
     * @param playerLegals the player's legal moves
     * @param opponentLegals the opponent's legal moves
     * @return the player's king castles
     */
    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals) {

        // If the king has no castle opportunities
        if (!hasCastleOpportunities()) {
            return Collections.emptyList();
        }

        final List<Move> kingCastles = new ArrayList<>(); // List of legal moves

        //Black Castling Requirements
        if(this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 4 && !this.isInCheck()) {
            //Black King Side Castle
            // If the tiles between the king and the rook are empty
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final Piece kingSideRook = this.board.getTile(7).getPiece();
                // If the rook is not null and the rook is a rook and the rook is the first move
                if(kingSideRook != null && kingSideRook.isFirstMove()) {
                    // If the king is not in check and the tiles between the king and the rook are not in check
                    if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            kingSideRook.getPieceType().isRook()) {
                        // Add the king side castle move to the list of legal moves
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
                                (Rook) kingSideRook, kingSideRook.getPiecePosition(), 5));
                    }
                }
            }
            //Black Queen Side Castle
            // If the tiles between the king and the rook are empty
            if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() &&
                    !this.board.getTile(3).isTileOccupied()) {
                final Piece queenSideRook = this.board.getTile(0).getPiece();
                // If the rook is not null and the rook is a rook and the rook is the first move
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    // If the king is not in check and the tiles between the king and the rook are not in check
                    if(Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                            queenSideRook.getPieceType().isRook()) {
                        // Add the queen side castle move to the list of legal moves
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
                                (Rook) queenSideRook, queenSideRook.getPiecePosition(), 3));
                    }
                }
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(kingCastles));
    }
}
