package com.chess;

import com.chess.model.board.Board;
import com.chess.view.Table;

public class OOPChess {

    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);

        Table.get().show();
    }

}
