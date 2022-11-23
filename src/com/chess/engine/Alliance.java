package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {

    /** White */
    WHITE{
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_ROW[position];
        }
    },

    /** Black */
    BLACK{
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_ROW[position];
        }
    };

    /** Get the direction of the alliance
     *
     * @return the direction of the alliance : -1 for white, 1 for black
     */
    public abstract int getDirection();

    /** Is the alliance white?
     *
     * @return true if the alliance is white, false otherwise
     */
    public abstract boolean isWhite();

    /** Is the alliance black?
     *
     * @return true if the alliance is black, false otherwise
     */
    public abstract boolean isBlack();

    /** Choose a player
     *
     * @param whitePlayer the white player
     * @param blackPlayer the black player
     * @return the player corresponding to the alliance
     */
    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    /** Is the position a pawn promotion square?
     *
     * @param position the position
     * @return true if the position is a pawn promotion square, false otherwise
     */
    public abstract boolean isPawnPromotionSquare(int position);

    /** Get the opposite alliance
     *
     * @return the opposite alliance
     */
    public int getOppositeDirection() {
        return -1 * this.getDirection();
    }
}
