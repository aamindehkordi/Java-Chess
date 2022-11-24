package com.chess.engine.player;

public enum MoveStatus {
    /** The move is done */
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    /** The move is illegal */
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }
    },
    /** The move leaves the player in check */
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }
    };

    /**
     * Returns true if the move is done
     * @return true if the move is done
     */
    public abstract boolean isDone();
}
