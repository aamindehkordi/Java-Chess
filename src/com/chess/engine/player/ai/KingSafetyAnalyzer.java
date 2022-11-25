package com.chess.engine.player.ai;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

import java.util.*;

/**
 * This class analyzes the king safety of a player's king
 */
public final class KingSafetyAnalyzer {
//TODO ADD COMMENTS

    private static final KingSafetyAnalyzer INSTANCE = new KingSafetyAnalyzer();        //Initializing Singleton
    private static final List<boolean[]> COLUMNS = initColumns();                       //List of columns


    private KingSafetyAnalyzer() {                              //Private constructor for Singleton
    }

    /* Returns the singleton instance of the class */
    public static KingSafetyAnalyzer get() {
        return INSTANCE;
    }

    /**
     * This is an initialization method for the columns
     */
    private static List<boolean[]> initColumns() {
        final List<boolean[]> columns = new ArrayList<>();       //Initializing the list
        columns.add(BoardUtils.FIRST_COLUMN);                    //Adding the first column
        columns.add(BoardUtils.SECOND_COLUMN);
        columns.add(BoardUtils.THIRD_COLUMN);
        columns.add(BoardUtils.FOURTH_COLUMN);                   //...
        columns.add(BoardUtils.FIFTH_COLUMN);
        columns.add(BoardUtils.SIXTH_COLUMN);
        columns.add(BoardUtils.SEVENTH_COLUMN);
        columns.add(BoardUtils.EIGHTH_COLUMN);
        return Collections.unmodifiableList(new LinkedList<>(columns));   //Returning the list
    }

    /**
     * This method does the analysis of the king safety
     *
     * @param player The player
     * @return the number of safe moves
     */
    public KingDistance calculateKingTropism(final Player player) {
        final int playerKingSquare = player.getPlayerKing().getPiecePosition();         //Getting the king's position
        final Collection<Move> enemyMoves = player.getOpponent().getLegalMoves();       //Getting the opponent's legal moves
        Piece closestPiece = null;                                                      //Initializing the closest piece
        int closestDistance = Integer.MAX_VALUE;                                        //Initializing the closest distance
        for(final Move move : enemyMoves) {                                             //Iterating over the opponent's legal moves
            final int currentDistance = calculateChebyshevDistance(playerKingSquare, move.getDestinationCoordinate()); //Calculating the distance
            if(currentDistance < closestDistance) {                                     //If the current distance is less than the closest distance
                closestDistance = currentDistance;                                      //Updating the closest distance
                closestPiece = move.getMovedPiece();                                    //Updating the closest piece
            }
        }
        return new KingDistance(closestPiece, closestDistance);                         //Returning the king distance
    }

    /**
     * This method calculates the Chebyshev distance between two squares
     *
     * @param kingTileId The king's position
     * @param enemyAttackTileId The enemy's attack position
     * @return the Chebyshev distance
     */
    private int calculateChebyshevDistance(final int kingTileId,                        //Calculating the Chebyshev distance
                                           final int enemyAttackTileId) {               //between the king and the enemy attack tile

        final int squareOneRank = getRank(kingTileId);                                  //Getting the rank of the first square
        final int squareTwoRank = getRank(enemyAttackTileId);                           //Getting the rank of the second square

        final int squareOneFile = getFile(kingTileId);                                  //Getting the file of the first square
        final int squareTwoFile = getFile(enemyAttackTileId);                           //Getting the file of the second square

        final int rankDistance = Math.abs(squareTwoRank - squareOneRank);               //Calculating the rank distance
        final int fileDistance = Math.abs(squareTwoFile - squareOneFile);               //Calculating the file distance

        return Math.max(rankDistance, fileDistance);                                    //Returning the maximum of the two distances
    }

    /**
     * This method returns the file of a square
     *
     * @param coordinate The square
     * @return the file
     */
    private static int getFile(final int coordinate) {                       //Getting the file of a square
        if(BoardUtils.FIRST_COLUMN[coordinate]) {                            //If the square is in the first column
            return 1;                                                        //Returning 1
        } else if(BoardUtils.SECOND_COLUMN[coordinate]) {                    //If the square is in the second column
            return 2;                                                        //Returning 2
        } else if(BoardUtils.THIRD_COLUMN[coordinate]) {
            return 3;
        } else if(BoardUtils.FOURTH_COLUMN[coordinate]) {
            return 4;
        } else if(BoardUtils.FIFTH_COLUMN[coordinate]) {                      //...
            return 5;
        } else if(BoardUtils.SIXTH_COLUMN[coordinate]) {
            return 6;
        } else if(BoardUtils.SEVENTH_COLUMN[coordinate]) {
            return 7;
        } else if(BoardUtils.EIGHTH_COLUMN[coordinate]) {
            return 8;
        }
        throw new RuntimeException("should not reach here!");                  //Throwing an exception
    }

    /**
     * This method returns the rank of a square
     *
     * @param coordinate The square
     * @return the rank
     */
    private static int getRank(final int coordinate) {                        //Getting the rank of a square
        if(BoardUtils.FIRST_ROW[coordinate]) {                                //If the square is in the first row
            return 1;                                                         //Returning 1
        } else if(BoardUtils.SECOND_ROW[coordinate]) {
            return 2;
        } else if(BoardUtils.THIRD_ROW[coordinate]) {
            return 3;
        } else if(BoardUtils.FOURTH_ROW[coordinate]) {                        //...
            return 4;
        } else if(BoardUtils.FIFTH_ROW[coordinate]) {
            return 5;
        } else if(BoardUtils.SIXTH_ROW[coordinate]) {
            return 6;
        } else if(BoardUtils.SEVENTH_ROW[coordinate]) {
            return 7;
        } else if(BoardUtils.EIGHTH_ROW[coordinate]) {
            return 8;
        }
        throw new RuntimeException("should not reach here!");                  //Throwing an exception
    }

    /**
     * This class represents the king distance
     * Returns the closest enemy piece and the distance to the king
     */
    static class KingDistance {

        /**
         * this is the closest enemy piece
         */
        final Piece enemyPiece;

        /**
         * this is the distance to the king
         */
        final int distance;

        /**
         * This is the constructor
         *
         * @param enemyDistance The closest enemy piece
         * @param distance The distance to the king
         */
        KingDistance(final Piece enemyDistance,
                     final int distance) {
            this.enemyPiece = enemyDistance;
            this.distance = distance;
        }

        /**
         * This method returns the closest enemy piece
         *
         * @return the closest enemy piece
         */
        public Piece getEnemyPiece() {
            return enemyPiece;
        }

        /**
         * This method returns the distance to the king
         *
         * @return the distance to the king
         */
        public int getDistance() {
            return distance;
        }

        /**
         * This method returns the string representation of the king distance
         *
         * @return the string representation of the king distance
         */
        public int tropismScore() {
            return (enemyPiece.getPieceValue()/10) * distance;
        }

    }

}
