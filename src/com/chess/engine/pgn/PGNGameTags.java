package com.chess.engine.pgn;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the tags of a PGN game.
 */
public class PGNGameTags {

    /**
     * The map of tags.
     */
    private final Map<String,String> gameTags;

    /**
     * Constructor for the PGNGameTags class.
     */
    private PGNGameTags(final TagsBuilder builder) {
        this.gameTags = builder.gameTags;
    }

    @Override
    public String toString() {
        return this.gameTags.toString();            //This returns the map of tags in string format.
    }

    /**
     * This method returns the value of the tag with the given name.
     * @return The value of the tag.
     */
    static class TagsBuilder {

        /**
         * The map of tags.
         */
        final Map<String,String> gameTags;

        /**
         * Constructor for the TagsBuilder class.
         */
        public TagsBuilder() {
            this.gameTags = new HashMap<>();
        }

        /**
         * This method adds a tag to the map of tags.
         * @param tagKey The name of the tag.
         * @param tagValue The value of the tag.
         * @return The builder object.
         */
        public TagsBuilder addTag(final String tagKey,
                                  final String tagValue) {
            this.gameTags.put(tagKey, tagValue);
            return this;
        }

        /**
         * This method returns the PGNGameTags object.
         * @return The PGNGameTags object.
         */
        public PGNGameTags build() {
            return new PGNGameTags(this);
        }

    }

}
