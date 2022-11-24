package com.chess.pgn;

import java.util.Collections;

public class InvalidGame extends Game {
    //TODO ADD COMMENTS

    final String malformedGameText;

    public InvalidGame(final PGNGameTags tags,
                       final String malformedGameText,
                       final String outcome) {
        super(tags, Collections.emptyList(), outcome);
        this.malformedGameText = malformedGameText;
    }

    @Override
    public String toString() {
        return "Invalid Game " + this.tags;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
