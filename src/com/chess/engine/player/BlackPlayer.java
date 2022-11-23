package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.*;

public class BlackPlayer extends Player {
    private Collection<Move> opponentLegals;

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

        //White Castling requirements
        if (this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()) {
            //WHITE KING SIDE CASTLE
            // If the tiles on the kingside are not occupied
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(63);
                // If the rook is on the tile and its first move
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // If the king is not in check
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }
            //WHITE QUEEN SIDE CASTLE
            // If the tiles on the queenside are not occupied
            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                final Tile rookTile = this.board.getTile(56);
                // If the rook is on the tile and it is the first move
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // If the king is not in check
                    if (Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }
}
