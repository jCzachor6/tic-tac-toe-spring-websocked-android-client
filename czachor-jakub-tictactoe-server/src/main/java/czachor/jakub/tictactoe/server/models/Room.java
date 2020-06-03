package czachor.jakub.tictactoe.server.models;

import czachor.jakub.statemachine.StateMachine;
import czachor.jakub.tictactoe.server.message.Action;
import czachor.jakub.tictactoe.server.util.RoomTransactions;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Room extends StateMachine<RoomState> {
    private static Long idCounter = 1L;
    public static String[] EMPTY_ROOM = {"n", "n", "n", "n", "n", "n", "n", "n", "n"};

    private Long id;
    private Player playerOne;
    private Player playerTwo;
    private String whoStarted;
    private List<String> tiles; //n - none, x, o
    private Action currentAction;

    public Room() {
        super(RoomState.EMPTY);
        id = Room.idCounter;
        playerOne = null;
        playerTwo = null;
        whoStarted = null;
        tiles = Arrays.asList(Room.EMPTY_ROOM);
        Room.idCounter++;
        RoomTransactions.initTransitions(this);
    }

    public void clearRoom() {
        tiles = Arrays.asList(Room.EMPTY_ROOM);
    }

    public Boolean checkDraw() {
        return tiles.stream().noneMatch(tile -> tile.equals("n"));
    }

    public Boolean checkTiles(String state) {
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

    private Boolean checkLine(int tile1, int tile2, int tile3, String state) {
        return tiles.get(tile1).equals(state) && tiles.get(tile2).equals(state) && tiles.get(tile3).equals(state);
    }

    public void setTile(Player player, int tileIndex) {
        boolean isPlayerOne = playerOne.equals(player);
        if (isPlayerOne && state().equals(RoomState.PLAYER_ONE_TURN) && tiles.get(tileIndex).equals("n")) {
            setTile(tileIndex, true);
        } else if (!isPlayerOne && state().equals(RoomState.PLAYER_TWO_TURN) && tiles.get(tileIndex).equals("n")) {
            setTile(tileIndex, false);
        }
    }

    private void setTile(int index, Boolean isPlayerOne) {
        List<String> newTiles = new ArrayList<>(9);
        for (int i = 0; i < tiles.size(); i++) {
            if (i != index) {
                newTiles.add(tiles.get(i));
            } else {
                if (isPlayerOne.equals(true)) {
                    newTiles.add("x");
                } else {
                    newTiles.add("o");
                }
            }
        }
        tiles = newTiles;
    }
}
