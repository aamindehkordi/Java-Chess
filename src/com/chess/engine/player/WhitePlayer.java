package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.pieces.PieceType.ROOK;

public class WhitePlayer extends Player {

    private final Collection<Move> opponentLegals;

    /** Constructor
     *
     * @param board the board
     * @param whiteStandardLegalMoves the white player's legal moves
     * @param blackStandardLegalMoves the black player's legal moves
     */
    public WhitePlayer(final Board board, final Collection<Move> whiteStandardLegalMoves, final Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.opponentLegals = blackStandardLegalMoves;
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
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {

        // If the king has no castle opportunities
        if(!hasCastleOpportunities()) {
            return Collections.emptyList();
        }

        final List<Move> kingCastles = new ArrayList<>(); // List of legal moves

        //White Castling requirements
        if(this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()) {
            //WHITE KING SIDE CASTLE
            // If the tiles on the kingside are not occupied
            if(!this.board.getTile(61).isTileOccupied()  && !this.board.getTile(62).isTileOccupied()) {
                // Get the tile of the kingside rook
                final Piece kingSideRook = this.board.getTile(63).getPiece();
                // If the rook has not moved and its tile is not occupied
                if(kingSideRook != null && kingSideRook.isFirstMove()) {
                    // If the tiles on the kingside are not attacked
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                            kingSideRook.getPieceType() == ROOK) {
                        // Add the move to the list of legal moves
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook) kingSideRook, kingSideRook.getPiecePosition(), 61));
                        }
                    }
                }
            }
            //WHITE QUEEN SIDE CASTLE
            // If the tiles on the queenside are not occupied
            if(!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()) {
                // Get the tile of the queenside rook
                final Piece queenSideRook = this.board.getTile(56).getPiece();
                // If the rook has not moved and its tile is not occupied
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    // If the tiles on the queenside are not attacked
                    if(Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                            queenSideRook.getPieceType() == ROOK) {
                        // Add the move to the list of legal moves
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook) queenSideRook, queenSideRook.getPiecePosition(), 59));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }

}
