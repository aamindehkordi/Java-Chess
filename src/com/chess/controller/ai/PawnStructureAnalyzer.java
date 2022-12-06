package com.chess.controller.ai;

import com.chess.model.pieces.Piece;
import com.chess.model.pieces.PieceType;
import com.chess.controller.Player;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class is used to analyze the pawn structure of a player.
 */
public final class PawnStructureAnalyzer {


    private static final PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer(); //Singleton

    public static final int ISOLATED_PAWN_PENALTY = -10;       //Penalty for having an isolated pawn
    public static final int DOUBLED_PAWN_PENALTY = -10;        //Penalty for having a doubled pawn

    private PawnStructureAnalyzer() {                          //Private constructor for singleton
    }

    /**
     * Returns the singleton instance of the PawnStructureAnalyzer.
     */
    public static PawnStructureAnalyzer get() {
        return INSTANCE;
    }

    /**
     * Returns the penalty for an isolated pawn.
     */
    public int isolatedPawnPenalty(final Player player) {
        return calculateIsolatedPawnPenalty(createPawnColumnTable(calculatePlayerPawns(player)));
    }

    /**
     * Returns the penalty for a doubled pawn.
     */
    public int doubledPawnPenalty(final Player player) {
        return calculatePawnColumnStack(createPawnColumnTable(calculatePlayerPawns(player)));
    }

    /**
     * Returns the score for the pawn structure of a player.
     */
    public int pawnStructureScore(final Player player) {
        final int[] pawnsOnColumnTable = createPawnColumnTable(calculatePlayerPawns(player));
        return calculatePawnColumnStack(pawnsOnColumnTable) + calculateIsolatedPawnPenalty(pawnsOnColumnTable);
    }

    /**
     * Returns the number of pawns on each column.
     */
    private static Collection<Piece> calculatePlayerPawns(final Player player) {
        return player.getActivePieces().stream().filter(piece -> piece.getPieceType() == PieceType.PAWN).collect(Collectors.toList());
    }

    /**
     * Checks for a pawn stack to calculate the penalty for a doubled pawn.
     */
    private static int calculatePawnColumnStack(final int[] pawnsOnColumnTable) {
        int pawnStackPenalty = 0;
        for(final int pawnStack : pawnsOnColumnTable) {                             //For each column
            if(pawnStack > 1) {                                                     //If there is more than one pawn
                pawnStackPenalty += pawnStack;
            }
        }
        return pawnStackPenalty * DOUBLED_PAWN_PENALTY;                             //Return the penalty
    }

    /**
     * Checks for an isolated pawn to calculate the penalty for an isolated pawn.
     */
    private static int calculateIsolatedPawnPenalty(final int[] pawnsOnColumnTable) {
        int numIsolatedPawns = 0;
        if(pawnsOnColumnTable[0] > 0 && pawnsOnColumnTable[1] == 0) {               //If there is a pawn on the first column and no pawn on the second column
            numIsolatedPawns += pawnsOnColumnTable[0];
        }
        if(pawnsOnColumnTable[7] > 0 && pawnsOnColumnTable[6] == 0) {               //If there is a pawn on the last column and no pawn on the second to last column
            numIsolatedPawns += pawnsOnColumnTable[7];
        }
        for(int i = 1; i < pawnsOnColumnTable.length - 1; i++) {
            if((pawnsOnColumnTable[i-1] == 0 && pawnsOnColumnTable[i+1] == 0)) {    //If there is a pawn on the current column and no pawn on the column to the left or right
                numIsolatedPawns += pawnsOnColumnTable[i];
            }
        }
        return numIsolatedPawns * ISOLATED_PAWN_PENALTY;                            //Return the penalty for the isolated pawns
    }

    /**
     * Creates a table of the number of pawns on each column.
     */
    private static int[] createPawnColumnTable(final Collection<Piece> playerPawns) {
        final int[] table = new int[8];
        for(final Piece playerPawn : playerPawns) {                                 //For each pawn
            table[playerPawn.getPiecePosition() % 8]++;
        }
        return table;                                                               //Return the table
    }

}
