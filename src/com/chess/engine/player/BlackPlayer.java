package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.*;

public class BlackPlayer extends Player {
    /** Constructor
     *
     * @param board the board
     * @param whiteStandardLegalMoves the white player's legal moves
     * @param blackStandardLegalMoves the black player's legal moves
     */
    public BlackPlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
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
     * @param playerLegals
     * @param opponentLegals
     * @return
     */
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>(); /* List of legal moves */

        // KING SIDE CASTLE
        if (this.playerKing.isFirstMove() && !this.isInCheck() && /* If the king has not moved and is not in check */
            !this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) { /* And if the tiles on the kingside are not occupied */
            final Tile rookTile = this.board.getTile(7); /* Get the tile of the rook */
            if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) { /* If the rook has not moved */
                if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() && /* If the tiles on the kingside are not attacked */
                    Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() && /* If the tiles on the kingside are not attacked */
                    rookTile.getPiece().getPieceType().isRook()) { /* If the piece on the tile is a rook */
                    //TODO more to come here (castling)
                    kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5)); /* Add the move to the list of legal moves */
                }
            }
        }
        // QUEEN SIDE CASTLE
        if (this.playerKing.isFirstMove() && !this.isInCheck() && /* If the king has not moved and is not in check */
            !this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied() && !this.board.getTile(3).isTileOccupied()) { /* And if the tiles on the queenside are not occupied */
            final Tile rookTile = this.board.getTile(0); /* Get the tile on the queenside */
            if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) { /* If the rook has not moved */
                if (Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() && /* If the tiles on the queenside are not attacked */
                    Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() && /* If the tiles on the queenside are not attacked */
                    rookTile.getPiece().getPieceType().isRook()) { /* If the piece on the tile is a rook */
                    //TODO more to come here (castling)
                    kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3)); /* Add the move to the list of legal moves */
                }
            }
        }
        return Collections.unmodifiableList(new LinkedList<>(kingCastles)); /* Return the list of legal moves */
    }
}
