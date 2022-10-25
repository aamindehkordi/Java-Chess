package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.*;

public class WhitePlayer extends Player {

    /** Constructor
     *
     * @param board the board
     * @param whiteStandardLegalMoves the white player's legal moves
     * @param blackStandardLegalMoves the black player's legal moves
     */
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    /**
     * @param playerLegals the player's legal moves
     * @param opponentLegals Opponent's legal moves
     * @return the legal moves for the king castles
     */
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>(); /* List of legal moves */

        // KING SIDE CASTLE
        if (this.playerKing.isFirstMove() && !this.isInCheck() && /* If the king has not moved and is not in check */
            !this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) { /* And if the tiles on the kingside are not occupied */
            final Tile rookTile = this.board.getTile(63);
            if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {
                    //TODO more to come here (castling)
                    kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                }
            }
        }
        // QUEEN SIDE CASTLE
        if (this.playerKing.isFirstMove() && !this.isInCheck() && /* If the king has not moved and is not in check */
            !this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) { /* And if the tiles on the queenside not occupied */
            final Tile rookTile = this.board.getTile(56);
            if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                if (Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) {


                    //TODO more to come here (castling)
                    kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }


        return Collections.unmodifiableList(new LinkedList<>(kingCastles));
    }
}
