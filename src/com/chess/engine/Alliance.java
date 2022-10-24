package com.chess.engine;

public enum Alliance {

    /* the white alliance */
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
    },

    /* the black alliance */
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
    };

    /* Get the direction of the alliance
     *
     * @return the direction of the alliance : -1 for white, 1 for black
     */
    public abstract int getDirection();

    /* Is the alliance white?
     *
     * @return true if the alliance is white, false otherwise
     */
    public abstract boolean isWhite();

    /* Is the alliance black?
     *
     * @return true if the alliance is black, false otherwise
     */
    public abstract boolean isBlack();

}
