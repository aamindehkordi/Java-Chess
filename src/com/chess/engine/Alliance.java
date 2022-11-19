package com.chess.engine;

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

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    public int getOppositeDirection() {
        return -1 * this.getDirection();
    }
}
