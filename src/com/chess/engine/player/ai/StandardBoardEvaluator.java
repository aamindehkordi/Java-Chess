package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

import java.util.Dictionary;
import java.util.Hashtable;

public class StandardBoardEvaluator implements BoardEvaluator {
//TODO ADD COMMENTS

    /** Bonus for checkmate */
    private static final int CHECK_MATE_BONUS = 10000;
    /** Bonus for giving a check */
    private static final int CHECK_BONUS = 45;
    /** Bonus for earlier checkmates */
    private static final int DEPTH_BONUS = 100;
    /** Bonus for castling */
    private static final int CASTLE_BONUS = 25;

    /** Bonus for having a bishop pair */
    private static final int BISHOP_PAIR_BONUS = 25;

    /** Bonus for having a knight pair */
    private static final int KNIGHT_PAIR_BONUS = 15;

    /** Bonus for having a rook pair */
    private static final int ROOK_PAIR_BONUS = 10;

    /** Bonus for having a queen pair */
    private static final int QUEEN_PAIR_BONUS = 40;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int ATTACK_MULTIPLIER = 1;

    private static final BoardEvaluator INSTANCE = new StandardBoardEvaluator();

    private StandardBoardEvaluator() {
    }


    /** get() returns the same instance of StandardBoardEvaluator */
    public static BoardEvaluator get() {
        return INSTANCE;
    }


    @Override
    public int evaluate(Board board, int depth) {
        // Returns the score of the board
        return scorePlayer(board.whitePlayer(), depth) -
               scorePlayer(board.blackPlayer(), depth);
    }

    /** Culminates the details of the evaluation to be debugged
     *
     * @param board the board to be evaluated
     * @param depth the depth of the search
     * @return the details of the evaluation in Dictionary format
     */
    public Hashtable<String, Integer> evaluationDetails(Board board, int depth) {
        Dictionary<String, Integer> details = new Hashtable<String, Integer>();
        details.put("wMobility", mobility(board.whitePlayer()));
        details.put("wKingThreats", kingThreats(board.whitePlayer(), depth));
        details.put("wAttacks", attacks(board.whitePlayer()));
        details.put("wCastle", castled(board.whitePlayer()));
        details.put("wPieceEval", pieceEvaluations(board.whitePlayer()));
        details.put("wPawnStructure", pawnStructure(board.whitePlayer()));
        details.put("wRookStructure", rookStructure(board.whitePlayer()));
        details.put("bMobility", mobility(board.blackPlayer()));
        details.put("bKingThreats", kingThreats(board.blackPlayer(), depth));
        details.put("bAttacks", attacks(board.blackPlayer()));
        details.put("bCastle", castled(board.blackPlayer()));
        details.put("bPieceEval", pieceEvaluations(board.blackPlayer()));
        details.put("bPawnStructure", pawnStructure(board.blackPlayer()));
        details.put("bRookStructure", rookStructure(board.blackPlayer()));
        return (Hashtable<String, Integer>) details;
    }

    /** Culminates the details of the evaluation to be pretty printed
     *
     * @param board the board to be evaluated
     * @param depth the depth of the search
     * @return the details of the evaluation in string format
     */
    public String evaluationDetailsString(final Board board, final int depth) {
        Dictionary<String, Integer> details = evaluationDetails(board, depth);
        StringBuilder sb = new StringBuilder();
        sb.append("White Mobility: " + details.get("wMobility") +
                    "White King Threats: " + details.get("wKingThreats") +
                    "White Attacks: " + details.get("wAttacks") +
                    "White Castling: " + details.get("wCastle") +
                    "White Piece Evaluation: " + details.get("wPieceEval") +
                    "White Pawn Structure: " + details.get("wPawnStructure") +
                    "White Rook Structure: " + details.get("wRookStructure") +
                    "White Bishop Pair: " + details.get("wBishopPair") +
                    "White Knight Pair: " + details.get("wKnightPair") +
                    "White Rook Pair: " + details.get("wRookPair") +
                    "White Queen Pair: " + details.get("wQueenPair") +
                        "---------------------\n" +
                    "Black Mobility: " + details.get("bMobility") +
                    "Black King Threats: " + details.get("bKingThreats") +
                    "Black Attacks: " + details.get("bAttacks") +
                    "Black Castling: " + details.get("bCastle") +
                    "Black Piece Evaluation: " + details.get("bPieceEval") +
                    "Black Pawn Structure: " + details.get("bPawnStructure") +
                    "Black Rook Structure: " + details.get("bRookStructure") +
                    "Black Bishop Pair: " + details.get("bBishopPair") +
                    "Black Knight Pair: " + details.get("bKnightPair") +
                    "Black Rook Pair: " + details.get("bRookPair") +
                    "Black Queen Pair: " + details.get("bQueenPair"));
        return sb.toString();
    }

    /**
     * This method returns the score of the player
     *
     * @param player The player
     * @param depth The depth
     * @return the score of the player
     */
    private int scorePlayer(Player player, int depth) {
        return mobility(player) +
                kingThreats(player, depth) +
                attacks(player) +
                castled(player) +
                pieceEvaluations(player) +
                pawnStructure(player);

    }

    private static int attacks(final Player player) {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()) {
            if(move.isAttack()) {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if(movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceEvaluations(Player player) {
        int pieceValueScore = 0;
        int numBishops = 0;
        int numKnights = 0;
        int numRooks = 0;
        int numQueens = 0;
        for (final Piece piece : player.getActivePieces()) {
            if (piece.getPieceType().isBishop()) {
                numBishops++;
            } else if (piece.getPieceType().isKnight()) {
                numKnights++;
            } else if (piece.getPieceType().isRook()) {
                numRooks++;
            } else if (piece.getPieceType().isQueen()) {
                numQueens++;
            }
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore + (numBishops == 2 ? BISHOP_PAIR_BONUS : 0) +
                (numKnights == 2 ? KNIGHT_PAIR_BONUS : 0) +
                (numRooks == 2 ? ROOK_PAIR_BONUS : 0) +
                (numQueens == 2 ? QUEEN_PAIR_BONUS : 0);
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        return (int)((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
    }

    private int kingThreats(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : check(player);
    }

    /**
     * This method returns the score of the player's check
     *
     * @param player The player
     * @return the score of the player's check
     */
    private int check(Player player) {
        return player.isInCheck() ? CHECK_BONUS : 0; // If the player is in check, return the check bonus
    }

    /**
     * This method returns the score of the player's castling
     *
     * @param player The player
     * @return the score of the player's castling
     */
    private int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0; // If the player has castled, return the castling bonus
    }

    /**
     * This method returns the depth bonus
     *
     * @param depth The depth
     * @return the depth bonus
     */
    private int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth; // If the depth is 0, return 1, otherwise return the depth bonus
    }

    private static int pawnStructure(Player player) {
        return PawnStructureAnalyzer.get().pawnStructureScore(player);
    }

    private static int rookStructure(Player player) {
        return RookStructureAnalyzer.get().rookStructureScore(player);
    }


}
