package czachor.jakub.tictactoe.server.impl.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public static final Character[] EMPTY_BOARD = {'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n', 'n'};
    private List<Character> tiles; //n - none, x, o

    public Board() {
        tiles = Arrays.asList(EMPTY_BOARD);
    }

    public void setTile(int index, Character turn) {
        List<Character> newTiles = new ArrayList<>(9);
        for (int i = 0; i < tiles.size(); i++) {
            if (i != index) {
                newTiles.add(tiles.get(i));
            } else {
                newTiles.add(turn);
            }
        }
        tiles = newTiles;
    }

    public boolean isTileEmpty(int idx) {
        return tiles.get(idx) == 'n';
    }

    public boolean isGameOver() {
        return checkTiles('x') || checkTiles('o') || draw();
    }

    public boolean xWon() {
        return checkTiles('x');
    }

    public boolean oWon() {
        return checkTiles('o');
    }

    public boolean draw() {
        return tiles.stream().noneMatch(c -> c.equals('n'));
    }

    private Boolean checkTiles(char state) {
        //0|1|2
        //3|4|5
        //6|7|8
        return checkLine(0, 1, 2, state)
                || checkLine(3, 4, 5, state)
                || checkLine(6, 7, 8, state)
                || checkLine(0, 3, 6, state)
                || checkLine(1, 4, 7, state)
                || checkLine(2, 5, 8, state)
                || checkLine(0, 4, 8, state)
                || checkLine(2, 4, 6, state);
    }

    private Boolean checkLine(int t1, int t2, int t3, char s) {
        return tiles.get(t1).equals(s) && tiles.get(t2).equals(s) && tiles.get(t3).equals(s);
    }

    public List<Character> getTiles() {
        return tiles;
    }
}
