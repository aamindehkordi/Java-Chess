package com.chess.model.pgn;

import com.chess.model.board.Board;
import com.chess.model.board.Move;
import com.chess.controller.Player;

public interface PGNPersistence {

    void persistGame(Game game);

    Move getNextBestMove(Board board, Player player, String gameText);

}
