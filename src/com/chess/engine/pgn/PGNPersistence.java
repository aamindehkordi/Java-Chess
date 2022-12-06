package com.chess.engine.pgn;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.player.Player;

public interface PGNPersistence {

    void persistGame(Game game);

    Move getNextBestMove(Board board, Player player, String gameText);

}
